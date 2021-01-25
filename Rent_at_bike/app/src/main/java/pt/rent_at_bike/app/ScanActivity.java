package pt.rent_at_bike.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import info.androidhive.barcode.BarcodeReader;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    ArrayList<Bike> bikes = new ArrayList<>();
    private FirebaseFirestore db;
    private CollectionReference colRefBikes;
    public static final String EXTRA_MESSAGE = "pt.ua.cm.rent_at_bike.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.cancel);

        db = FirebaseFirestore.getInstance();
        colRefBikes = db.collection("/bikes");
        fetchCollection();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onScanned(Barcode barcode) {
        try {
            Long res = Long.parseLong(barcode.displayValue);
            Bike bike = bikes.stream()
                    .filter(b -> res == b.getId())
                    .findAny()
                    .orElse(null);
            if (bike != null) {
                Intent intent = new Intent(ScanActivity.this, BikeActivity.class);
                intent.putExtra(EXTRA_MESSAGE, bike);
                startActivity(intent);
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ScanActivity.this, "Invalid Bike Code!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ScanActivity.this, "Invalid Bike Code!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}