package pt.rent_at_bike.app;

import java.io.Serializable;

public class LatLon implements Serializable {
    double lat;
    double lon;

    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
