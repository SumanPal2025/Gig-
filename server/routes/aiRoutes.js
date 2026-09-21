const express = require('express');
const router = express.Router();
const { handleSmartMatch, handleWorkforceAllocation, handlePriceSuggestion } = require('../controllers/aiController');

// POST /api/ai/smart-match
router.post('/smart-match', handleSmartMatch);

// GET /api/ai/workforce-allocation
router.get('/workforce-allocation', handleWorkforceAllocation);

// POST /api/ai/price-suggestion
router.post('/price-suggestion', handlePriceSuggestion);

module.exports = router;
