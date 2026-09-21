const express = require('express');
const router = express.Router();
const { getWorkers, getWorkerById, updateWorker, verifyWorker } = require('../controllers/workerController');
const { protect } = require('../middleware/auth');
const { authorize } = require('../middleware/role');

router.get('/', getWorkers);
router.get('/:id', getWorkerById);
router.put('/:id', protect, updateWorker);
router.post('/:id/verify', protect, authorize('admin'), verifyWorker);

module.exports = router;
