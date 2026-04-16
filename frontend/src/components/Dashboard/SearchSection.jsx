// components/Dashboard/SearchSection.jsx
export default function SearchSection({
  user, search, setSearch, searchBuses, loading
}) {
  if (!user) return null

  return (
    <section className="card">
      <h2>🔍 Search Buses</h2>

      <form className="search-grid" onSubmit={searchBuses}>
        <div className="form-group">
          <input
            placeholder="Source city (e.g., Delhi)"
            value={search.source}
            onChange={e => setSearch({ ...search, source: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <input
            placeholder="Destination city (e.g., Mumbai)"
            value={search.destination}
            onChange={e => setSearch({ ...search, destination: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <input
            type="date"
            value={search.travelDate}
            onChange={e => setSearch({ ...search, travelDate: e.target.value })}
            required
          />
        </div>

        <button className="btn-primary search-btn" disabled={loading}>
          {loading ? 'Searching…' : '🔎 Search Buses'}
        </button>
      </form>
    </section>
  )
}