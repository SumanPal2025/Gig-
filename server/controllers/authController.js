const bcrypt = require('bcryptjs');
const { User, WorkerProfile, Welfare, memoryStore, generateId, isMongoReady } = require('../models');
const { generateToken } = require('../services/tokenService');
const { validateEmail } = require('../middleware/validate');

/**
 * @desc    Register a new user (Customer or Worker)
 * @route   POST /api/auth/register
 * @access  Public
 */
const register = async (req, res, next) => {
  try {
    const { name, email, phone, password, role = 'customer', trade, address } = req.body;

    if (!name || !email || !phone || !password) {
      return res.status(400).json({
        success: false,
        message: 'Please provide name, email, phone, and password'
      });
    }

    if (!validateEmail(email)) {
      return res.status(400).json({
        success: false,
        message: 'Please provide a valid email address'
      });
    }

    if (password.length < 6) {
      return res.status(400).json({
        success: false,
        message: 'Password must be at least 6 characters long'
      });
    }

    const normalizedEmail = email.toLowerCase().trim();

    // Check existing
    let existing;
    if (isMongoReady()) {
      existing = await User.findOne({ email: normalizedEmail });
    } else {
      existing = memoryStore.users.find(u => u.email.toLowerCase() === normalizedEmail);
    }

    if (existing) {
      return res.status(400).json({
        success: false,
        message: 'A user with this email already exists'
      });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const userId = generateId();

    const newUser = {
      _id: userId,
      name: name.trim(),
      email: normalizedEmail,
      phone: phone.trim(),
      password: hashedPassword,
      role: ['customer', 'worker', 'admin'].includes(role) ? role : 'customer',
      status: 'active',
      avatar: `https://images.unsplash.com/photo-1535713875002?w=150`,
      createdAt: new Date(),
      updatedAt: new Date()
    };

    if (isMongoReady()) {
      await User.create(newUser);
    } else {
      memoryStore.users.push(newUser);
    }

    let workerProfileData = null;
    // If worker role, initialize WorkerProfile and Welfare
    if (newUser.role === 'worker') {
      const profileId = generateId();
      const welfareId = generateId();

      const newWelfare = {
        _id: welfareId,
        worker: profileId,
        insuranceActive: true,
        insurancePolicyNumber: `COOP-INS-${Date.now()}`,
        insuranceCoverage: 500000,
        emergencyFundBalance: 15000,
        pensionAccrued: 5000,
        patronageBonusYTD: 1200,
        healthCheckupStatus: 'scheduled',
        claims: []
      };

      const newProfile = {
        _id: profileId,
        user: userId,
        name: newUser.name,
        email: newUser.email,
        phone: newUser.phone,
        trade: trade || 'Electrician',
        skills: [trade || 'Electrician', 'General Maintenance'],
        bio: 'Newly registered cooperative member service professional',
        rating: 5.0,
        totalReviews: 0,
        completedJobs: 0,
        availability: 'available',
        currentWorkload: 0,
        monthlyJobCount: 0,
        monthlyCap: 35,
        fairAllocationScore: 100,
        totalEarnings: 0,
        patronageDividendsEarned: 0,
        verificationStatus: 'verified',
        location: {
          address: address || 'Bangalore, Karnataka',
          city: 'Bangalore',
          state: 'Karnataka',
          lat: 12.9716,
          lng: 77.5946
        },
        certifications: [],
        welfare: welfareId
      };

      if (isMongoReady()) {
        await Welfare.create(newWelfare);
        await WorkerProfile.create(newProfile);
      } else {
        memoryStore.welfares.push(newWelfare);
        memoryStore.workerProfiles.push(newProfile);
      }
      workerProfileData = newProfile;
    }

    const token = generateToken({
      id: newUser._id,
      email: newUser.email,
      role: newUser.role,
      name: newUser.name
    });

    res.status(201).json({
      success: true,
      message: 'User registered successfully',
      token,
      user: {
        id: newUser._id,
        name: newUser.name,
        email: newUser.email,
        phone: newUser.phone,
        role: newUser.role,
        avatar: newUser.avatar,
        workerProfile: workerProfileData
      }
    });
  } catch (error) {
    next(error);
  }
};

/**
 * @desc    Login user & get token
 * @route   POST /api/auth/login
 * @access  Public
 */
const login = async (req, res, next) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({
        success: false,
        message: 'Please provide email and password'
      });
    }

    const normalizedEmail = email.toLowerCase().trim();

    let user;
    if (isMongoReady()) {
      user = await User.findOne({ email: normalizedEmail }).select('+password');
    } else {
      user = memoryStore.users.find(u => u.email.toLowerCase() === normalizedEmail);
    }

    if (!user) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password'
      });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password'
      });
    }

    // If worker, fetch worker profile
    let workerProfile = null;
    if (user.role === 'worker') {
      if (isMongoReady()) {
        workerProfile = await WorkerProfile.findOne({ user: user._id });
      } else {
        workerProfile = memoryStore.workerProfiles.find(
          wp => String(wp.user) === String(user._id) || wp.email === user.email
        );
      }
    }

    const token = generateToken({
      id: user._id,
      email: user.email,
      role: user.role,
      name: user.name
    });

    res.status(200).json({
      success: true,
      message: 'Login successful',
      token,
      user: {
        id: user._id,
        name: user.name,
        email: user.email,
        phone: user.phone,
        role: user.role,
        avatar: user.avatar,
        workerProfile
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  register,
  login
};
