package pt.rent_at_bike.app;


public class Detail {

    private String icon;
    private String name;
    private String text;

    public Detail(String n_icon, String n_name) {
        icon = n_icon;
        name = n_name;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
