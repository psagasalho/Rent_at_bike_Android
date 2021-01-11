package pt.rent_at_bike.app.bike;

import java.io.Serializable;

public class LatLon implements Serializable {
    double lat;
    double lon;

    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
