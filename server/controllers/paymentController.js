const { Payment, Booking, memoryStore, generateId, isMongoReady } = require('../models');
const { calculateFeeBreakdown } = require('../services/cooperativeEngine');

/**
 * @desc    Process a booking payment
 * @route   POST /api/payments
 * @access  Private (Customer)
 */
const createPayment = async (req, res, next) => {
  try {
    const { bookingId, amount, paymentMethod = 'upi' } = req.body;

    if (!bookingId || !amount) {
      return res.status(400).json({
        success: false,
        message: 'Missing bookingId or amount'
      });
    }

    const breakdown = calculateFeeBreakdown(amount);
    const paymentId = generateId();

    const newPayment = {
      _id: paymentId,
      booking: bookingId,
      customer: req.user ? req.user._id : 'usr_cust_100',
      amount: breakdown.amount,
      cooperativeFee: breakdown.platformFee,
      workerPayout: breakdown.workerEarnings,
      welfareAllocation: breakdown.welfareContribution,
      paymentMethod,
      status: 'completed',
      transactionId: `TXN-HMZ-PAY-${Date.now()}`,
      paidAt: new Date(),
      createdAt: new Date()
    };

    if (isMongoReady()) {
      await Payment.create(newPayment);
      await Booking.findByIdAndUpdate(bookingId, { payment: paymentId });
    } else {
      memoryStore.payments.push(newPayment);
      const b = memoryStore.bookings.find(item => String(item._id) === String(bookingId));
      if (b) b.payment = paymentId;
    }

    res.status(201).json({
      success: true,
      message: 'Payment processed successfully with 5% transparent cooperative fee',
      data: newPayment
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Get payment by ID
 * @route   GET /api/payments/:id
 * @access  Private
 */
const getPaymentById = async (req, res, next) => {
  try {
    const { id } = req.params;

    let payment = null;
    if (isMongoReady()) {
      payment = await Payment.findById(id);
    } else {
      payment = memoryStore.payments.find(p => String(p._id) === String(id) || String(p.booking) === String(id));
    }

    if (!payment) {
      return res.status(404).json({
        success: false,
        message: `Payment not found with id ${id}`
      });
    }

    res.status(200).json({
      success: true,
      data: payment
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  createPayment,
  getPaymentById
};
