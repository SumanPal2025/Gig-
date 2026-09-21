const express = require('express');
const router = express.Router();
const { getWorkerWelfare, updateWorkerWelfare } = require('../controllers/welfareController');
const { protect } = require('../middleware/auth');

router.get('/:workerId', protect, getWorkerWelfare);
router.put('/:workerId', protect, updateWorkerWelfare);

module.exports = router;
