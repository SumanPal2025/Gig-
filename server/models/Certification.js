const mongoose = require('mongoose');

const certificationSchema = new mongoose.Schema(
  {
    worker: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'WorkerProfile',
      required: true
    },
    title: {
      type: String,
      required: true
    },
    issuingBody: {
      type: String,
      required: true // e.g. NSDC, Govt ITI, Skill India
    },
    issueDate: {
      type: Date,
      default: Date.now
    },
    expiryDate: {
      type: Date
    },
    credentialId: {
      type: String
    },
    verifiedStatus: {
      type: String,
      enum: ['verified', 'pending', 'rejected'],
      default: 'verified'
    },
    documentUrl: {
      type: String,
      default: ''
    }
  },
  { timestamps: true }
);

module.exports = mongoose.model('Certification', certificationSchema);
