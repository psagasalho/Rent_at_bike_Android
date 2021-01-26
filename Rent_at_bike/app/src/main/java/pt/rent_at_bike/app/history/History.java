package pt.rent_at_bike.app.history;

import java.io.Serializable;
import java.time.LocalDate;

public class History implements Serializable {

    private long histID;
    private String userEmail;
    private long bikeID;
    private long priceTotal;
    private LocalDate start;
    private LocalDate stop;

    public History(long histID, String userEmail, long bikeID, long priceTotal, LocalDate start, LocalDate stop) {
        this.histID = histID;
        this.userEmail = userEmail;
        this.bikeID = bikeID;
        this.priceTotal = priceTotal;
        this.start = start;
        this.stop = stop;
    }

    public long getHistID() {
        return histID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getBikeID() {
        return bikeID;
    }

    public long getPriceTotal() {
        return priceTotal;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getStop() {
        return stop;
    }

    @Override
    public String toString() {
        return "History{" +
                "histID=" + histID +
                ", userEmail='" + userEmail + '\'' +
                ", bikeID=" + bikeID +
                ", priceTotal=" + priceTotal +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
