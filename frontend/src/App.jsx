import { useState, useEffect } from 'react'
import './App.css'
import Dashboard from './components/Dashboard/Dashboard'
import Login from './components/Auth/Login'
import Register from './components/Auth/Register'
import { busApi, bookingApi } from './services/api'

export default function App() {
  // Auth state
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('user')) || null)
  const [currentPage, setCurrentPage] = useState('login')

  // Search state
  const [search, setSearch] = useState({
    source: '',
    destination: '',
    travelDate: ''
  })

  // Bus & Seat state
  const [buses, setBuses] = useState([])
  const [selectedBus, setSelectedBus] = useState(null)
  const [seats, setSeats] = useState([])
  const [selectedSeatNumbers, setSelectedSeatNumbers] = useState([])

  // Passenger state
  const [passengerEmail, setPassengerEmail] = useState('')
  const [passengerDetails, setPassengerDetails] = useState([])

  // Booking state
  const [bookings, setBookings] = useState([])
  const [showConfirmModal, setShowConfirmModal] = useState(false)
  const [loading, setLoading] = useState(false)

  // Handle URL hash for navigation
  useEffect(() => {
    const hash = window.location.hash.slice(1) || 'login'
    if (user) {
      setCurrentPage('dashboard')
    } else {
      setCurrentPage(hash === 'register' ? 'register' : 'login')
    }
  }, [user])

  // Listen for hash changes
  useEffect(() => {
    const handleHashChange = () => {
      const hash = window.location.hash.slice(1) || 'login'
      if (user) {
        setCurrentPage('dashboard')
      } else {
        setCurrentPage(hash === 'register' ? 'register' : 'login')
      }
    }

    window.addEventListener('hashchange', handleHashChange)
    return () => window.removeEventListener('hashchange', handleHashChange)
  }, [user])

  // Load my bookings on mount and when user changes
  useEffect(() => {
    if (user) {
      fetchMyBookings()
    }
  }, [user])

  // Fetch my bookings
  const fetchMyBookings = async () => {
    const result = await bookingApi.getMyBookings(user?.id)
    if (result.success) {
      const bookingsData = Array.isArray(result.data) ? result.data : result.data.bookings || []
      setBookings(bookingsData)
    } else {
      console.error('Error fetching bookings:', result.error)
    }
  }

  // Search buses
  const searchBuses = async (e) => {
    e.preventDefault()
    if (!search.source || !search.destination || !search.travelDate) {
      alert('Please fill all search fields')
      return
    }

    setLoading(true)
    const result = await busApi.searchBuses(search.source, search.destination, search.travelDate)
    
    if (result.success) {
      const busesData = Array.isArray(result.data) ? result.data : result.data.buses || []
      setBuses(busesData)
      if (busesData.length === 0) {
        alert('No buses found for your search criteria')
      }
      setSelectedBus(null)
      setSelectedSeatNumbers([])
      setPassengerDetails([])
    } else {
      alert('Error searching buses: ' + result.error)
    }
    setLoading(false)
  }

  // Load seats for selected bus
  const loadSeats = async (bus) => {
    setLoading(true)
    const result = await busApi.getSeats(bus.id)
    
    if (result.success) {
      const seatsData = Array.isArray(result.data) ? result.data : result.data.seats || []
      setSeats(seatsData)
      setSelectedBus(bus)
      setSelectedSeatNumbers([])
      setPassengerDetails([])
    } else {
      alert('Error loading seats: ' + result.error)
    }
    setLoading(false)
  }

  // Toggle seat selection
  const toggleSeat = (seatNumber, isBooked) => {
    if (isBooked) {
      alert('This seat is already booked')
      return
    }

    if (selectedSeatNumbers.includes(seatNumber)) {
      setSelectedSeatNumbers(selectedSeatNumbers.filter(s => s !== seatNumber))
      setPassengerDetails(passengerDetails.filter(p => p.seatNumber !== seatNumber))
    } else {
      setSelectedSeatNumbers([...selectedSeatNumbers, seatNumber])
      setPassengerDetails([
        ...passengerDetails,
        { seatNumber, name: '', age: '', gender: '' }
      ])
    }
  }

  // Update passenger details
  const updatePassenger = (seatNumber, field, value) => {
    setPassengerDetails(
      passengerDetails.map(p =>
        p.seatNumber === seatNumber ? { ...p, [field]: value } : p
      )
    )
  }

  // Handle review/confirm click
  const handleReviewClick = () => {
    for (let passenger of passengerDetails) {
      if (!passenger.name || !passenger.age) {
        alert('Please fill all passenger details')
        return
      }
    }
    
    if (!passengerEmail) {
      alert('Please enter email')
      return
    }

    setShowConfirmModal(true)
  }

  // Calculate total fare
  const totalFare = () => {
    if (!selectedBus) return 0
    return selectedSeatNumbers.length * selectedBus.fare
  }

  // Submit booking
  const submitBooking = async () => {
    setLoading(true)
    const bookingPayload = {
      userId: user.id,
      busId: selectedBus.id,
      seats: selectedSeatNumbers,
      passengerDetails: passengerDetails,
      email: passengerEmail,
      totalFare: totalFare()
    }

    const result = await bookingApi.createBooking(bookingPayload, user?.id)

    if (result.success) {
      alert('Booking confirmed! Booking Reference: ' + (result.data.bookingReference || result.data.id))
      
      setShowConfirmModal(false)
      setSelectedBus(null)
      setSelectedSeatNumbers([])
      setPassengerDetails([])
      setPassengerEmail('')
      setBuses([])
      
      await fetchMyBookings()
    } else {
      alert('Booking failed: ' + result.error)
    }
    
    setLoading(false)
  }

  // Cancel booking
  const cancelBooking = async (bookingId) => {
    if (!window.confirm('Are you sure you want to cancel this booking?')) {
      return
    }

    setLoading(true)
    const result = await bookingApi.cancelBooking(bookingId, user?.id)

    if (result.success) {
      alert('Booking cancelled successfully')
      await fetchMyBookings()
    } else {
      alert('Cancellation failed: ' + result.error)
    }
    setLoading(false)
  }

  // Handle login
  const handleLoginSuccess = (userData) => {
    setUser(userData)
    setCurrentPage('dashboard')
  }

  // Handle register
  const handleRegisterSuccess = (userData) => {
    setUser(userData)
    setCurrentPage('dashboard')
  }

  // Logout
  const handleLogout = () => {
    setUser(null)
    sessionStorage.removeItem('user')
    setBuses([])
    setSeats([])
    setBookings([])
    setSelectedBus(null)
    setSelectedSeatNumbers([])
    setPassengerDetails([])
    setPassengerEmail('')
    setShowConfirmModal(false)
    window.location.hash = '#login'
  }

  // If not logged in, show auth pages
  if (!user) {
    return (
      <>
        {currentPage === 'register' ? (
          <Register onRegister={handleRegisterSuccess} />
        ) : (
          <Login onLogin={handleLoginSuccess} />
        )}
      </>
    )
  }

  // Show dashboard if logged in
  return (
    <div className="container">
      <header className="header">
        <h1>🚌 Bus Booking System</h1>
        <div className="header-right">
          <span className="welcome-text">Welcome, {user.name}!</span>
          <button className="btn-logout" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </header>

      <Dashboard
        user={user}
        search={search}
        setSearch={setSearch}
        buses={buses}
        seats={seats}
        selectedBus={selectedBus}
        selectedSeatNumbers={selectedSeatNumbers}
        passengerDetails={passengerDetails}
        passengerEmail={passengerEmail}
        bookings={bookings}
        loading={loading}
        
        searchBuses={searchBuses}
        loadSeats={loadSeats}
        toggleSeat={toggleSeat}
        updatePassenger={updatePassenger}
        handleReviewClick={handleReviewClick}
        submitBooking={submitBooking}
        cancelBooking={cancelBooking}
        
        totalFare={totalFare}
        setPassengerEmail={setPassengerEmail}
        setShowConfirmModal={setShowConfirmModal}
        showConfirmModal={showConfirmModal}
      />
    </div>
  )
}