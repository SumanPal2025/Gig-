const { connectDB } = require('../config/db');
const { generateSeedData } = require('./seedData');
const {
  User,
  WorkerProfile,
  Cooperative,
  Service,
  Booking,
  Payment,
  Rating,
  Certification,
  Welfare,
  Notification,
  memoryStore,
  isMongoReady
} = require('../models');

const seedDatabase = async () => {
  console.log('[Seeder] Initializing HOMEZY database seed...');
  const data = await generateSeedData();

  // Populate memoryStore
  memoryStore.users = [...data.users];
  memoryStore.workerProfiles = [...data.workerProfiles];
  memoryStore.cooperatives = [data.cooperative];
  memoryStore.services = [...data.services];
  memoryStore.bookings = [...data.bookings];
  memoryStore.payments = [...data.payments];
  memoryStore.ratings = [...data.ratings];
  memoryStore.certifications = [...data.certifications];
  memoryStore.welfares = [...data.welfares];
  memoryStore.notifications = [...data.notifications];

  console.log(`[Seeder] Memory Store loaded:
    - ${memoryStore.users.length} Users (${data.users.filter(u => u.role === 'customer').length} customers, ${data.workerProfiles.length} workers, 1 admin)
    - ${memoryStore.services.length} Services (${data.services.map(s => s.name).join(', ')})
    - ${memoryStore.bookings.length} Bookings
    - ${memoryStore.payments.length} Payments
    - ${memoryStore.welfares.length} Worker Welfare Profiles`);

  // If MongoDB is connected, also persist to MongoDB collections
  if (isMongoReady()) {
    try {
      await Promise.all([
        User.deleteMany({}),
        WorkerProfile.deleteMany({}),
        Cooperative.deleteMany({}),
        Service.deleteMany({}),
        Booking.deleteMany({}),
        Payment.deleteMany({}),
        Rating.deleteMany({}),
        Certification.deleteMany({}),
        Welfare.deleteMany({}),
        Notification.deleteMany({})
      ]);

      await Cooperative.create(data.cooperative);
      await Service.insertMany(data.services);
      await User.insertMany(data.users);
      await WorkerProfile.insertMany(data.workerProfiles);
      await Certification.insertMany(data.certifications);
      await Welfare.insertMany(data.welfares);
      await Booking.insertMany(data.bookings);
      await Payment.insertMany(data.payments);
      await Rating.insertMany(data.ratings);
      await Notification.insertMany(data.notifications);

      console.log('[Seeder] Successfully populated MongoDB collections!');
    } catch (err) {
      console.warn('[Seeder] MongoDB persist skipped / error:', err.message);
    }
  }

  return true;
};

// If run directly: `node utils/seeder.js`
if (require.main === module) {
  require('dotenv').config();
  (async () => {
    await connectDB();
    await seedDatabase();
    process.exit(0);
  })();
}

module.exports = { seedDatabase };
