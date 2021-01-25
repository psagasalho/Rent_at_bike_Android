package pt.rent_at_bike.app.history;

import java.time.LocalDate;

public class History {

    private int histID;
    private String userEmail;
    private int bikeID;
    private int priceTotal;
    private LocalDate start;
    private LocalDate stop;

    public History(int histID, String userEmail, int bikeID, int priceTotal, LocalDate start, LocalDate stop) {
        this.histID = histID;
        this.userEmail = userEmail;
        this.bikeID = bikeID;
        this.priceTotal = priceTotal;
        this.start = start;
        this.stop = stop;
    }

    public int getHistID() {
        return histID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getBikeID() {
        return bikeID;
    }

    public int getPriceTotal() {
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
