// components/Dashboard/BookingSection.jsx
export default function BookingSection({
  selectedBus,
  seats,
  selectedSeatNumbers,
  toggleSeat,
  passengerDetails,
  passengerEmail,
  setPassengerEmail,
  updatePassenger,
  handleReviewClick,
  totalFare
}) {
  const availableSeats = seats.filter(s => !s.booked).length
  const bookedSeats = seats.filter(s => s.booked).length

  return (
    <section className="card">
      <h2>💺 Seat Selection</h2>

      {/* Bus Summary */}
      {selectedBus && (
        <div className="bus-summary-bar">
          <span><strong>{selectedBus.operatorName || selectedBus.busName}</strong> - {selectedBus.source} → {selectedBus.destination}</span>
          <span>₹{selectedBus.fare}/seat</span>
        </div>
      )}

      {/* Seat Legend */}
      <div className="legend">
        <div>
          <span className="dot dot-available"></span>
          <span>Available ({availableSeats})</span>
        </div>
        <div>
          <span className="dot dot-selected"></span>
          <span>Selected ({selectedSeatNumbers.length})</span>
        </div>
        <div>
          <span className="dot dot-booked"></span>
          <span>Booked ({bookedSeats})</span>
        </div>
      </div>

      {/* Seats Grid */}
      <div className="seat-grid">
        {seats.map(seat => {
          const isSelected = selectedSeatNumbers.includes(seat.seatNumber)
          const isBooked = seat.booked || seat.status === 'BOOKED'

          let seatClass = 'seat available'
          if (isBooked) seatClass = 'seat booked'
          else if (isSelected) seatClass = 'seat selected'

          return (
            <button
              key={seat.id}
              className={seatClass}
              disabled={isBooked}
              onClick={() => toggleSeat(seat.seatNumber, isBooked)}
              title={isBooked ? 'Already booked' : `${isSelected ? 'Deselect' : 'Select'} seat ${seat.seatNumber}`}
            >
              {seat.seatNumber}
            </button>
          )
        })}
      </div>

      {/* Price Summary */}
      {selectedSeatNumbers.length > 0 && (
        <div className="price-summary">
          <div className="price-row">
            <span>Seats: {selectedSeatNumbers.join(', ')}</span>
          </div>
          <div className="price-row">
            <span>Units: {selectedSeatNumbers.length} × ₹{selectedBus.fare}</span>
            <span>₹{selectedSeatNumbers.length * selectedBus.fare}</span>
          </div>
          <div className="price-row total-row">
            <span>Total Fare:</span>
            <span className="total-amount">₹{totalFare()}</span>
          </div>
        </div>
      )}

      {/* Passenger Details */}
      {selectedSeatNumbers.length > 0 && (
        <div className="passenger-section">
          <h3>👥 Passenger Details</h3>
          <p className="section-hint">
            Enter the email and details for each selected seat
          </p>

          <div className="email-field">
            <label style={{ display: 'block', marginBottom: '5px' }}>Email Address</label>
            <input
              placeholder="your.email@example.com"
              type="email"
              value={passengerEmail}
              onChange={e => setPassengerEmail(e.target.value)}
              style={{ marginBottom: '16px' }}
            />
          </div>

          {passengerDetails.map(p => (
            <div key={p.seatNumber} className="passenger-card">
              <div className="passenger-card-header">
                <strong>Seat {p.seatNumber}</strong>
                <span className="seat-price">₹{selectedBus.fare}</span>
              </div>
              
              <div className="passenger-fields">
                <input
                  placeholder="Full Name"
                  value={p.name}
                  onChange={e => updatePassenger(p.seatNumber, 'name', e.target.value)}
                  required
                />

                <input
                  placeholder="Age"
                  type="number"
                  min="1"
                  max="150"
                  value={p.age}
                  onChange={e => updatePassenger(p.seatNumber, 'age', e.target.value)}
                  required
                />

                <input
                  placeholder="Gender (M/F/O)"
                  value={p.gender || ''}
                  onChange={e => updatePassenger(p.seatNumber, 'gender', e.target.value)}
                />
              </div>
            </div>
          ))}

          <button className="btn-confirm-booking" onClick={handleReviewClick}>
            Confirm & Review Booking (₹{totalFare()})
          </button>
        </div>
      )}

      {selectedSeatNumbers.length === 0 && seats.length > 0 && (
        <p className="muted-center" style={{ marginTop: '20px' }}>
          Select seats to proceed with booking
        </p>
      )}
    </section>
  )
}