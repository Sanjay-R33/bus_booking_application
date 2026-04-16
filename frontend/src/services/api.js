// API service file for all backend calls
const API_BASE_URL = 'http://localhost:8080/api'

// Helper function for API calls
const apiCall = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }
  
  try {
    const response = await fetch(url, {
      ...options,
      headers,
    })
    
    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || `HTTP Error: ${response.status}`)
    }
    
    const data = await response.json().catch(() => ({}))
    return { success: true, data }
  } catch (error) {
    return { success: false, error: error.message }
  }
}

// USER CONTROLLER APIs - Authentication
export const userApi = {
  register: async (userData) => {
    return apiCall('/users/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    })
  },

  login: async (email, password) => {
    return apiCall('/users/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    })
  },
}

// BUS CONTROLLER APIs
export const busApi = {
  searchBuses: async (source, destination, travelDate) => {
    return apiCall(`/buses/search?source=${source}&destination=${destination}&travelDate=${travelDate}`)
  },

  getSeats: async (busId) => {
    return apiCall(`/buses/${busId}/seats`)
  },
}

// BOOKING CONTROLLER APIs
export const bookingApi = {
  createBooking: async (bookingData, userId) => {
    return apiCall('/bookings', {
      method: 'POST',
      body: JSON.stringify(bookingData),
      headers: {
        'X-User-Id': userId,
      },
    })
  },

  getMyBookings: async (userId) => {
    return apiCall('/bookings/me', {
      method: 'GET',
      headers: {
        'X-User-Id': userId,
      },
    })
  },

  cancelBooking: async (bookingId, userId) => {
    return apiCall(`/bookings/${bookingId}/cancel`, {
      method: 'POST',
      headers: {
        'X-User-Id': userId,
      },
    })
  },
}

export default { userApi, busApi, bookingApi }
