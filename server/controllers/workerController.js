const { WorkerProfile, Certification, Welfare, memoryStore, isMongoReady } = require('../models');
const { calculateFairAllocationScore } = require('../services/cooperativeEngine');

/**
 * @desc    Get all workers (with filtering by trade, availability, search)
 * @route   GET /api/workers
 * @access  Public
 */
const getWorkers = async (req, res, next) => {
  try {
    const { trade, availability, search, limit = 50 } = req.query;

    let workers = [];
    if (isMongoReady()) {
      const query = {};
      if (trade) query.trade = new RegExp(trade, 'i');
      if (availability) query.availability = availability;
      if (search) {
        query.$or = [
          { name: new RegExp(search, 'i') },
          { skills: new RegExp(search, 'i') },
          { trade: new RegExp(search, 'i') }
        ];
      }
      workers = await WorkerProfile.find(query).limit(Number(limit));
    } else {
      workers = [...memoryStore.workerProfiles];

      if (trade) {
        workers = workers.filter(w => w.trade && w.trade.toLowerCase() === trade.toLowerCase());
      }
      if (availability) {
        workers = workers.filter(w => w.availability === availability);
      }
      if (search) {
        const q = search.toLowerCase();
        workers = workers.filter(w =>
          (w.name && w.name.toLowerCase().includes(q)) ||
          (w.trade && w.trade.toLowerCase().includes(q)) ||
          (w.skills && w.skills.some(s => s.toLowerCase().includes(q)))
        );
      }
      workers = workers.slice(0, Number(limit));
    }

    // Attach fair allocation scores
    const enriched = workers.map(w => {
      const obj = w.toObject ? w.toObject() : { ...w };
      obj.fairAllocationScore = calculateFairAllocationScore(obj);
      return obj;
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
 * @desc    Get single worker by ID with certifications and welfare info
 * @route   GET /api/workers/:id
 * @access  Public
 */
const getWorkerById = async (req, res, next) => {
  try {
    const { id } = req.params;

    let worker = null;
    let certs = [];
    let welfare = null;

    if (isMongoReady()) {
      worker = await WorkerProfile.findById(id);
      if (worker) {
        certs = await Certification.find({ worker: worker._id });
        welfare = await Welfare.findOne({ worker: worker._id });
      }
    } else {
      worker = memoryStore.workerProfiles.find(w => String(w._id) === String(id) || String(w.user) === String(id));
      if (worker) {
        certs = memoryStore.certifications.filter(c => String(c.worker) === String(worker._id));
        welfare = memoryStore.welfares.find(wf => String(wf.worker) === String(worker._id));
      }
    }

    if (!worker) {
      return res.status(404).json({
        success: false,
        message: `Worker not found with ID ${id}`
      });
    }

    const result = worker.toObject ? worker.toObject() : { ...worker };
    result.certificationsData = certs;
    result.welfareData = welfare;
    result.fairAllocationScore = calculateFairAllocationScore(result);

    res.status(200).json({
      success: true,
      data: result
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Update worker profile
 * @route   PUT /api/workers/:id
 * @access  Private (Worker themselves or Admin)
 */
const updateWorker = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { availability, skills, bio, trade, location } = req.body;

    let worker = null;
    if (isMongoReady()) {
      worker = await WorkerProfile.findById(id);
      if (!worker) {
        return res.status(404).json({ success: false, message: 'Worker not found' });
      }

      if (availability) worker.availability = availability;
      if (skills) worker.skills = skills;
      if (bio) worker.bio = bio;
      if (trade) worker.trade = trade;
      if (location) worker.location = { ...worker.location, ...location };

      await worker.save();
    } else {
      const index = memoryStore.workerProfiles.findIndex(w => String(w._id) === String(id) || String(w.user) === String(id));
      if (index === -1) {
        return res.status(404).json({ success: false, message: 'Worker not found' });
      }

      const existing = memoryStore.workerProfiles[index];
      memoryStore.workerProfiles[index] = {
        ...existing,
        availability: availability || existing.availability,
        skills: skills || existing.skills,
        bio: bio || existing.bio,
        trade: trade || existing.trade,
        location: location ? { ...existing.location, ...location } : existing.location,
        updatedAt: new Date()
      };
      worker = memoryStore.workerProfiles[index];
    }

    res.status(200).json({
      success: true,
      message: 'Worker profile updated successfully',
      data: worker
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Verify a worker (Co-op Admin action)
 * @route   POST /api/workers/:id/verify
 * @access  Private (Admin only)
 */
const verifyWorker = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { status = 'verified' } = req.body;

    let worker = null;
    if (isMongoReady()) {
      worker = await WorkerProfile.findByIdAndUpdate(
        id,
        { verificationStatus: status },
        { new: true }
      );
    } else {
      const item = memoryStore.workerProfiles.find(w => String(w._id) === String(id));
      if (item) {
        item.verificationStatus = status;
        worker = item;
      }
    }

    if (!worker) {
      return res.status(404).json({ success: false, message: 'Worker not found' });
    }

    res.status(200).json({
      success: true,
      message: `Worker verification status updated to '${status}'`,
      data: worker
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getWorkers,
  getWorkerById,
  updateWorker,
  verifyWorker
};
