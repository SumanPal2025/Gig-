const mongoose = require('mongoose');

let isConnected = false;

const connectDB = async () => {
  const uri = process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/homezy';
  try {
    const conn = await mongoose.connect(uri, {
      serverSelectionTimeoutMS: 2000,
    });
    isConnected = true;
    console.log(`[Database] MongoDB Connected: ${conn.connection.host}`);
    return conn;
  } catch (error) {
    console.warn(`[Database] MongoDB not reachable at ${uri} (${error.message}). Running in High-Speed Memory Storage mode.`);
    isConnected = false;
    return null;
  }
};

const getStatus = () => ({
  connected: isConnected,
  mode: isConnected ? 'mongodb' : 'memory'
});

module.exports = { connectDB, getStatus };
