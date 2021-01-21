package pt.rent_at_bike.app;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;
import pt.rent_at_bike.app.detail.Detail;
import pt.rent_at_bike.app.detail.DetailAdapter;

public class BikeActivity extends AppCompatActivity {

    ArrayList<Detail> details = new ArrayList<>();
    public RecyclerView rvDetails;
    public DetailAdapter adapter;

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    LocalDate localDate = LocalDate.now();

    public LocalDate start = localDate;
    public LocalDate stop = localDate;

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setStop(LocalDate stop) {
        this.stop = stop;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getStop() {
        return stop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bike bike = (Bike) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        rvDetails = (RecyclerView) findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) findViewById(R.id.imageBike);
        TextView nameBike = (TextView) findViewById(R.id.nameBike);
        TextView totalBike = (TextView) findViewById(R.id.totalBike);
        FloatingActionButton buyBike = (FloatingActionButton) findViewById(R.id.buy);

        LatLon loc = bike.getLoc();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(loc.getLat(), loc.getLon(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        int fIndex = cityName.indexOf(',')+2;
        int lIndex = cityName.lastIndexOf(',');
        cityName = cityName.substring(fIndex, lIndex); // Remove street and country
        cityName = cityName.substring(cityName.indexOf(' ')+1); // Remove postal code

        Log.v("LOC",addresses.get(0).toString());
        // Initialize contacts
        details.add(new Detail("ic_add","ID", Long.toString(bike.getId())));
        details.add(new Detail("ic_add","Price", Long.toString(bike.getPrice())));
        details.add(new Detail("ic_add","Location", cityName));
        details.add(new Detail("ic_add","Category", bike.getTypebike()));
        details.add(new Detail("ic_add","Start Day", ""));
        details.add(new Detail("ic_add","Stop Day", ""));

        imageBike.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(bike.getProfileImg(), "drawable", getPackageName())));

        nameBike.setText(bike.getName());
        totalBike.setText("Total: 0€");

        // Create adapter passing in the sample user data
        adapter = new DetailAdapter(details);
        // Attach the adapter to the recyclerview to populate items
        rvDetails.setAdapter(adapter);
        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // That's all!

        ImageView imgFavorite = (ImageView) findViewById(R.id.login);
        imgFavorite.setClickable(true);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BikeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}