const express = require('express');
const router = express.Router();
const { getDashboardStats, getAdminWorkers, getAnalytics } = require('../controllers/adminController');
const { protect } = require('../middleware/auth');
const { authorize } = require('../middleware/role');

// All admin routes require authentication and 'admin' role
router.use(protect);
router.use(authorize('admin'));

router.get('/dashboard', getDashboardStats);
router.get('/workers', getAdminWorkers);
router.get('/analytics', getAnalytics);

module.exports = router;
