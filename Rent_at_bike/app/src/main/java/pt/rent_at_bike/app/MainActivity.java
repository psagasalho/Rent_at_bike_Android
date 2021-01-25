package pt.rent_at_bike.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import pt.rent_at_bike.app.bike.Bike;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "pt.ua.cm.rent_at_bike.MESSAGE";

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

    public void showBike(Bike bike) {
        Intent intent = new Intent(MainActivity.this, BikeActivity.class);
        intent.putExtra(EXTRA_MESSAGE, bike);
        startActivity(intent);
    }

    public void scanQRcode() {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
    }
}