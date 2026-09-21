const mongoose = require('mongoose');

const ratingSchema = new mongoose.Schema(
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
    rating: {
      type: Number,
      required: true,
      min: 1,
      max: 5
    },
    comment: {
      type: String,
      default: ''
    },
    workQualityRating: {
      type: Number,
      min: 1,
      max: 5,
      default: 5
    },
    punctualityRating: {
      type: Number,
      min: 1,
      max: 5,
      default: 5
    },
    politenessRating: {
      type: Number,
      min: 1,
      max: 5,
      default: 5
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Rating', ratingSchema);
