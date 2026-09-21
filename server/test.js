const { startServer } = require('./server');

const runTests = async () => {
  console.log('\n=============================================');
  console.log('STARTING HOMEZY BACKEND API INTEGRATION TESTS');
  console.log('=============================================\n');

  const server = await startServer();
  const baseUrl = 'http://localhost:5001/api';

  let passed = 0;
  let failed = 0;

  const assert = (condition, message) => {
    if (condition) {
      console.log(`✅ [PASS] ${message}`);
      passed++;
    } else {
      console.error(`❌ [FAIL] ${message}`);
      failed++;
    }
  };

  try {
    // 1. Health check
    const healthRes = await fetch(`${baseUrl}/health`).then(r => r.json());
    assert(healthRes.status === 'online', 'Health endpoint reports online status');

    // 2. Services list
    const servicesRes = await fetch(`${baseUrl}/services`).then(r => r.json());
    assert(servicesRes.success === true, 'Services list returns success');
    assert(servicesRes.data.length >= 3, `Services count >= 3 (Found: ${servicesRes.data.length})`);
    const serviceNames = servicesRes.data.map(s => s.name);
    assert(
      serviceNames.includes('AC Service') &&
      serviceNames.includes('Electrician') &&
      serviceNames.includes('Plumber'),
      'All requested services present (AC Service, Electrician, Plumber)'
    );

    // 3. Workers list
    const workersRes = await fetch(`${baseUrl}/workers`).then(r => r.json());
    assert(workersRes.success === true, 'Workers list returns success');
    assert(workersRes.count >= 15, `Worker count is at least 15 (Found: ${workersRes.count})`);

    const workerId = workersRes.data[0]._id;
    const singleWorkerRes = await fetch(`${baseUrl}/workers/${workerId}`).then(r => r.json());
    assert(singleWorkerRes.success === true, `Worker detail returns worker (${singleWorkerRes.data.name})`);
    assert(singleWorkerRes.data.fairAllocationScore > 0, 'Worker contains fairAllocationScore');

    // 4. Auth - Login as Admin
    const adminLoginRes = await fetch(`${baseUrl}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: 'admin@homezy.demo',
        password: 'Homezy@123'
      })
    }).then(r => r.json());

    assert(adminLoginRes.success === true, 'Admin login succeeded');
    assert(!!adminLoginRes.token, 'Admin JWT token returned');
    const adminToken = adminLoginRes.token;

    // 5. Auth - Login as Customer
    const customerLoginRes = await fetch(`${baseUrl}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: 'priya.s@example.com',
        password: 'Homezy@123'
      })
    }).then(r => r.json());
    assert(customerLoginRes.success === true, 'Customer login succeeded');
    const customerToken = customerLoginRes.token;

    // 6. Auth - Register new user
    const testEmail = `newcustomer_${Date.now()}@example.com`;
    const registerRes = await fetch(`${baseUrl}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Aarav Patel',
        email: testEmail,
        phone: '+91 98450 99999',
        password: 'Password@123',
        role: 'customer'
      })
    }).then(r => r.json());
    assert(registerRes.success === true, 'New customer registration succeeded');
    assert(!!registerRes.token, 'Registration returns JWT token');

    // 7. Bookings - Create Booking
    const newBookingRes = await fetch(`${baseUrl}/bookings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${customerToken}`
      },
      body: JSON.stringify({
        workerId: workerId,
        serviceId: servicesRes.data[0]._id,
        scheduledDate: '2026-09-25',
        timeSlot: '11:00 AM - 12:30 PM',
        customerAddress: '55, Lavelle Road, Bangalore',
        notes: 'Seasonal checkup'
      })
    }).then(r => r.json());
    assert(newBookingRes.success === true, 'Booking created successfully');
    assert(newBookingRes.data.platformFee === Math.round(newBookingRes.data.amount * 0.05), 'Booking enforces transparent 5% platform fee');
    const createdBookingId = newBookingRes.data._id;

    // 8. Bookings - Get Bookings
    const bookingsListRes = await fetch(`${baseUrl}/bookings`, {
      headers: { 'Authorization': `Bearer ${customerToken}` }
    }).then(r => r.json());
    assert(bookingsListRes.success === true, 'Bookings list retrieved');
    assert(bookingsListRes.data.length > 0, 'Customer has active bookings');

    // 9. Bookings - Update Booking Status
    const updateStatusRes = await fetch(`${baseUrl}/bookings/${createdBookingId}/status`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${customerToken}`
      },
      body: JSON.stringify({ status: 'in_progress' })
    }).then(r => r.json());
    assert(updateStatusRes.success === true, 'Booking status updated to in_progress');

    // 10. Payments - Process Payment
    const paymentRes = await fetch(`${baseUrl}/payments`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${customerToken}`
      },
      body: JSON.stringify({
        bookingId: createdBookingId,
        amount: 599,
        paymentMethod: 'upi'
      })
    }).then(r => r.json());
    assert(paymentRes.success === true, 'Payment processed successfully');
    assert(paymentRes.data.cooperativeFee === Math.round(599 * 0.05), 'Payment calculates 5% cooperative fee');

    // 11. Ratings - Submit Rating
    const ratingRes = await fetch(`${baseUrl}/ratings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${customerToken}`
      },
      body: JSON.stringify({
        bookingId: createdBookingId,
        workerId: workerId,
        rating: 5,
        comment: 'Brilliant cooperative service, highly skilled and punctual!'
      })
    }).then(r => r.json());
    assert(ratingRes.success === true, 'Rating submitted successfully');

    // 12. Welfare - Get Worker Welfare
    const welfareRes = await fetch(`${baseUrl}/welfare/${workerId}`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    }).then(r => r.json());
    assert(welfareRes.success === true, 'Worker welfare profile retrieved');
    assert(welfareRes.data.insuranceActive === true, 'Worker insurance coverage confirmed active');

    // 13. Admin - Dashboard Stats
    const dashboardRes = await fetch(`${baseUrl}/admin/dashboard`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    }).then(r => r.json());
    assert(dashboardRes.success === true, 'Admin dashboard stats returned');
    assert(dashboardRes.data.totalWorkers >= 15, `Admin dashboard confirms ${dashboardRes.data.totalWorkers} workers`);
    assert(dashboardRes.data.platformFeePercentage === 5.0, 'Federation platform fee verified at 5.0%');

    // 14. Admin - Workers
    const adminWorkersRes = await fetch(`${baseUrl}/admin/workers`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    }).then(r => r.json());
    assert(adminWorkersRes.success === true, 'Admin worker list retrieved');

    // 15. Admin - Analytics
    const analyticsRes = await fetch(`${baseUrl}/admin/analytics`, {
      headers: { 'Authorization': `Bearer ${adminToken}` }
    }).then(r => r.json());
    assert(analyticsRes.success === true, 'Admin federation analytics retrieved');
    assert(analyticsRes.data.servicesBreakdown.length >= 3, 'Service financial breakdown available');

  } catch (err) {
    console.error('Test execution error:', err);
    failed++;
  } finally {
    console.log('\n=============================================');
    console.log(`TEST RESULTS: ${passed} PASSED, ${failed} FAILED`);
    console.log('=============================================\n');

    server.close(() => {
      process.exit(failed > 0 ? 1 : 0);
    });
  }
};

runTests();
