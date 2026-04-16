// Script to add sample buses directly to MySQL database
const mysql = require('mysql2/promise')

const busData = [
  {
    source: 'Delhi',
    destination: 'Mumbai',
    departureTime: '2026-04-20 18:00:00',
    arrivalTime: '2026-04-21 08:00:00',
    totalSeats: 50,
    fare: 1500.00
  },
  {
    source: 'Delhi',
    destination: 'Bangalore',
    departureTime: '2026-04-20 09:00:00',
    arrivalTime: '2026-04-20 20:00:00',
    totalSeats: 40,
    fare: 2000.00
  },
  {
    source: 'Mumbai',
    destination: 'Goa',
    departureTime: '2026-04-20 14:00:00',
    arrivalTime: '2026-04-20 22:00:00',
    totalSeats: 45,
    fare: 800.00
  },
  {
    source: 'Bangalore',
    destination: 'Chennai',
    departureTime: '2026-04-20 08:00:00',
    arrivalTime: '2026-04-20 12:00:00',
    totalSeats: 35,
    fare: 600.00
  },
  {
    source: 'Delhi',
    destination: 'Jaipur',
    departureTime: '2026-04-20 10:00:00',
    arrivalTime: '2026-04-20 13:00:00',
    totalSeats: 55,
    fare: 500.00
  },
  {
    source: 'Mumbai',
    destination: 'Pune',
    departureTime: '2026-04-20 15:00:00',
    arrivalTime: '2026-04-20 18:00:00',
    totalSeats: 40,
    fare: 400.00
  },
  {
    source: 'Bangalore',
    destination: 'Hyderabad',
    departureTime: '2026-04-20 11:00:00',
    arrivalTime: '2026-04-20 17:00:00',
    totalSeats: 50,
    fare: 1200.00
  },
  {
    source: 'Chennai',
    destination: 'Bangalore',
    departureTime: '2026-04-20 06:00:00',
    arrivalTime: '2026-04-20 10:00:00',
    totalSeats: 45,
    fare: 600.00
  }
]

async function addBusesToDatabase() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '@AK005choco',
    database: 'bus'
  })

  console.log('🚍 Adding sample buses to the database...\n')

  for (const bus of busData) {
    try {
      const query = `
        INSERT INTO bus (source, destination, departure_time, arrival_time, total_seats, available_seats, fare)
        VALUES (?, ?, ?, ?, ?, ?, ?)
      `
      
      await connection.execute(query, [
        bus.source,
        bus.destination,
        bus.departureTime,
        bus.arrivalTime,
        bus.totalSeats,
        bus.totalSeats, // available_seats = total_seats initially
        bus.fare
      ])
      
      console.log(`✅ Added: ${bus.source} → ${bus.destination}`)
    } catch (error) {
      console.log(`❌ Failed: ${bus.source} → ${bus.destination} - ${error.message}`)
    }
  }

  await connection.end()
  console.log('\n✨ Done! All buses have been added to the database.')
}

addBusesToDatabase().catch(err => {
  console.error('Database connection failed:', err.message)
  process.exit(1)
})
