const { Welfare, memoryStore, isMongoReady } = require('../models');

/**
 * @desc    Get worker welfare stats, insurance coverage, emergency fund, and claims
 * @route   GET /api/welfare/:workerId
 * @access  Private (Worker themselves or Admin)
 */
const getWorkerWelfare = async (req, res, next) => {
  try {
    const { workerId } = req.params;

    let welfare = null;
    if (isMongoReady()) {
      welfare = await Welfare.findOne({ worker: workerId });
    } else {
      welfare = memoryStore.welfares.find(wf => String(wf.worker) === String(workerId) || String(wf._id) === String(workerId));
    }

    if (!welfare) {
      // Return a default co-op welfare guarantee if not yet specifically provisioned
      const defaultWelfare = {
        worker: workerId,
        insuranceActive: true,
        insuranceCoverage: 500000,
        emergencyFundBalance: 15000,
        pensionAccrued: 8200,
        patronageBonusYTD: 3450,
        healthCheckupStatus: 'completed',
        claims: []
      };
      return res.status(200).json({
        success: true,
        data: defaultWelfare
      });
    }

    res.status(200).json({
      success: true,
      data: welfare
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Update worker welfare or add a claim
 * @route   PUT /api/welfare/:workerId
 * @access  Private (Worker or Admin)
 */
const updateWorkerWelfare = async (req, res, next) => {
  try {
    const { workerId } = req.params;
    const { newClaim, healthCheckupStatus } = req.body;

    let welfare = null;
    if (isMongoReady()) {
      welfare = await Welfare.findOne({ worker: workerId });
      if (welfare) {
        if (healthCheckupStatus) welfare.healthCheckupStatus = healthCheckupStatus;
        if (newClaim) {
          welfare.claims.push({
            title: newClaim.title,
            amount: newClaim.amount,
            status: 'approved',
            disbursedAt: new Date()
          });
        }
        await welfare.save();
      }
    } else {
      welfare = memoryStore.welfares.find(wf => String(wf.worker) === String(workerId));
      if (welfare) {
        if (healthCheckupStatus) welfare.healthCheckupStatus = healthCheckupStatus;
        if (newClaim) {
          welfare.claims.push({
            title: newClaim.title,
            amount: newClaim.amount,
            status: 'approved',
            disbursedAt: new Date()
          });
        }
      }
    }

    if (!welfare) {
      return res.status(404).json({
        success: false,
        message: 'Welfare record not found for this worker'
      });
    }

    res.status(200).json({
      success: true,
      message: 'Worker welfare record updated',
      data: welfare
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getWorkerWelfare,
  updateWorkerWelfare
};
