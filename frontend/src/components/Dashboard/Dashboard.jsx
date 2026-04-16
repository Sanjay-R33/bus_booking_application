// components/Dashboard/Dashboard.jsx
import SearchSection from './SearchSection'
import BusSection from './BusSection'
import BookingSection from './BookingSection'
import Modal from './Modal'
import MyBookings from './MyBookings'

export default function Dashboard(props) {
  return (
    <>
      <SearchSection {...props} />

      {props.buses.length > 0 && (
        <BusSection {...props} />
      )}

      {props.selectedBus && (
        <BookingSection {...props} />
      )}

      {props.showConfirmModal && (
        <Modal {...props} />
      )}

      <MyBookings {...props} />
    </>
  )
}