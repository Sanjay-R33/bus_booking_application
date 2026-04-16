// components/Dashboard/BusSection.jsx
export default function BusSection({
  buses, loadSeats, loading
}) {
  return (
    <section className="card">
      <h2>🚌 Available Buses</h2>

      {buses.length === 0 ? (
        <p className="muted-center">No buses found for your search criteria</p>
      ) : (
        buses.map(bus => (
          <div key={bus.id} className="bus-card">
            <div className="bus-info">
              <div className="bus-header">
                <span className="bus-operator">{bus.operatorName || bus.busName || 'Bus'}</span>
                <span className="bus-number-badge">{bus.busNumber || bus.id}</span>
              </div>
              <div className="bus-route">
                {bus.source} <strong>→</strong> {bus.destination}
              </div>
              <div className="bus-meta">
                <span>📍 {bus.source}</span>
                <span>🎯 {bus.destination}</span>
                {bus.departureTime && <span>⏰ {bus.departureTime}</span>}
                {bus.arrivalTime && <span>🕐 {bus.arrivalTime}</span>}
                {bus.totalSeats && <span>💺 {bus.totalSeats} seats</span>}
                {bus.availableSeats && <span>✅ {bus.availableSeats} available</span>}
              </div>
            </div>
            <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', gap: '8px', alignItems: 'flex-end' }}>
              <div className="bus-fare">₹{bus.fare}</div>
              <button 
                className="btn-select" 
                onClick={() => loadSeats(bus)} 
                disabled={loading}
              >
                {loading ? 'Loading…' : 'Select Seats'}
              </button>
            </div>
          </div>
        ))
      )}
    </section>
  )
}