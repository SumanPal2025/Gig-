const mongoose = require('mongoose');

const welfareSchema = new mongoose.Schema(
  {
    worker: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'WorkerProfile',
      required: true,
      unique: true
    },
    insuranceActive: {
      type: Boolean,
      default: true
    },
    insurancePolicyNumber: {
      type: String,
      default: 'COOP-INS-2026-9081'
    },
    insuranceCoverage: {
      type: Number,
      default: 500000 // 5 Lakh accident + health cover
    },
    emergencyFundBalance: {
      type: Number,
      default: 15000
    },
    pensionAccrued: {
      type: Number,
      default: 8200
    },
    patronageBonusYTD: {
      type: Number,
      default: 3450
    },
    healthCheckupStatus: {
      type: String,
      enum: ['completed', 'due', 'scheduled'],
      default: 'completed'
    },
    lastCheckupDate: {
      type: Date,
      default: Date.now
    },
    claims: [{
      title: String,
      amount: Number,
      status: {
        type: String,
        enum: ['approved', 'pending', 'rejected'],
        default: 'approved'
      },
      disbursedAt: Date
    }]
  },
  { timestamps: true }
);

module.exports = mongoose.model('Welfare', welfareSchema);
