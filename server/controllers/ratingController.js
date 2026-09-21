const { Rating, WorkerProfile, Booking, memoryStore, generateId, isMongoReady } = require('../models');

/**
 * @desc    Submit a rating and review for completed service
 * @route   POST /api/ratings
 * @access  Private (Customer)
 */
const createRating = async (req, res, next) => {
  try {
    const { bookingId, workerId, rating, comment, workQualityRating, punctualityRating, politenessRating } = req.body;

    if (!bookingId || !workerId || !rating) {
      return res.status(400).json({
        success: false,
        message: 'Missing required fields: bookingId, workerId, and rating (1-5)'
      });
    }

    const ratingNum = Number(rating);
    if (ratingNum < 1 || ratingNum > 5) {
      return res.status(400).json({
        success: false,
        message: 'Rating must be between 1 and 5'
      });
    }

    const ratingId = generateId();
    const customerId = req.user ? req.user._id : 'usr_cust_100';

    const newRating = {
      _id: ratingId,
      booking: bookingId,
      customer: customerId,
      worker: workerId,
      rating: ratingNum,
      comment: comment || '',
      workQualityRating: workQualityRating || ratingNum,
      punctualityRating: punctualityRating || ratingNum,
      politenessRating: politenessRating || ratingNum,
      createdAt: new Date()
    };

    if (isMongoReady()) {
      await Rating.create(newRating);
      await Booking.findByIdAndUpdate(bookingId, { rating: ratingId });
    } else {
      memoryStore.ratings.push(newRating);
      const b = memoryStore.bookings.find(item => String(item._id) === String(bookingId));
      if (b) b.rating = ratingId;
    }

    // Recalculate Worker average rating
    let workerRatings = [];
    if (isMongoReady()) {
      workerRatings = await Rating.find({ worker: workerId });
      const avg = workerRatings.reduce((acc, curr) => acc + curr.rating, 0) / workerRatings.length;
      await WorkerProfile.findByIdAndUpdate(workerId, {
        rating: Math.round(avg * 10) / 10,
        totalReviews: workerRatings.length
      });
    } else {
      workerRatings = memoryStore.ratings.filter(r => String(r.worker) === String(workerId));
      const avg = workerRatings.reduce((acc, curr) => acc + curr.rating, 0) / (workerRatings.length || 1);
      const w = memoryStore.workerProfiles.find(item => String(item._id) === String(workerId));
      if (w) {
        w.rating = Math.round(avg * 10) / 10;
        w.totalReviews = workerRatings.length;
      }
    }

    res.status(201).json({
      success: true,
      message: 'Rating submitted successfully',
      data: newRating
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  createRating
};
