const express = require('express');
const router = express.Router();
const { createPayment, getPaymentById } = require('../controllers/paymentController');
const { protect } = require('../middleware/auth');

router.post('/', protect, createPayment);
router.get('/:id', protect, getPaymentById);

module.exports = router;
