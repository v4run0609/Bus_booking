import java.sql.*;
import java.util.*;

public class BusDAO {
    public void addBus(String name, int seats) throws SQLException {
        String sql = "INSERT INTO BUS (BUS_NAME, TOTAL_SEATS) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name); p.setInt(2, seats); p.executeUpdate();
        }
    }

    public List<Bus> getAll() throws SQLException {
        List<Bus> list = new ArrayList<>();
        String sql = "SELECT BUS_ID, BUS_NAME, TOTAL_SEATS FROM BUS";
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(new Bus(rs.getInt(1), rs.getString(2), rs.getInt(3)));
        }
        return list;
    }

    public Bus findById(int id) throws SQLException {
        String sql = "SELECT BUS_ID, BUS_NAME, TOTAL_SEATS FROM BUS WHERE BUS_ID = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return new Bus(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        }
        return null;
    }
}
