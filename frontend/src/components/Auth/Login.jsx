// components/Auth/Login.jsx
import { useState } from 'react'
import { userApi } from '../../services/api'

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

    // Call the API to authenticate
    const result = await userApi.login(email, password)

    if (result.success) {
      const userData = result.data
      sessionStorage.setItem('user', JSON.stringify(userData))
      onLogin(userData)
    } else {
      setError(result.error || 'Invalid email or password')
    }

    setLoading(false)
  }

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>🚌 Bus Booking System</h1>
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
          <p>First register a new account or use existing credentials</p>
        </div>
      </div>
    </div>
  )
}
