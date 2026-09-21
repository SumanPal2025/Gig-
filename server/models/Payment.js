const mongoose = require('mongoose');

const paymentSchema = new mongoose.Schema(
  {
    booking: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Booking',
      required: true
    },
    customer: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true
    },
    worker: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'WorkerProfile',
      required: true
    },
    amount: {
      type: Number,
      required: true
    },
    cooperativeFee: {
      type: Number,
      default: 0 // 5%
    },
    workerPayout: {
      type: Number,
      default: 0 // 94%
    },
    welfareAllocation: {
      type: Number,
      default: 0 // 1%
    },
    paymentMethod: {
      type: String,
      enum: ['upi', 'card', 'cash', 'coop_wallet'],
      default: 'upi'
    },
    status: {
      type: String,
      enum: ['pending', 'completed', 'failed', 'refunded'],
      default: 'completed'
    },
    transactionId: {
      type: String,
      default: () => `TXN-HMZ-${Date.now()}-${Math.floor(1000 + Math.random() * 9000)}`
    },
    paidAt: {
      type: Date,
      default: Date.now
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Payment', paymentSchema);
