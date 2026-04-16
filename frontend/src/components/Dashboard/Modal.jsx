// components/Dashboard/Modal.jsx
export default function Modal({
  selectedBus,
  passengerDetails,
  passengerEmail,
  totalFare,
  submitBooking,
  setShowConfirmModal,
  loading
}) {
  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>✅ Confirm Your Booking</h2>
        <p className="modal-sub">Please review your booking details before confirming</p>

        {/* Bus Info */}
        {selectedBus && (
          <div className="modal-bus-info">
            <strong>Bus:</strong> {selectedBus.operatorName || selectedBus.busName} ({selectedBus.busNumber})<br />
            <strong>Route:</strong> {selectedBus.source} → {selectedBus.destination}<br />
            <strong>Fare per seat:</strong> ₹{selectedBus.fare}
          </div>
        )}

        {/* Passengers Table */}
        <table className="confirm-table">
          <thead>
            <tr>
              <th>Seat</th>
              <th>Passenger Name</th>
              <th>Age</th>
              <th>Fare</th>
            </tr>
          </thead>
          <tbody>
            {passengerDetails.map(p => (
              <tr key={p.seatNumber}>
                <td><strong>{p.seatNumber}</strong></td>
                <td>{p.name}</td>
                <td>{p.age}</td>
                <td>₹{selectedBus.fare}</td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <td colSpan="3" style={{ textAlign: 'right', fontWeight: '700' }}>Total:</td>
              <td style={{ color: '#16a34a', fontWeight: '700' }}>₹{totalFare()}</td>
            </tr>
          </tfoot>
        </table>

        {/* Email Info */}
        <div className="modal-email-row">
          📧 Confirmation will be sent to: <strong>{passengerEmail}</strong>
        </div>

        {/* Actions */}
        <div className="modal-actions">
          <button 
            className="btn-back" 
            onClick={() => setShowConfirmModal(false)}
            disabled={loading}
          >
            Back
          </button>
          <button 
            className="btn-confirm-final" 
            onClick={submitBooking}
            disabled={loading}
          >
            {loading ? 'Processing...' : `Confirm Booking (₹${totalFare()})`}
          </button>
        </div>
      </div>
    </div>
  )
}