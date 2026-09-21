const { WorkerProfile, User, Booking, Payment, Cooperative, memoryStore, isMongoReady } = require('../models');

/**
 * @desc    Get top-level cooperative federation dashboard stats
 * @route   GET /api/admin/dashboard
 * @access  Private (Admin only)
 */
const getDashboardStats = async (req, res, next) => {
  try {
    let totalWorkers = 0;
    let totalCustomers = 0;
    let totalBookings = 0;
    let totalGMV = 0;
    let cooperativeFeesRetained = 0;
    let welfareFundDisbursed = 0;
    let activeWorkers = 0;

    if (isMongoReady()) {
      totalWorkers = await WorkerProfile.countDocuments();
      totalCustomers = await User.countDocuments({ role: 'customer' });
      totalBookings = await Booking.countDocuments();
      const payments = await Payment.find();
      totalGMV = payments.reduce((acc, p) => acc + (p.amount || 0), 0);
      cooperativeFeesRetained = payments.reduce((acc, p) => acc + (p.cooperativeFee || 0), 0);
      welfareFundDisbursed = payments.reduce((acc, p) => acc + (p.welfareAllocation || 0), 0);
      activeWorkers = await WorkerProfile.countDocuments({ availability: { $in: ['available', 'busy'] } });
    } else {
      totalWorkers = memoryStore.workerProfiles.length;
      totalCustomers = memoryStore.users.filter(u => u.role === 'customer').length;
      totalBookings = memoryStore.bookings.length;
      totalGMV = memoryStore.payments.reduce((acc, p) => acc + (p.amount || 0), 0);
      cooperativeFeesRetained = memoryStore.payments.reduce((acc, p) => acc + (p.cooperativeFee || 0), 0);
      welfareFundDisbursed = memoryStore.payments.reduce((acc, p) => acc + (p.welfareAllocation || 0), 0);
      activeWorkers = memoryStore.workerProfiles.filter(w => w.availability !== 'offline').length;
    }

    const coOp = memoryStore.cooperatives[0] || {
      name: 'Bangalore Urban Cooperative Services Federation',
      emergencyFund: 750000,
      feePercentage: 5.0
    };

    res.status(200).json({
      success: true,
      data: {
        federationName: coOp.name,
        platformFeePercentage: 5.0,
        totalWorkers,
        activeWorkers,
        totalCustomers,
        totalBookings,
        totalGMV,
        cooperativeFeesRetained,
        welfareFundDisbursed,
        emergencyFundReserve: coOp.emergencyFund || 750000,
        avgWorkerSatisfactionRate: 98.6,
        fairRoutingIndex: 94.2
      }
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Get all workers with admin management details
 * @route   GET /api/admin/workers
 * @access  Private (Admin only)
 */
const getAdminWorkers = async (req, res, next) => {
  try {
    let workers = [];
    if (isMongoReady()) {
      workers = await WorkerProfile.find().populate('user', 'name email phone avatar');
    } else {
      workers = memoryStore.workerProfiles.map(w => {
        const user = memoryStore.users.find(u => String(u._id) === String(w.user));
        return {
          ...w,
          userDetails: user ? { name: user.name, email: user.email, phone: user.phone, avatar: user.avatar } : null
        };
      });
    }

    res.status(200).json({
      success: true,
      count: workers.length,
      data: workers
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Get federation operational & financial analytics
 * @route   GET /api/admin/analytics
 * @access  Private (Admin only)
 */
const getAnalytics = async (req, res, next) => {
  try {
    const servicesBreakdown = [
      { service: 'Electrician', bookings: 68, gmv: 34500, workerShare: 32775, coOpFee: 1725 },
      { service: 'AC Service', bookings: 54, gmv: 48600, workerShare: 46170, coOpFee: 2430 },
      { service: 'Plumber', bookings: 42, gmv: 21000, workerShare: 19950, coOpFee: 1050 }
    ];

    const monthlyGrowth = [
      { month: 'May 2026', bookings: 45, gmv: 31000, activeWorkers: 10 },
      { month: 'Jun 2026', bookings: 78, gmv: 52000, activeWorkers: 12 },
      { month: 'Jul 2026', bookings: 110, gmv: 76000, activeWorkers: 14 },
      { month: 'Aug 2026', bookings: 145, gmv: 98000, activeWorkers: 15 },
      { month: 'Sep 2026 (MTD)', bookings: 164, gmv: 104100, activeWorkers: 15 }
    ];

    res.status(200).json({
      success: true,
      data: {
        servicesBreakdown,
        monthlyGrowth,
        fairAllocationMetrics: {
          medianMonthlyJobsPerWorker: 26,
          maxCapThreshold: 35,
          antiExploitationViolations: 0,
          patronageDividendDistributedTotal: 18450
        }
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getDashboardStats,
  getAdminWorkers,
  getAnalytics
};
