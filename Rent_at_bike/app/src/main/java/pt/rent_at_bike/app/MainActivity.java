package pt.rent_at_bike.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "pt.ua.cm.rent_at_bike.MESSAGE";
    ArrayList<Bike> bikes = new ArrayList<>();
    private FirebaseFirestore db;
    private CollectionReference colRefBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        ImageView imgFavorite = (ImageView) findViewById(R.id.login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            imgFavorite.setImageResource(R.drawable.profilegreen);
            imgFavorite.setClickable(true);
            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            imgFavorite.setImageResource(R.drawable.profilered);
            imgFavorite.setClickable(true);
            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public ArrayList<Bike> getBikes() {
        db = FirebaseFirestore.getInstance();
        colRefBikes = db.collection("/bikes");
        fetchCollection();
        return bikes;
    }

    public void showBike(Bike bike) {
        Intent intent = new Intent(MainActivity.this, BikeActivity.class);
        intent.putExtra(EXTRA_MESSAGE, bike);
        startActivity(intent);
    }

    public void scanQRcode() {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
    }

    private void fetchCollection() {
        colRefBikes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Map<String,Object> databasebikes = document.getData();
                        System.out.println(document.getId() + "\n" + databasebikes + "\n\n");
                        if((boolean)databasebikes.get("available")== true){
                            GeoPoint point = (GeoPoint) databasebikes.get("loc");
                            LatLon lt = new LatLon(point.getLatitude(),point.getLongitude());
                            bikes.add(new Bike((long)databasebikes.get("id"),(String)databasebikes.get("name"),(String)databasebikes.get("profileImg"),
                                    (String)databasebikes.get("typebike"),(long)databasebikes.get("price"),lt,(boolean)databasebikes.get("available")));

                            Log.e("ListFragment", " new bike added from firestore :::::  " +databasebikes.get("profileImg"));
                        }

                    }
                } else {
                    System.out.println("Error");
                }
            }
        });
    }
}