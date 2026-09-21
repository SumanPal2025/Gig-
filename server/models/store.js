const mongoose = require('mongoose');
const { getStatus } = require('../config/db');

// In-memory backing collections for fallback mode
const memoryStore = {
  users: [],
  workerProfiles: [],
  cooperatives: [],
  services: [],
  bookings: [],
  payments: [],
  ratings: [],
  certifications: [],
  welfares: [],
  notifications: []
};

let idCounter = 1000;
const generateId = () => `mem_${Date.now()}_${++idCounter}`;

// Helper to check if Mongoose connection is ready
const isMongoReady = () => {
  return mongoose.connection && mongoose.connection.readyState === 1;
};

module.exports = {
  memoryStore,
  generateId,
  isMongoReady
};
