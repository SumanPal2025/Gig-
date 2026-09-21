const bcrypt = require('bcryptjs');

const generateSeedData = async () => {
  const defaultPassword = await bcrypt.hash('Homezy@123', 10);

  const cooperative = {
    _id: 'coop_ka_001',
    name: 'Bangalore Urban Cooperative Services Federation',
    code: 'KA-COOP-BLR',
    region: 'Bangalore Metropolitan Region',
    establishedYear: 2024,
    totalMembers: 15,
    retainedSurplus: 145000,
    emergencyFund: 750000,
    feePercentage: 5.0,
    pincodes: ['560001', '560034', '560038', '560102', '560041', '560066'],
    status: 'active'
  };

  const services = [
    {
      _id: 'srv_ac_001',
      name: 'AC Service',
      category: 'Cooling & Appliances',
      description: 'Comprehensive multi-point air conditioner deep cleaning, gas pressure inspection, coil treatment, and filter sanitization.',
      basePrice: 599,
      estimatedDurationMinutes: 60,
      popular: true,
      icon: 'ac_unit',
      includedTasks: [
        'Foam jet indoor coil deep cleaning',
        'Outdoor unit high-pressure wash',
        'Coolant gas pressure & leak test',
        'Thermostat temperature calibration'
      ],
      standardCoopFeePercentage: 5.0,
      active: true
    },
    {
      _id: 'srv_elec_002',
      name: 'Electrician',
      category: 'Electrical & Power',
      description: 'Certified electrical repair, switchboard installation, MCB tripping resolution, inverter wiring, and appliance circuit inspection.',
      basePrice: 249,
      estimatedDurationMinutes: 45,
      popular: true,
      icon: 'bolt',
      includedTasks: [
        'Short-circuit and earthing diagnosis',
        'Modular switchboard and socket fixes',
        'Ceiling fan and LED fixture installation',
        'MCB trip check and load balancing'
      ],
      standardCoopFeePercentage: 5.0,
      active: true
    },
    {
      _id: 'srv_plumb_003',
      name: 'Plumber',
      category: 'Plumbing & Water',
      description: 'Quick resolution for leaking pipes, tap repairs, bathroom fittings, flush tanks, sink blockages, and water motor connections.',
      basePrice: 299,
      estimatedDurationMinutes: 50,
      popular: true,
      icon: 'plumbing',
      includedTasks: [
        'Concealed pipeline leak detection',
        'Tap, angle valve, and jet spray replacement',
        'Drain clogs removal and sewer trap clearing',
        'Water tank float valve calibration'
      ],
      standardCoopFeePercentage: 5.0,
      active: true
    }
  ];

  // 10 Customers
  const customerRaw = [
    { name: 'Priya Sundaram', email: 'priya.s@example.com', phone: '+91 98450 11001', address: '124, 4th Cross, Koramangala 4th Block, Bangalore' },
    { name: 'Amitabh Sen', email: 'amitabh.sen@example.com', phone: '+91 98450 11002', address: '88, 100 Feet Rd, Indiranagar, Bangalore' },
    { name: 'Sunita Reddy', email: 'sunita.reddy@example.com', phone: '+91 98450 11003', address: '502, Prestige Palms, Whitefield, Bangalore' },
    { name: 'Kavita Menon', email: 'kavita.m@example.com', phone: '+91 98450 11004', address: '23, 14th Main, HSR Layout Sector 3, Bangalore' },
    { name: 'Vikram Joshi', email: 'vikram.j@example.com', phone: '+91 98450 11005', address: '71, 9th Cross, Jayanagar 4th Block, Bangalore' },
    { name: 'Ananya Deshmukh', email: 'ananya.d@example.com', phone: '+91 98450 11006', address: '304, Green Glen Layout, Bellandur, Bangalore' },
    { name: 'Rohan Mehra', email: 'rohan.mehra@example.com', phone: '+91 98450 11007', address: '12, CMH Road, Ulsoor, Bangalore' },
    { name: 'Deepa Hegde', email: 'deepa.h@example.com', phone: '+91 98450 11008', address: '45, 5th Main, Malleshwaram, Bangalore' },
    { name: 'Naveen Nair', email: 'naveen.n@example.com', phone: '+91 98450 11009', address: '18, Bannerghatta Main Rd, BTM 2nd Stage, Bangalore' },
    { name: 'Sneha Kulkarni', email: 'sneha.k@example.com', phone: '+91 98450 11010', address: '99, 17th Cross, JP Nagar Phase 2, Bangalore' }
  ];

  // 15 Workers
  const workerRaw = [
    {
      name: 'Rahul Sharma',
      email: 'rahul.electrician@homezy.demo',
      phone: '+91 98230 20001',
      trade: 'Electrician',
      location: 'Koramangala, Bangalore',
      skills: ['Wiring', 'Inverter Setup', 'MCB Diagnostics', 'Appliance Circuits'],
      rating: 4.9,
      reviews: 142,
      completedJobs: 138,
      workload: 2,
      earnings: 48500,
      cert: 'Govt ITI Certified Electrician (Grade A)',
      certBody: 'Karnataka Vocational Training Council'
    },
    {
      name: 'Suresh Kumar',
      email: 'suresh.ac@homezy.demo',
      phone: '+91 98230 20002',
      trade: 'AC Service',
      location: 'HSR Layout, Bangalore',
      skills: ['HVAC Gas Charging', 'Split AC Maintenance', 'Ductless Inverter AC'],
      rating: 4.8,
      reviews: 98,
      completedJobs: 95,
      workload: 1,
      earnings: 54200,
      cert: 'NSDC Certified Refrigeration & Air Conditioning Specialist',
      certBody: 'National Skill Development Corporation'
    },
    {
      name: 'Manjunath Gowda',
      email: 'manju.plumber@homezy.demo',
      phone: '+91 98230 20003',
      trade: 'Plumber',
      location: 'Jayanagar, Bangalore',
      skills: ['Leak Detection', 'PPR Pipe Welding', 'Sanitary Fitting', 'Pump Installation'],
      rating: 4.9,
      reviews: 164,
      completedJobs: 160,
      workload: 3,
      earnings: 62100,
      cert: 'Govt ITI Master Plumber License',
      certBody: 'Directorate of Technical Education'
    },
    {
      name: 'Karthik Raja',
      email: 'karthik.electrician@homezy.demo',
      phone: '+91 98230 20004',
      trade: 'Electrician',
      location: 'Indiranagar, Bangalore',
      skills: ['Home Automation', 'Smart Switches', 'Three-Phase Wiring'],
      rating: 4.7,
      reviews: 82,
      completedJobs: 80,
      workload: 1,
      earnings: 39400,
      cert: 'Skill India Certified Electrical Wireman',
      certBody: 'Skill India Mission'
    },
    {
      name: 'Arjun Das',
      email: 'arjun.ac@homezy.demo',
      phone: '+91 98230 20005',
      trade: 'AC Service',
      location: 'Whitefield, Bangalore',
      skills: ['PCB Repair', 'Compressor Replacement', 'Jet Foam Cleaning'],
      rating: 4.8,
      reviews: 110,
      completedJobs: 106,
      workload: 2,
      earnings: 58900,
      cert: 'Daikin & BlueStar Approved Service Technician',
      certBody: 'HVAC Industry Council'
    },
    {
      name: 'Ramesh Babu',
      email: 'ramesh.plumber@homezy.demo',
      phone: '+91 98230 20006',
      trade: 'Plumber',
      location: 'BTM Layout, Bangalore',
      skills: ['Drain Unclogging', 'Water Tank Cleaning', 'Bathroom Fitting'],
      rating: 4.6,
      reviews: 74,
      completedJobs: 72,
      workload: 0,
      earnings: 32800,
      cert: 'Certified Domestic Water Systems Specialist',
      certBody: 'Skill Development Institute'
    },
    {
      name: 'Venkatesh Murthy',
      email: 'venkatesh.elec@homezy.demo',
      phone: '+91 98230 20007',
      trade: 'Electrician',
      location: 'Malleshwaram, Bangalore',
      skills: ['Earthing Setup', 'Heavy Appliance Wiring', 'Lighting Design'],
      rating: 4.9,
      reviews: 195,
      completedJobs: 190,
      workload: 2,
      earnings: 71000,
      cert: 'Licensed Electrical Supervisor (Class 1)',
      certBody: 'Electrical Inspectorate Karnataka'
    },
    {
      name: 'Mohammed Imran',
      email: 'imran.ac@homezy.demo',
      phone: '+91 98230 20008',
      trade: 'AC Service',
      location: 'Frazer Town, Bangalore',
      skills: ['Cassette AC', 'Window AC Overhaul', 'Gas Leak Soldering'],
      rating: 4.7,
      reviews: 65,
      completedJobs: 63,
      workload: 1,
      earnings: 34500,
      cert: 'Advanced HVAC Diagnostics Certificate',
      certBody: 'National Skill Registry'
    },
    {
      name: 'Praveen Patil',
      email: 'praveen.plumber@homezy.demo',
      phone: '+91 98230 20009',
      trade: 'Plumber',
      location: 'Rajajinagar, Bangalore',
      skills: ['Pressure Pump Repair', 'Faucet Overhaul', 'Sewage Blockage Cleansing'],
      rating: 4.8,
      reviews: 89,
      completedJobs: 87,
      workload: 1,
      earnings: 41200,
      cert: 'Plumbing Systems Standard Certification (PSSC)',
      certBody: 'Indian Plumbing Association'
    },
    {
      name: 'Dinesh Yadav',
      email: 'dinesh.elec@homezy.demo',
      phone: '+91 98230 20010',
      trade: 'Electrician',
      location: 'Bellandur, Bangalore',
      skills: ['Apartment Re-wiring', 'Generator Transfer Switch', 'EV Charger Install'],
      rating: 4.8,
      reviews: 93,
      completedJobs: 90,
      workload: 2,
      earnings: 45600,
      cert: 'EVSE Level 2 Installer Certified',
      certBody: 'Govt Skill India'
    },
    {
      name: 'Vinay Shettar',
      email: 'vinay.ac@homezy.demo',
      phone: '+91 98230 20011',
      trade: 'AC Service',
      location: 'Electronic City, Bangalore',
      skills: ['Inverter AC Sensor Repair', 'Coil Anti-Rust Coating', 'Indoor Fan Balancing'],
      rating: 4.6,
      reviews: 58,
      completedJobs: 56,
      workload: 0,
      earnings: 28900,
      cert: 'Appliance & Cooling Certified Specialist',
      certBody: 'NSDC Skill Mission'
    },
    {
      name: 'Santosh Naik',
      email: 'santosh.plumber@homezy.demo',
      phone: '+91 98230 20012',
      trade: 'Plumber',
      location: 'Marathahalli, Bangalore',
      skills: ['CPVC / UPVC Fitting', 'Geyser Inlet / Outlet', 'Submersible Pump'],
      rating: 4.7,
      reviews: 78,
      completedJobs: 75,
      workload: 1,
      earnings: 37400,
      cert: 'Hydraulic & Sanitary Services Diploma',
      certBody: 'Karnataka Technical Board'
    },
    {
      name: 'Anand Kulkarni',
      email: 'anand.elec@homezy.demo',
      phone: '+91 98230 20013',
      trade: 'Electrician',
      location: 'JP Nagar, Bangalore',
      skills: ['Power Factor Correction', 'Surge Protection', 'Main Distribution Board'],
      rating: 4.9,
      reviews: 130,
      completedJobs: 126,
      workload: 2,
      earnings: 56200,
      cert: 'Certified Master Electrician (Level 4)',
      certBody: 'NSDC'
    },
    {
      name: 'Girish Prasad',
      email: 'girish.ac@homezy.demo',
      phone: '+91 98230 20014',
      trade: 'AC Service',
      location: 'Hebbal, Bangalore',
      skills: ['Central AC Inspection', 'Odor Elimination', 'Capacitor Testing'],
      rating: 4.8,
      reviews: 84,
      completedJobs: 82,
      workload: 1,
      earnings: 43800,
      cert: 'Air Conditioning Specialist Certificate',
      certBody: 'Govt ITI Bangalore'
    },
    {
      name: 'Basavaraj Hiremath',
      email: 'basava.plumber@homezy.demo',
      phone: '+91 98230 20015',
      trade: 'Plumber',
      location: 'Yelahanka, Bangalore',
      skills: ['Water Line Inspection', 'Overhead Tank Plumbing', 'Drain Jetting'],
      rating: 4.8,
      reviews: 91,
      completedJobs: 89,
      workload: 1,
      earnings: 46100,
      cert: 'Cooperative Master Craftsman Credential',
      certBody: 'Bangalore Cooperative Federation'
    }
  ];

  // Admin user
  const adminUser = {
    _id: 'usr_admin_001',
    name: 'Federation Admin',
    email: 'admin@homezy.demo',
    phone: '+91 98000 00001',
    password: defaultPassword,
    role: 'admin',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
    status: 'active'
  };

  const users = [adminUser];
  const workerProfiles = [];
  const certifications = [];
  const welfares = [];

  // Build customers
  customerRaw.forEach((c, idx) => {
    users.push({
      _id: `usr_cust_${100 + idx}`,
      name: c.name,
      email: c.email,
      phone: c.phone,
      password: defaultPassword,
      role: 'customer',
      address: c.address,
      avatar: `https://images.unsplash.com/photo-${1535713875002 + idx}?w=150`,
      status: 'active'
    });
  });

  // Build workers
  workerRaw.forEach((w, idx) => {
    const userId = `usr_work_${200 + idx}`;
    const profileId = `wp_${300 + idx}`;
    const certId = `cert_${400 + idx}`;
    const welfareId = `welf_${500 + idx}`;

    users.push({
      _id: userId,
      name: w.name,
      email: w.email,
      phone: w.phone,
      password: defaultPassword,
      role: 'worker',
      avatar: `https://images.unsplash.com/photo-${1507003211169 + idx}?w=150`,
      status: 'active'
    });

    certifications.push({
      _id: certId,
      worker: profileId,
      title: w.cert,
      issuingBody: w.certBody,
      issueDate: new Date('2023-01-15'),
      expiryDate: new Date('2028-01-15'),
      credentialId: `CRD-KA-${2023000 + idx}`,
      verifiedStatus: 'verified'
    });

    welfares.push({
      _id: welfareId,
      worker: profileId,
      insuranceActive: true,
      insurancePolicyNumber: `COOP-HLTH-${202600 + idx}`,
      insuranceCoverage: 500000,
      emergencyFundBalance: 15000 + (idx * 600),
      pensionAccrued: 8200 + (idx * 450),
      patronageBonusYTD: 3450 + (idx * 280),
      healthCheckupStatus: idx % 3 === 0 ? 'scheduled' : 'completed',
      claims: [
        {
          title: 'Annual Family Preventive Health Screen',
          amount: 2500,
          status: 'approved',
          disbursedAt: new Date('2026-02-10')
        }
      ]
    });

    workerProfiles.push({
      _id: profileId,
      user: userId,
      name: w.name,
      email: w.email,
      phone: w.phone,
      cooperative: cooperative._id,
      trade: w.trade,
      skills: w.skills,
      bio: `Professional ${w.trade} with verified cooperative union backing and 5+ years of trusted local service.`,
      rating: w.rating,
      totalReviews: w.reviews,
      completedJobs: w.completedJobs,
      availability: w.workload >= 3 ? 'busy' : 'available',
      currentWorkload: w.workload,
      monthlyJobCount: Math.min(w.completedJobs % 30 + 12, 35),
      monthlyCap: 35,
      fairAllocationScore: Math.round(100 - (w.workload * 12)),
      totalEarnings: w.earnings,
      patronageDividendsEarned: Math.round(w.earnings * 0.08),
      verificationStatus: 'verified',
      location: {
        address: w.location,
        city: 'Bangalore',
        state: 'Karnataka',
        lat: 12.93 + (idx * 0.005),
        lng: 77.62 + (idx * 0.004)
      },
      certifications: [certId],
      welfare: welfareId
    });
  });

  // Create realistic bookings
  const bookings = [
    {
      _id: 'bk_001',
      customer: 'usr_cust_100', // Priya Sundaram
      worker: 'wp_300',         // Rahul Sharma (Electrician)
      service: 'srv_elec_002',  // Electrician
      scheduledDate: '2026-09-20',
      timeSlot: '10:00 AM - 11:30 AM',
      status: 'completed',
      amount: 499,
      platformFee: 25,
      workerEarnings: 474,
      welfareContribution: 5,
      customerAddress: '124, 4th Cross, Koramangala 4th Block, Bangalore',
      customerPhone: '+91 98450 11001',
      notes: 'Living room fan switch tripping the main MCB breaker.'
    },
    {
      _id: 'bk_002',
      customer: 'usr_cust_101', // Amitabh Sen
      worker: 'wp_301',         // Suresh Kumar (AC Service)
      service: 'srv_ac_001',    // AC Service
      scheduledDate: '2026-09-20',
      timeSlot: '02:00 PM - 03:30 PM',
      status: 'in_progress',
      amount: 899,
      platformFee: 45,
      workerEarnings: 854,
      welfareContribution: 9,
      customerAddress: '88, 100 Feet Rd, Indiranagar, Bangalore',
      customerPhone: '+91 98450 11002',
      notes: 'Master bedroom split AC cooling slowly, please do foam jet wash.'
    },
    {
      _id: 'bk_003',
      customer: 'usr_cust_102', // Sunita Reddy
      worker: 'wp_302',         // Manjunath Gowda (Plumber)
      service: 'srv_plumb_003', // Plumber
      scheduledDate: '2026-09-21',
      timeSlot: '11:00 AM - 12:30 PM',
      status: 'requested',
      amount: 399,
      platformFee: 20,
      workerEarnings: 379,
      welfareContribution: 4,
      customerAddress: '502, Prestige Palms, Whitefield, Bangalore',
      customerPhone: '+91 98450 11003',
      notes: 'Kitchen sink pipe dripping and slow drain.'
    },
    {
      _id: 'bk_004',
      customer: 'usr_cust_103', // Kavita Menon
      worker: 'wp_300',         // Rahul Sharma (Electrician)
      service: 'srv_elec_002',  // Electrician
      scheduledDate: '2026-09-21',
      timeSlot: '04:00 PM - 05:30 PM',
      status: 'accepted',
      amount: 650,
      platformFee: 33,
      workerEarnings: 617,
      welfareContribution: 7,
      customerAddress: '23, 14th Main, HSR Layout Sector 3, Bangalore',
      customerPhone: '+91 98450 11004',
      notes: 'Install 2 smart switches and check earthing in home office.'
    },
    {
      _id: 'bk_005',
      customer: 'usr_cust_104', // Vikram Joshi
      worker: 'wp_304',         // Arjun Das (AC Service)
      service: 'srv_ac_001',    // AC Service
      scheduledDate: '2026-09-19',
      timeSlot: '01:00 PM - 02:30 PM',
      status: 'completed',
      amount: 1199,
      platformFee: 60,
      workerEarnings: 1139,
      welfareContribution: 12,
      customerAddress: '71, 9th Cross, Jayanagar 4th Block, Bangalore',
      customerPhone: '+91 98450 11005',
      notes: 'Dual AC complete seasonal servicing and filter replacement.'
    }
  ];

  // Payments
  const payments = bookings.map((b, i) => ({
    _id: `pay_${600 + i}`,
    booking: b._id,
    customer: b.customer,
    worker: b.worker,
    amount: b.amount,
    cooperativeFee: b.platformFee,
    workerPayout: b.workerEarnings,
    welfareAllocation: b.welfareContribution,
    paymentMethod: i % 2 === 0 ? 'upi' : 'coop_wallet',
    status: b.status === 'completed' ? 'completed' : 'pending',
    transactionId: `TXN-HMZ-2026-${8890 + i}`,
    paidAt: new Date()
  }));

  // Ratings
  const ratings = [
    {
      _id: 'rat_701',
      booking: 'bk_001',
      customer: 'usr_cust_100',
      worker: 'wp_300',
      rating: 5,
      comment: 'Rahul arrived exactly on time, diagnosed the faulty MCB quickly and gave transparent co-op pricing without hidden charges!',
      workQualityRating: 5,
      punctualityRating: 5,
      politenessRating: 5
    },
    {
      _id: 'rat_702',
      booking: 'bk_005',
      customer: 'usr_cust_104',
      worker: 'wp_304',
      rating: 5,
      comment: 'Excellent AC foam jet service. Very polite and cleaned up completely after finishing the job.',
      workQualityRating: 5,
      punctualityRating: 5,
      politenessRating: 5
    }
  ];

  const notifications = [
    {
      _id: 'notif_801',
      user: 'usr_admin_001',
      title: 'Co-op Daily Allocation Health',
      message: 'Fair routing engine dispatched 15 jobs today with average worker satisfaction at 98.4%.',
      type: 'system',
      read: false,
      createdAt: new Date()
    },
    {
      _id: 'notif_802',
      user: 'usr_cust_100',
      title: 'Job Completed',
      message: 'Rahul Sharma completed your Electrician service. You saved ₹180 vs corporate platform rates!',
      type: 'booking',
      read: true,
      createdAt: new Date()
    }
  ];

  return {
    cooperative,
    services,
    users,
    workerProfiles,
    certifications,
    welfares,
    bookings,
    payments,
    ratings,
    notifications
  };
};

module.exports = { generateSeedData };
