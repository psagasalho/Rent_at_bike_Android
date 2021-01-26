package pt.rent_at_bike.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;
import pt.rent_at_bike.app.detail.Detail;
import pt.rent_at_bike.app.detail.DetailBikeAdapter;
import pt.rent_at_bike.app.history.History;

public class BikeActivity extends AppCompatActivity {

    ArrayList<Detail> details = new ArrayList<>();
    public RecyclerView rvDetails;
    public DetailBikeAdapter adapter;
    public TextView totalBike;

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    LocalDate localDate = LocalDate.now();

    public LocalDate start = localDate;
    public LocalDate stop = localDate.plusDays(1);
    public long price;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference colRefHistory;
    private ArrayList<History> histories = new ArrayList<>();

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

    public static final String EXTRA_MESSAGE = "pt.ua.cm.rent_at_bike.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        colRefHistory = db.collection("/history");
        fetchCollectionHistory();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final Bike bike = (Bike) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        rvDetails = (RecyclerView) findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) findViewById(R.id.imageBike);
        TextView nameBike = (TextView) findViewById(R.id.nameBike);
        totalBike = (TextView) findViewById(R.id.totalBike);
        FloatingActionButton buyBike = (FloatingActionButton) findViewById(R.id.buy);
        FloatingActionButton qrcode = (FloatingActionButton) findViewById(R.id.qrcodeCreate);

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert_dialog, null);

                ImageView imageView= (ImageView) dialogLayout.findViewById(R.id.selectedImage);
                imageView.setImageBitmap(getQRcode((int) bike.getId()));
                new AlertDialog.Builder(BikeActivity.this)
                        .setView(dialogLayout)
                        .show();
            }
        });

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
        details.add(new Detail("ic_info","ID", Long.toString(bike.getId())));
        details.add(new Detail("ic_money","Price", Long.toString(bike.getPrice())));
        details.add(new Detail("ic_location","Location", cityName));
        details.add(new Detail("ic_category","Category", bike.getTypebike()));
        details.add(new Detail("ic_time","Start Day", ""));
        details.add(new Detail("ic_time_2","Stop Day", ""));

        imageBike.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(bike.getProfileImg(), "drawable", getPackageName())));

        price = bike.getPrice();
        long days = ChronoUnit.DAYS.between(start, stop);
        nameBike.setText(bike.getName());
        totalBike.setText("Total: "+ days*bike.getPrice() +"€");

        // Create adapter passing in the sample user data
        adapter = new DetailBikeAdapter(details);
        // Attach the adapter to the recyclerview to populate items
        rvDetails.setAdapter(adapter);
        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // That's all!

        ImageView imgFavorite = (ImageView) findViewById(R.id.login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            imgFavorite.setImageResource(R.drawable.profilegreen);
            imgFavorite.setClickable(true);
            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            imgFavorite.setImageResource(R.drawable.profilered);
            imgFavorite.setClickable(true);
            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        buyBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    History hist = new History(histories.size()+1, email, bike.getId(), days*bike.getPrice(), getStart(), getStop());
                    Intent intent = new Intent(BikeActivity.this, BuyActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, hist);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BikeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public Bitmap getQRcode(int data) {

        Bitmap bitmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        QRGEncoder qrgEncoder = new QRGEncoder(Integer.toString(data), null, QRGContents.Type.TEXT, 500);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {}
        return bitmap;
    }

    private void fetchCollectionHistory() {
        colRefHistory.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Map<String,Object> databasehistories = document.getData();
                        if(databasehistories.get("userEmail").equals(mAuth.getCurrentUser().getEmail())){
                            histories.add(new History((long)databasehistories.get("histID"),(String)databasehistories.get("userEmail"),(long)databasehistories.get("bikeID"),
                                    (long)databasehistories.get("priceTotal"),LocalDate.now(), LocalDate.now()));

                            adapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    System.out.println("Error");
                }
            }
        });
    }

}