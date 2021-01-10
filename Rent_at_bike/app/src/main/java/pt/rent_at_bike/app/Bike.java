package pt.rent_at_bike.app;

import pt.rent_at_bike.app.LatLon;

import java.io.Serializable;

public class Bike implements Serializable {

    private int id;
    private String name;
    private String profileImg;
    private String typebike;
    private int price;
    private LatLon loc;
    private boolean available;

    public Bike(int n_id, String n_name, String n_profileImg, String n_typebike, int n_price, LatLon n_loc, boolean n_available) {
        id = n_id;
        name = n_name;
        profileImg = n_profileImg;
        typebike = n_typebike;
        price = n_price;
        loc = n_loc;
        available = n_available;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getTypebike() {
        return typebike;
    }

    public int getPrice() {
        return price;
    }

    public LatLon getLoc() {
        return loc;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", typebike='" + typebike + '\'' +
                ", price=" + price +
                ", loc=" + loc +
                ", available=" + available +
                '}';
    }
}
