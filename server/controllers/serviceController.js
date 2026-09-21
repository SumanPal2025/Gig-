const { Service, memoryStore, isMongoReady } = require('../models');

/**
 * @desc    Get all available cooperative services
 * @route   GET /api/services
 * @access  Public
 */
const getServices = async (req, res, next) => {
  try {
    let services = [];
    if (isMongoReady()) {
      services = await Service.find({ active: true });
    } else {
      services = memoryStore.services.filter(s => s.active !== false);
    }

    res.status(200).json({
      success: true,
      count: services.length,
      data: services
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getServices
};
