import java.sql.*;
import java.util.*;

public class ScheduleDAO {
    public void addSchedule(int busId, String src, String dest, String departure, double fare) throws SQLException {
        String sql = "INSERT INTO SCHEDULE (BUS_ID, SOURCE, DESTINATION, DEPARTURE_TIME, FARE) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, busId); p.setString(2, src); p.setString(3, dest); p.setString(4, departure); p.setDouble(5, fare);
            p.executeUpdate();
        }
    }

    public List<Schedule> getAll() throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT SCHEDULE_ID, BUS_ID, SOURCE, DESTINATION, DEPARTURE_TIME, FARE FROM SCHEDULE";
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Schedule(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6)));
        }
        return list;
    }

    public Schedule findById(int id) throws SQLException {
        String sql = "SELECT SCHEDULE_ID, BUS_ID, SOURCE, DESTINATION, DEPARTURE_TIME, FARE FROM SCHEDULE WHERE SCHEDULE_ID = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return new Schedule(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6));
            }
        }
        return null;
    }

    public int availableSeats(int scheduleId) throws SQLException {
        // total seats from bus - sum of seats booked for this schedule
        String sql = "SELECT b.total_seats - NVL((SELECT SUM(seats_booked) FROM booking bk WHERE bk.schedule_id = ?),0) "
                   + "FROM bus b JOIN schedule s ON b.bus_id = s.bus_id WHERE s.schedule_id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, scheduleId);
            p.setInt(2, scheduleId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
