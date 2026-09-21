const mongoose = require('mongoose');

const serviceSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      unique: true,
      trim: true
    },
    category: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    },
    basePrice: {
      type: Number,
      required: true
    },
    estimatedDurationMinutes: {
      type: Number,
      default: 60
    },
    popular: {
      type: Boolean,
      default: false
    },
    icon: {
      type: String,
      default: 'service'
    },
    includedTasks: [{
      type: String
    }],
    standardCoopFeePercentage: {
      type: Number,
      default: 5.0
    },
    active: {
      type: Boolean,
      default: true
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Service', serviceSchema);
