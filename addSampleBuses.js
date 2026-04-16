// Script to add sample buses to the database
const API_URL = 'http://localhost:8080/api'

const sampleBuses = [
  {
    source: 'Delhi',
    destination: 'Mumbai',
    departureTime: '2026-04-20T18:00:00',
    arrivalTime: '2026-04-21T08:00:00',
    totalSeats: 50,
    fare: 1500.00
  },
  {
    source: 'Delhi',
    destination: 'Bangalore',
    departureTime: '2026-04-20T09:00:00',
    arrivalTime: '2026-04-20T20:00:00',
    totalSeats: 40,
    fare: 2000.00
  },
  {
    source: 'Mumbai',
    destination: 'Goa',
    departureTime: '2026-04-20T14:00:00',
    arrivalTime: '2026-04-20T22:00:00',
    totalSeats: 45,
    fare: 800.00
  },
  {
    source: 'Bangalore',
    destination: 'Chennai',
    departureTime: '2026-04-20T08:00:00',
    arrivalTime: '2026-04-20T12:00:00',
    totalSeats: 35,
    fare: 600.00
  },
  {
    source: 'Delhi',
    destination: 'Jaipur',
    departureTime: '2026-04-20T10:00:00',
    arrivalTime: '2026-04-20T13:00:00',
    totalSeats: 55,
    fare: 500.00
  },
  {
    source: 'Mumbai',
    destination: 'Pune',
    departureTime: '2026-04-20T15:00:00',
    arrivalTime: '2026-04-20T18:00:00',
    totalSeats: 40,
    fare: 400.00
  },
  {
    source: 'Bangalore',
    destination: 'Hyderabad',
    departureTime: '2026-04-20T11:00:00',
    arrivalTime: '2026-04-20T17:00:00',
    totalSeats: 50,
    fare: 1200.00
  },
  {
    source: 'Chennai',
    destination: 'Bangalore',
    departureTime: '2026-04-20T06:00:00',
    arrivalTime: '2026-04-20T10:00:00',
    totalSeats: 45,
    fare: 600.00
  }
]

async function addBuses() {
  console.log('🚍 Adding sample buses to the database...\n')
  
  for (const bus of sampleBuses) {
    try {
      const response = await fetch(`${API_URL}/buses`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(bus)
      })
      
      if (response.ok) {
        const data = await response.json()
        console.log(`✅ Added: ${bus.source} → ${bus.destination} (ID: ${data.id})`)
      } else {
        const error = await response.text()
        console.log(`❌ Failed: ${bus.source} → ${bus.destination} - ${error}`)
      }
    } catch (error) {
      console.log(`❌ Error: ${bus.source} → ${bus.destination} - ${error.message}`)
    }
  }
  
  console.log('\n✨ Done! All buses have been added to the database.')
}

addBuses()
