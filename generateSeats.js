// Script to create seats for existing buses
const mysql = require('mysql2/promise')

async function createSeatsForExistingBuses() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '@AK005choco',
    database: 'bus'
  })

  console.log('🎫 Creating seats for existing buses...\n')

  try {
    // Get all buses
    const [buses] = await connection.query('SELECT * FROM bus')
    
    for (const bus of buses) {
      // Check if seats already exist for this bus
      const [existingSeats] = await connection.query(
        'SELECT COUNT(*) as count FROM seat WHERE bus_id = ?',
        [bus.id]
      )
      
      if (existingSeats[0].count > 0) {
        console.log(`✅ Seats already exist for Bus ${bus.id}: ${bus.source} → ${bus.destination}`)
        continue
      }

      // Create seats for this bus
      const seatsPerRow = 5
      const rows = Math.ceil(bus.total_seats / seatsPerRow)
      let seatCounter = 0

      for (let row = 0; row < rows; row++) {
        for (let col = 0; col < seatsPerRow && seatCounter < bus.total_seats; col++) {
          const rowLetter = String.fromCharCode(65 + row) // A, B, C, etc.
          const seatNumber = rowLetter + (col + 1)

          await connection.execute(
            'INSERT INTO seat (bus_id, seat_number, status) VALUES (?, ?, ?)',
            [bus.id, seatNumber, 'NOT_BOOKED']
          )

          seatCounter++
        }
      }

      console.log(`✅ Created ${bus.total_seats} seats for Bus ${bus.id}: ${bus.source} → ${bus.destination}`)
    }

    console.log('\n✨ Seat generation complete!')
  } catch (error) {
    console.error('❌ Error:', error.message)
  } finally {
    await connection.end()
  }
}

createSeatsForExistingBuses()
