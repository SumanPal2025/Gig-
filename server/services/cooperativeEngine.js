/**
 * Cooperative Gig Economy Engine
 * Enforces:
 * 1. Transparent 5% platform fee (vs corporate platforms charging 20-30%)
 * 2. 1% Welfare & Emergency fund allocation
 * 3. 94% Direct worker earnings
 * 4. Fair workload dispatching (routing jobs to workers below monthly caps)
 */

const calculateFeeBreakdown = (serviceAmount) => {
  const amount = Number(serviceAmount) || 0;
  const platformFee = Math.round(amount * 0.05); // 5% Cooperative Maintenance
  const welfareContribution = Math.round(amount * 0.01); // 1% Emergency/Welfare reserve
  const workerEarnings = amount - platformFee; // 95% total credited to worker (94% cash + 1% in co-op welfare)

  return {
    amount,
    platformFee,
    welfareContribution,
    workerEarnings,
    feePercentage: 5.0
  };
};

/**
 * Calculates a fair allocation priority score for a worker
 * Higher score = higher priority in dispatch queue
 */
const calculateFairAllocationScore = (worker) => {
  const currentJobs = worker.currentWorkload || 0;
  const monthlyCap = worker.monthlyCap || 35;
  const monthlyJobs = worker.monthlyJobCount || 0;
  const rating = worker.rating || 4.5;

  // Anti-burnout factor: prioritize workers who haven't reached their cap
  const capacityRatio = Math.max(0, 1 - (monthlyJobs / monthlyCap));
  const workloadPenalty = Math.max(0, 1 - (currentJobs / 5));

  // Score from 0 to 100
  const score = Math.round((capacityRatio * 50) + (workloadPenalty * 30) + ((rating / 5) * 20));
  return score;
};

module.exports = {
  calculateFeeBreakdown,
  calculateFairAllocationScore
};
