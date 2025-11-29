import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static BusDAO busDao = new BusDAO();
    static ScheduleDAO schDao = new ScheduleDAO();
    static BookingDAO bookDao = new BookingDAO();

    public static void main(String[] args) {
        System.out.println("=== Bus Ticket Reservation System ===");
        while (true) {
            System.out.println("\n1. Admin (manage buses/schedules)\n2. Customer (book/view/cancel)\n3. Exit");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            try {
                if ("1".equals(c)) adminMenu();
                else if ("2".equals(c)) customerMenu();
                else if ("3".equals(c)) { System.out.println("Bye"); System.exit(0); }
                else System.out.println("Invalid");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    static void adminMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Admin ---\n1. Add Bus\n2. Add Schedule\n3. View Buses\n4. View Schedules\n5. Back");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();
            if ("1".equals(ch)) {
                System.out.print("Bus name: "); String name = sc.nextLine();
                System.out.print("Total seats: "); int seats = Integer.parseInt(sc.nextLine());
                busDao.addBus(name, seats);
                System.out.println("Bus added.");
            } else if ("2".equals(ch)) {
                System.out.print("Bus id: "); int bid = Integer.parseInt(sc.nextLine());
                System.out.print("Source: "); String src = sc.nextLine();
                System.out.print("Destination: "); String dst = sc.nextLine();
                System.out.print("Departure (e.g. 08:00 AM): "); String dep = sc.nextLine();
                System.out.print("Fare: "); double fare = Double.parseDouble(sc.nextLine());
                schDao.addSchedule(bid, src, dst, dep, fare);
                System.out.println("Schedule added.");
            } else if ("3".equals(ch)) {
                List<Bus> buses = busDao.getAll();
                System.out.println("Buses:");
                for (Bus b : buses) System.out.println(b.busId + ") " + b.busName + " [" + b.totalSeats + " seats]");
            } else if ("4".equals(ch)) {
                List<Schedule> list = schDao.getAll();
                System.out.println("Schedules:");
                for (Schedule s : list) System.out.println(s.scheduleId + ") Bus " + s.busId + " " + s.source + " -> " + s.destination + " @ " + s.departure + " Rs." + s.fare);
            } else if ("5".equals(ch)) return;
            else System.out.println("Invalid");
        }
    }

    static void customerMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Customer ---\n1. View schedules\n2. Book ticket\n3. My bookings (by email)\n4. Cancel booking\n5. Back");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();
            if ("1".equals(ch)) {
                List<Schedule> list = schDao.getAll();
                for (Schedule s : list) {
                    int avail = schDao.availableSeats(s.scheduleId);
                    System.out.println(s.scheduleId + ") " + s.source + " -> " + s.destination + " @ " + s.departure + " | Fare: " + s.fare + " | Available: " + avail);
                }
            } else if ("2".equals(ch)) {
                System.out.print("Schedule id: "); int sid = Integer.parseInt(sc.nextLine());
                int avail = schDao.availableSeats(sid);
                System.out.println("Available seats: " + avail);
                if (avail <= 0) { System.out.println("No seats available"); continue; }
                System.out.print("Your name: "); String name = sc.nextLine();
                System.out.print("Your email: "); String email = sc.nextLine();
                System.out.print("Seats to book: "); int seats = Integer.parseInt(sc.nextLine());
                if (seats <= 0 || seats > avail) { System.out.println("Invalid seats"); continue; }
                bookDao.createBooking(sid, name, email, seats);
                System.out.println("Booking successful.");
            } else if ("3".equals(ch)) {
                System.out.print("Enter your email: "); String email = sc.nextLine();
                List<Booking> bk = bookDao.getBookingsForEmail(email);
                System.out.println("Bookings:");
                for (Booking b : bk) System.out.println(b.bookingId + ") Schedule " + b.scheduleId + " - " + b.customerName + " seats: " + b.seatsBooked + " at " + b.bookedAt);
            } else if ("4".equals(ch)) {
                System.out.print("Booking id to cancel: "); int bid = Integer.parseInt(sc.nextLine());
                boolean ok = bookDao.cancelBooking(bid);
                System.out.println(ok ? "Cancelled." : "Not found.");
            } else if ("5".equals(ch)) return;
            else System.out.println("Invalid");
        }
    }
}
