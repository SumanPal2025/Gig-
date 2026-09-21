/**
 * AI FAIR PRICE ASSISTANT SERVICE
 * Phase 6: Explainable, transparent price recommendation and negotiation engine
 * Notice: AI is an assistant, not an autonomous decision maker.
 */

const calculateFairPriceSuggestion = ({
  serviceType = 'AC Service',
  jobComplexity = 'Standard',
  workerSkill = 'Certified Senior',
  location = 'Indiranagar, Bengaluru',
  customerBudget = 500
}) => {
  const budget = Math.max(100, Number(customerBudget) || 500);

  // 1. Service Base Rates (Cooperative standard baseline rate cards)
  let baseRate = 425;
  const sType = (serviceType || '').toLowerCase();
  if (sType.includes('ac') || sType.includes('air conditioner')) {
    baseRate = 425;
  } else if (sType.includes('electr')) {
    baseRate = 340;
  } else if (sType.includes('plumb')) {
    baseRate = 300;
  } else if (sType.includes('appliance') || sType.includes('washing') || sType.includes('refriger')) {
    baseRate = 380;
  } else {
    baseRate = 350;
  }

  // 2. Job Complexity Multiplier
  let complexityMultiplier = 1.12;
  const comp = (jobComplexity || '').toLowerCase();
  if (comp.includes('minor') || comp.includes('quick')) {
    complexityMultiplier = 1.0;
  } else if (comp.includes('complex') || comp.includes('major') || comp.includes('heavy')) {
    complexityMultiplier = 1.45;
  } else if (comp.includes('moderate')) {
    complexityMultiplier = 1.25;
  } else {
    // Standard
    complexityMultiplier = 1.12;
  }

  // 3. Worker Skill Multiplier
  let skillMultiplier = 1.08;
  const skill = (workerSkill || '').toLowerCase();
  if (skill.includes('master') || skill.includes('expert') || skill.includes('lead')) {
    skillMultiplier = 1.2;
  } else if (skill.includes('senior') || skill.includes('certified')) {
    skillMultiplier = 1.08;
  } else {
    skillMultiplier = 1.0;
  }

  // 4. Location Factor
  let locationAdjustment = 25;
  const loc = (location || '').toLowerCase();
  if (loc.includes('indiranagar') || loc.includes('koramangala') || loc.includes('salt lake') || loc.includes('whitefield')) {
    locationAdjustment = 35;
  } else if (loc.includes('howrah') || loc.includes('new town') || loc.includes('bellandur')) {
    locationAdjustment = 25;
  } else {
    locationAdjustment = 20;
  }

  // Calculate Median Fair Center
  const rawFair = (baseRate * complexityMultiplier * skillMultiplier) + locationAdjustment;
  const fairCenter = Math.round(rawFair / 25) * 25;

  let suggestedMin = Math.round((fairCenter * 0.95) / 25) * 25;
  let suggestedMax = Math.round((fairCenter * 1.05) / 25) * 25;

  if (suggestedMax <= suggestedMin) {
    suggestedMax = suggestedMin + 50;
  } else if (suggestedMax - suggestedMin < 50) {
    suggestedMax = suggestedMin + 50;
  }

  // Tailored AI Explanation based on user's entered offer
  let explanation = '';
  let startingOffer = suggestedMin;

  if (budget < suggestedMin) {
    const diff = suggestedMin - budget;
    if (diff <= 50) {
      explanation = `Your offer is slightly below the suggested range.\n₹${suggestedMin} may be a reasonable starting offer.`;
      startingOffer = suggestedMin;
    } else {
      explanation = `Your offer of ₹${budget} is noticeably below the suggested range (₹${suggestedMin}–₹${suggestedMax}).\n₹${suggestedMin} is recommended as an equitable starting proposal.`;
      startingOffer = suggestedMin;
    }
  } else if (budget >= suggestedMin && budget <= suggestedMax) {
    explanation = `Your offer of ₹${budget} fits squarely within the recommended range (₹${suggestedMin}–₹${suggestedMax}) and is likely to be accepted promptly by verified craftspeople.`;
    startingOffer = budget;
  } else {
    explanation = `Your offer of ₹${budget} is comfortably above the recommended range (₹${suggestedMin}–₹${suggestedMax}). It represents a fair premium for express scheduling.`;
    startingOffer = suggestedMax;
  }

  return {
    customerBudget: budget,
    suggestedMin,
    suggestedMax,
    suggestedRangeText: `₹${suggestedMin}–₹${suggestedMax}`,
    recommendedStartingOffer: startingOffer,
    explanation,
    disclaimer: 'AI-assisted price suggestion. AI is an assistant, not an autonomous decision maker.',
    serviceType,
    jobComplexity,
    workerSkill,
    location,
    baseRate,
    factors: {
      baseRate,
      complexityFactor: `${Math.round((complexityMultiplier - 1) * 100)}% adjustment for ${jobComplexity}`,
      skillFactor: `${Math.round((skillMultiplier - 1) * 100)}% adjustment for ${workerSkill}`,
      locationAdjustment: `+₹${locationAdjustment} transit allowance`
    }
  };
};

module.exports = {
  calculateFairPriceSuggestion
};
