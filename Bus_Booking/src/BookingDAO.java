import java.sql.*;
import java.util.*;

public class BookingDAO {
    public void createBooking(int scheduleId, String name, String email, int seats) throws SQLException {
        String sql = "INSERT INTO BOOKING (SCHEDULE_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, SEATS_BOOKED) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, scheduleId);
            p.setString(2, name);
            p.setString(3, email);
            p.setInt(4, seats);
            p.executeUpdate();
        }
    }

    public List<Booking> getBookingsForEmail(String email) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT BOOKING_ID, SCHEDULE_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, SEATS_BOOKED, BOOKED_AT FROM BOOKING WHERE CUSTOMER_EMAIL = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.bookingId = rs.getInt(1);
                    b.scheduleId = rs.getInt(2);
                    b.customerName = rs.getString(3);
                    b.customerEmail = rs.getString(4);
                    b.seatsBooked = rs.getInt(5);
                    b.bookedAt = rs.getTimestamp(6);
                    list.add(b);
                }
            }
        }
        return list;
    }

    public boolean cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM BOOKING WHERE BOOKING_ID = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, bookingId);
            int rows = p.executeUpdate();
            return rows > 0;
        }
    }
}
