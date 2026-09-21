const mongoose = require('mongoose');

const workerProfileSchema = new mongoose.Schema(
  {
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true,
      unique: true
    },
    cooperative: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Cooperative'
    },
    trade: {
      type: String,
      required: true,
      enum: ['Electrician', 'Plumber', 'AC Service', 'Carpenter', 'Painter', 'House Cleaning', 'Appliance Repair']
    },
    skills: [{
      type: String
    }],
    experienceYears: {
      type: Number,
      default: 3
    },
    bio: {
      type: String,
      default: 'Verified Cooperative Service Professional'
    },
    rating: {
      type: Number,
      default: 4.8,
      min: 1.0,
      max: 5.0
    },
    totalReviews: {
      type: Number,
      default: 0
    },
    completedJobs: {
      type: Number,
      default: 0
    },
    availability: {
      type: String,
      enum: ['available', 'busy', 'offline'],
      default: 'available'
    },
    currentWorkload: {
      type: Number,
      default: 0 // Active jobs this week
    },
    monthlyJobCount: {
      type: Number,
      default: 0
    },
    monthlyCap: {
      type: Number,
      default: 35 // Anti-exploitation cap for fair distribution
    },
    fairAllocationScore: {
      type: Number,
      default: 95 // Cooperative routing engine score
    },
    totalEarnings: {
      type: Number,
      default: 0
    },
    patronageDividendsEarned: {
      type: Number,
      default: 0
    },
    verificationStatus: {
      type: String,
      enum: ['verified', 'pending', 'rejected'],
      default: 'verified'
    },
    location: {
      address: { type: String, default: 'Koramangala, Bangalore' },
      city: { type: String, default: 'Bangalore' },
      state: { type: String, default: 'Karnataka' },
      pincode: { type: String, default: '560034' },
      lat: { type: Number, default: 12.9352 },
      lng: { type: Number, default: 77.6245 }
    },
    certifications: [{
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Certification'
    }],
    welfare: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Welfare'
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('WorkerProfile', workerProfileSchema);
