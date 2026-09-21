const mongoose = require('mongoose');

const cooperativeSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      trim: true
    },
    code: {
      type: String,
      required: true,
      unique: true,
      uppercase: true
    },
    region: {
      type: String,
      required: true
    },
    establishedYear: {
      type: Number,
      default: 2024
    },
    totalMembers: {
      type: Number,
      default: 0
    },
    retainedSurplus: {
      type: Number,
      default: 0
    },
    emergencyFund: {
      type: Number,
      default: 500000
    },
    feePercentage: {
      type: Number,
      default: 5.0 // 5% cooperative platform fee
    },
    pincodes: [{
      type: String
    }],
    status: {
      type: String,
      enum: ['active', 'inactive'],
      default: 'active'
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Cooperative', cooperativeSchema);
