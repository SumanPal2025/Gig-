const mongoose = require('mongoose');

const bookingSchema = new mongoose.Schema(
  {
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
    service: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Service',
      required: true
    },
    scheduledDate: {
      type: String,
      required: true
    },
    timeSlot: {
      type: String,
      required: true
    },
    status: {
      type: String,
      enum: ['requested', 'accepted', 'in_progress', 'completed', 'cancelled'],
      default: 'requested'
    },
    amount: {
      type: Number,
      required: true
    },
    platformFee: {
      type: Number,
      default: 0 // 5%
    },
    workerEarnings: {
      type: Number,
      default: 0 // 94%
    },
    welfareContribution: {
      type: Number,
      default: 0 // 1%
    },
    customerAddress: {
      type: String,
      required: true
    },
    customerPhone: {
      type: String,
      default: ''
    },
    notes: {
      type: String,
      default: ''
    },
    payment: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Payment'
    },
    rating: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Rating'
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Booking', bookingSchema);
