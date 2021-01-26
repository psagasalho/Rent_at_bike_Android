package pt.rent_at_bike.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;
import pt.rent_at_bike.app.history.History;

public class BuyActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference colRefBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final History hist = (History) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        db = FirebaseFirestore.getInstance();
        colRefBikes = db.collection("/bikes");

        Map<String, Object> history = new HashMap<>();
        history.put("bikeID", hist.getBikeID());
        history.put("histID", hist.getHistID());
        history.put("priceTotal", hist.getPriceTotal());
        history.put("start", hist.getStart().toString());
        history.put("stop", hist.getStop().toString());
        history.put("userEmail", hist.getUserEmail());

        // Add a new document with a generated ID
        FirebaseFirestore.getInstance().collection("/history")
                .add(history)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(BuyActivity.this, "Bike pay success!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("ERROR");
                    }
                });
        DocumentReference ref = FirebaseFirestore.getInstance().collection("/bikes").document();
        ref.update("available", false ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Updated");
            }
        });

        colRefBikes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Map<String,Object> databasebikes = document.getData();
                        System.out.println(document.getId() + "\n" + databasebikes + "\n\n");
                        if((long)databasebikes.get("id")== hist.getBikeID()){
                            DocumentReference ref = FirebaseFirestore.getInstance().collection("/bikes").document(document.getId());
                            ref.update("available", false ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(BuyActivity.this, "Availability changed Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                } else {
                    System.out.println("Error");
                }
            }
        });



        FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}