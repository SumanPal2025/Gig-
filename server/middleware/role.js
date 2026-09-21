/**
 * Role-based access control middleware
 * Checks if user's role is permitted to perform the action
 */
const authorize = (...roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: 'Not authenticated'
      });
    }

    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        message: `Role '${req.user.role}' is not authorized to access this resource. Required role(s): ${roles.join(', ')}`
      });
    }

    next();
  };
};

module.exports = { authorize };
