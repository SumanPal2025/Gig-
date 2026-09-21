require('dotenv').config();
const express = require('express');
const cors = require('cors');
const { connectDB, getStatus } = require('./config/db');
const { seedDatabase } = require('./utils/seeder');
const errorHandler = require('./middleware/errorHandler');

// Route files
const authRoutes = require('./routes/authRoutes');
const workerRoutes = require('./routes/workerRoutes');
const serviceRoutes = require('./routes/serviceRoutes');
const bookingRoutes = require('./routes/bookingRoutes');
const paymentRoutes = require('./routes/paymentRoutes');
const ratingRoutes = require('./routes/ratingRoutes');
const welfareRoutes = require('./routes/welfareRoutes');
const adminRoutes = require('./routes/adminRoutes');
const aiRoutes = require('./routes/aiRoutes');

const app = express();

// Body parser
app.use(express.json());

// Enable CORS
app.use(cors());

// System Health Check
app.get('/api/health', (req, res) => {
  const dbStatus = getStatus();
  res.status(200).json({
    status: 'online',
    service: 'HOMEZY Backend API',
    version: '1.0.0',
    timestamp: new Date(),
    database: dbStatus
  });
});

// Mount routers
app.use('/api/auth', authRoutes);
app.use('/api/workers', workerRoutes);
app.use('/api/services', serviceRoutes);
app.use('/api/bookings', bookingRoutes);
app.use('/api/payments', paymentRoutes);
app.use('/api/ratings', ratingRoutes);
app.use('/api/welfare', welfareRoutes);
app.use('/api/admin', adminRoutes);
app.use('/api/ai', aiRoutes);

// Error handler middleware
app.use(errorHandler);

const PORT = process.env.BACKEND_PORT || 5001;

let serverInstance = null;

const startServer = async () => {
  await connectDB();
  await seedDatabase();

  return new Promise((resolve) => {
    serverInstance = app.listen(PORT, () => {
      console.log(`[HOMEZY Server] Running in ${process.env.NODE_ENV || 'development'} mode on port ${PORT}`);
      resolve(serverInstance);
    });
  });
};

if (require.main === module) {
  startServer();
}

module.exports = { app, startServer };
