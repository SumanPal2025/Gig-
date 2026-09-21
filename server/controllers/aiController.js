const { WorkerProfile, memoryStore, isMongoReady } = require('../models');
const { computeSmartMatchScore, evaluateWorkforceAllocation, getZoneWorkforceAllocations } = require('../services/aiMatchingEngine');
const { calculateFairPriceSuggestion } = require('../services/aiFairPriceService');

/**
 * @desc    AI Smart Matching Prototype
 * @route   POST /api/ai/smart-match
 * @access  Public
 */
const handleSmartMatch = async (req, res, next) => {
  try {
    const { service = 'AC Service', requiredSkill = '', customerLocation = 'Indiranagar, Bengaluru', preferredTime = 'Morning' } = req.body;

    let workers = [];
    if (isMongoReady()) {
      workers = await WorkerProfile.find({}).lean();
    } else {
      workers = [...memoryStore.workerProfiles];
    }

    // Filter relevant workers or all active workers
    let candidates = workers;
    if (service) {
      const matchTrade = workers.filter(w => (w.trade || '').toLowerCase().includes(service.toLowerCase()) || service.toLowerCase().includes((w.trade || '').toLowerCase()));
      if (matchTrade.length > 0) {
        candidates = matchTrade;
      }
    }

    // Compute explainable scores
    const scoredCandidates = candidates.map(worker => {
      const scoring = computeSmartMatchScore(worker, { service, requiredSkill, customerLocation, preferredTime });
      return {
        workerId: worker.id || worker._id,
        workerName: worker.name,
        trade: worker.trade,
        verified: true,
        rating: worker.rating || 4.8,
        jobsCompleted: worker.completedJobs || 42,
        distanceKm: scoring.distanceKm,
        availability: scoring.breakdown.availabilityLabel,
        estimatedPrice: worker.hourlyRate || 499,
        matchPercentage: scoring.matchPercentage,
        breakdown: scoring.breakdown
      };
    });

    // Sort by Match Percentage descending
    scoredCandidates.sort((a, b) => b.matchPercentage - a.matchPercentage);

    // Take top 3 as requested
    const topCandidates = scoredCandidates.slice(0, 3);

    res.status(200).json({
      success: true,
      algorithm: 'AI Smart Matching Prototype',
      notice: 'Prototype scoring based on explainable heuristics (Skill, Distance, Availability, Rating, Workload). Not a black-box AI model.',
      count: topCandidates.length,
      data: topCandidates
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Fair Workforce Allocation Prototype
 * @route   GET /api/ai/workforce-allocation
 * @access  Public
 */
const handleWorkforceAllocation = async (req, res, next) => {
  try {
    const { trade, area } = req.query;

    let workers = [];
    if (isMongoReady()) {
      workers = await WorkerProfile.find({}).lean();
    } else {
      workers = [...memoryStore.workerProfiles];
    }

    if (trade) {
      workers = workers.filter(w => (w.trade || '').toLowerCase().includes(trade.toLowerCase()));
    }

    const workerStats = evaluateWorkforceAllocation(workers);
    const zoneAllocations = getZoneWorkforceAllocations();

    res.status(200).json({
      success: true,
      algorithm: 'Prototype Fair Allocation Algorithm',
      notice: 'Avoids unfair concentration of jobs on nearest/monopoly workers. Equitably routes tasks across all qualified guild members.',
      data: {
        algorithmName: 'Prototype Fair Allocation Algorithm',
        disclaimer: 'SIH Prototype - Explainable heuristics avoiding unfair job concentration.',
        zoneAllocations,
        workerFairnessStats: workerStats
      }
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    AI Fair Price Assistant Suggestion
 * @route   POST /api/ai/price-suggestion
 * @access  Public
 */
const handlePriceSuggestion = async (req, res, next) => {
  try {
    const {
      serviceType = 'AC Service',
      jobComplexity = 'Standard',
      workerSkill = 'Certified Senior',
      location = 'Indiranagar, Bengaluru',
      customerBudget = 500
    } = req.body;

    const suggestion = calculateFairPriceSuggestion({
      serviceType,
      jobComplexity,
      workerSkill,
      location,
      customerBudget
    });

    res.status(200).json({
      success: true,
      label: 'AI-assisted price suggestion',
      notice: 'AI is an assistant, not an autonomous decision maker. Configurable demo pricing based on cooperative standard baseline rate cards.',
      data: suggestion
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  handleSmartMatch,
  handleWorkforceAllocation,
  handlePriceSuggestion
};
