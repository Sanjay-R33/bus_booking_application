
import { useState } from 'react'

export default function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (!email || !password) {
      setError('Please fill in all fields')
      return
    }

    setLoading(true)

    setTimeout(() => {
      
      const testUsers = [
        { id: 'user1', name: 'John Doe', email: 'john@example.com', password: 'password123' },
        { id: 'user2', name: 'Jane Smith', email: 'jane@example.com', password: 'password123' },
        { id: 'user3', name: 'Admin User', email: 'admin@example.com', password: 'admin123' },
      ]

      const user = testUsers.find(u => u.email === email && u.password === password)

      if (user) {
        const userData = { id: user.id, name: user.name, email: user.email }
        sessionStorage.setItem('user', JSON.stringify(userData))
        onLogin(userData)
      } else {
        setError('Invalid email or password')
      }

      setLoading(false)
    }, 500)
  }

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1> Bus Booking System</h1>
        <h2>Login to Your Account</h2>

        {error && <div className="auth-error">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label>Email Address</label>
            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button className="btn-primary" style={{ width: '100%' }} disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-divider">or</div>

        <button className="btn-secondary" onClick={() => window.location.hash = '#register'}>
          Create New Account
        </button>

        <div className="test-credentials">
          <p><strong>Test Credentials:</strong></p>
          <ul>
            <li>Email: john@example.com | Pass: password123</li>
            <li>Email: jane@example.com | Pass: password123</li>
            <li>Email: admin@example.com | Pass: admin123</li>
          </ul>
        </div>
      </div>
    </div>
  )
}