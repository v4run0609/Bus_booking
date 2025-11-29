public class Schedule {
    public int scheduleId;
    public int busId;
    public String source;
    public String destination;
    public String departure;
    public double fare;

    public Schedule() {}
    public Schedule(int sid, int bid, String src, String dest, String dep, double fare) {
        this.scheduleId = sid; this.busId = bid; this.source = src; this.destination = dest; this.departure = dep; this.fare = fare;
    }
}
