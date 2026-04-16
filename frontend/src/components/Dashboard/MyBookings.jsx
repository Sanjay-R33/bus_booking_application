
export default function MyBookings({
  user, bookings, cancelBooking, loading
}) {
  if (!user) return null

  return (
    <section className="card">
      <h2>📋 My Bookings</h2>

      {bookings.length === 0 ? (
        <p className="muted-center">No bookings yet. Search and book a bus to get started!</p>
      ) : (
        bookings.map(b => (
          <div key={b.id || b.bookingId} className="booking-card">
            <div className="booking-left">
              <div className="booking-top-row">
                <span className="booking-ref">
                  #{b.bookingReference || b.id}
                </span>
                <span className={`status-pill ${b.status === 'BOOKED' ? 'pill-booked' : 'pill-cancelled'}`}>
                  {b.status || 'ACTIVE'}
                </span>
              </div>
              <div className="booking-route">
                {b.source || 'Source'} <strong>→</strong> {b.destination || 'Destination'}
              </div>
              <div className="booking-meta">
                <span>🚌 {b.busName || 'Bus'}</span>
                <span>💺 Seat {b.seatNumber || b.seatNumbers?.join(', ') || 'N/A'}</span>
                {b.travelDate && <span>📅 {new Date(b.travelDate).toLocaleDateString()}</span>}
                <span className="booking-fare">₹{b.totalFare || b.fare || 0}</span>
              </div>
            </div>
            
            {b.status === 'BOOKED' && (
              <button 
                className="btn-cancel" 
                onClick={() => cancelBooking(b.bookingReference || b.id)}
                disabled={loading}
              >
                {loading ? 'Cancelling...' : 'Cancel'}
              </button>
            )}
          </div>
        ))
      )}
    </section>
  )
}