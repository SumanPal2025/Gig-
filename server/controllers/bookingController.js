const { Booking, WorkerProfile, Service, Payment, Notification, memoryStore, generateId, isMongoReady } = require('../models');
const { calculateFeeBreakdown } = require('../services/cooperativeEngine');

/**
 * @desc    Create a new service booking
 * @route   POST /api/bookings
 * @access  Private (Customer)
 */
const createBooking = async (req, res, next) => {
  try {
    const { workerId, serviceId, scheduledDate, timeSlot, customerAddress, notes, customerPhone } = req.body;

    if (!workerId || !serviceId || !scheduledDate || !timeSlot || !customerAddress) {
      return res.status(400).json({
        success: false,
        message: 'Missing required booking fields (workerId, serviceId, scheduledDate, timeSlot, customerAddress)'
      });
    }

    // Find service to compute price
    let service;
    if (isMongoReady()) {
      service = await Service.findById(serviceId);
    } else {
      service = memoryStore.services.find(s => String(s._id) === String(serviceId));
    }

    const baseAmount = service ? service.basePrice : 499;
    const feeCalculation = calculateFeeBreakdown(baseAmount);

    const bookingId = generateId();
    const customerId = req.user ? req.user._id : req.body.customerId || 'usr_cust_100';

    const newBooking = {
      _id: bookingId,
      customer: customerId,
      worker: workerId,
      service: serviceId,
      scheduledDate,
      timeSlot,
      status: 'requested',
      amount: feeCalculation.amount,
      platformFee: feeCalculation.platformFee,
      workerEarnings: feeCalculation.workerEarnings,
      welfareContribution: feeCalculation.welfareContribution,
      customerAddress,
      customerPhone: customerPhone || (req.user ? req.user.phone : ''),
      notes: notes || '',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    if (isMongoReady()) {
      await Booking.create(newBooking);
    } else {
      memoryStore.bookings.unshift(newBooking);
    }

    // Auto create pending Payment record
    const paymentId = generateId();
    const newPayment = {
      _id: paymentId,
      booking: bookingId,
      customer: customerId,
      worker: workerId,
      amount: feeCalculation.amount,
      cooperativeFee: feeCalculation.platformFee,
      workerPayout: feeCalculation.workerEarnings,
      welfareAllocation: feeCalculation.welfareContribution,
      paymentMethod: 'upi',
      status: 'pending',
      transactionId: `TXN-HMZ-${Date.now()}`,
      paidAt: new Date()
    };

    if (isMongoReady()) {
      await Payment.create(newPayment);
    } else {
      memoryStore.payments.push(newPayment);
    }

    // Update worker current workload
    if (isMongoReady()) {
      await WorkerProfile.findByIdAndUpdate(workerId, { $inc: { currentWorkload: 1 } });
    } else {
      const w = memoryStore.workerProfiles.find(item => String(item._id) === String(workerId));
      if (w) w.currentWorkload = (w.currentWorkload || 0) + 1;
    }

    res.status(201).json({
      success: true,
      message: 'Booking created successfully with fair cooperative fee structure',
      data: newBooking,
      feeBreakdown: feeCalculation
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Get bookings (filtered for current user role or query params)
 * @route   GET /api/bookings
 * @access  Private
 */
const getBookings = async (req, res, next) => {
  try {
    const { status, workerId, customerId } = req.query;

    let bookings = [];
    if (isMongoReady()) {
      const query = {};
      if (status) query.status = status;
      if (workerId) query.worker = workerId;
      if (customerId) query.customer = customerId;

      // Restrict by logged in user role if protected
      if (req.user) {
        if (req.user.role === 'customer') {
          query.customer = req.user._id;
        } else if (req.user.role === 'worker') {
          const wp = await WorkerProfile.findOne({ user: req.user._id });
          if (wp) query.worker = wp._id;
        }
      }

      bookings = await Booking.find(query).sort({ createdAt: -1 });
    } else {
      bookings = [...memoryStore.bookings];

      if (status) {
        bookings = bookings.filter(b => b.status === status);
      }
      if (workerId) {
        bookings = bookings.filter(b => String(b.worker) === String(workerId));
      }
      if (customerId) {
        bookings = bookings.filter(b => String(b.customer) === String(customerId));
      }

      if (req.user) {
        if (req.user.role === 'customer') {
          bookings = bookings.filter(b => String(b.customer) === String(req.user._id));
        } else if (req.user.role === 'worker') {
          const wp = memoryStore.workerProfiles.find(w => String(w.user) === String(req.user._id));
          if (wp) {
            bookings = bookings.filter(b => String(b.worker) === String(wp._id));
          }
        }
      }
    }

    // Enrich with worker and service names
    const enriched = bookings.map(b => {
      const obj = b.toObject ? b.toObject() : { ...b };
      const worker = memoryStore.workerProfiles.find(w => String(w._id) === String(obj.worker));
      const service = memoryStore.services.find(s => String(s._id) === String(obj.service));
      const customer = memoryStore.users.find(u => String(u._id) === String(obj.customer));

      return {
        ...obj,
        workerName: worker ? worker.name : 'Co-op Worker',
        workerTrade: worker ? worker.trade : 'Technician',
        workerPhone: worker ? worker.phone : '',
        serviceName: service ? service.name : 'Home Service',
        customerName: customer ? customer.name : 'Customer'
      };
    });

    res.status(200).json({
      success: true,
      count: enriched.length,
      data: enriched
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Get single booking by ID
 * @route   GET /api/bookings/:id
 * @access  Private
 */
const getBookingById = async (req, res, next) => {
  try {
    const { id } = req.params;

    let booking = null;
    if (isMongoReady()) {
      booking = await Booking.findById(id);
    } else {
      booking = memoryStore.bookings.find(b => String(b._id) === String(id));
    }

    if (!booking) {
      return res.status(404).json({
        success: false,
        message: `Booking not found with id ${id}`
      });
    }

    const obj = booking.toObject ? booking.toObject() : { ...booking };
    const worker = memoryStore.workerProfiles.find(w => String(w._id) === String(obj.worker));
    const service = memoryStore.services.find(s => String(s._id) === String(obj.service));
    const customer = memoryStore.users.find(u => String(u._id) === String(obj.customer));
    const payment = memoryStore.payments.find(p => String(p.booking) === String(obj._id));

    res.status(200).json({
      success: true,
      data: {
        ...obj,
        workerName: worker ? worker.name : 'Co-op Worker',
        workerTrade: worker ? worker.trade : 'Technician',
        workerPhone: worker ? worker.phone : '',
        serviceName: service ? service.name : 'Home Service',
        customerName: customer ? customer.name : 'Customer',
        payment
      }
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Update booking status (e.g. accepted, in_progress, completed, cancelled)
 * @route   PUT /api/bookings/:id/status
 * @access  Private
 */
const updateBookingStatus = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { status } = req.body;

    const validStatuses = ['requested', 'accepted', 'in_progress', 'completed', 'cancelled'];
    if (!status || !validStatuses.includes(status)) {
      return res.status(400).json({
        success: false,
        message: `Invalid status. Must be one of: ${validStatuses.join(', ')}`
      });
    }

    let booking = null;
    if (isMongoReady()) {
      booking = await Booking.findByIdAndUpdate(
        id,
        { status, updatedAt: new Date() },
        { new: true }
      );
    } else {
      const item = memoryStore.bookings.find(b => String(b._id) === String(id));
      if (item) {
        item.status = status;
        item.updatedAt = new Date();
        booking = item;
      }
    }

    if (!booking) {
      return res.status(404).json({
        success: false,
        message: 'Booking not found'
      });
    }

    // If completed, update worker completedJobs and total earnings
    if (status === 'completed') {
      const workerId = booking.worker;
      if (isMongoReady()) {
        await WorkerProfile.findByIdAndUpdate(workerId, {
          $inc: {
            completedJobs: 1,
            totalEarnings: booking.workerEarnings || 0,
            currentWorkload: -1
          }
        });
      } else {
        const w = memoryStore.workerProfiles.find(item => String(item._id) === String(workerId));
        if (w) {
          w.completedJobs = (w.completedJobs || 0) + 1;
          w.totalEarnings = (w.totalEarnings || 0) + (booking.workerEarnings || 0);
          w.currentWorkload = Math.max(0, (w.currentWorkload || 1) - 1);
        }
      }
    }

    res.status(200).json({
      success: true,
      message: `Booking status updated to ${status}`,
      data: booking
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  createBooking,
  getBookings,
  getBookingById,
  updateBookingStatus
};
