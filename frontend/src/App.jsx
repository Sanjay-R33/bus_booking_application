import { useState, useEffect } from 'react'
import './App.css'
import Login from './components/Auth/Login'
import Register from './components/Auth/Register'
// import MyBookings from './components/Dashboard/MyBookings'

export default function App() {
  const [user, setUser] = useState(
    JSON.parse(sessionStorage.getItem('user')) || null
  )
  const [currentPage, setCurrentPage] = useState('login')

  // Handle navigation using URL hash
  useEffect(() => {
    const hash = window.location.hash.slice(1) || 'login'

    if (user) {
      setCurrentPage('dashboard')
    } else {
      setCurrentPage(hash === 'register' ? 'register' : 'login')
    }
  }, [user])

  // Listen for hash change
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

  // Handle login
  const handleLoginSuccess = (userData) => {
    setUser(userData)
  }

  // Handle register
  const handleRegisterSuccess = (userData) => {
    setUser(userData)
  }

  // Logout
  const handleLogout = () => {
    setUser(null)
    sessionStorage.removeItem('user')
    window.location.hash = '#login'
  }

  // If not logged in → show auth pages
  if (!user) {
    return currentPage === 'register' ? (
      <Register onRegister={handleRegisterSuccess} />
    ) : (
      <Login onLogin={handleLoginSuccess} />
    )
  }


  return (
    <div className="container">
      <h1> Bus Booking System</h1>
      <h2>Welcome, {user.name}!</h2>

      <button onClick={handleLogout}>Logout</button>
    </div>
  )
}