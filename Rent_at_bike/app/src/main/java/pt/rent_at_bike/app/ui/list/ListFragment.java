package pt.rent_at_bike.app.ui.list;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import pt.rent_at_bike.app.bike.BikeAdapter;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;
import pt.rent_at_bike.app.R;

public class ListFragment extends Fragment {

    ArrayList<Bike> bikes = new ArrayList<>();
    private FirebaseFirestore db;
    private CollectionReference colRefBikes;
    private BikeAdapter adapter;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        db = FirebaseFirestore.getInstance();
        colRefBikes = db.collection("/bikes");
        fetchCollection();
        // Lookup the recyclerview in activity layout
        RecyclerView rvBikes = (RecyclerView) root.findViewById(R.id.rvBikes);

        // Create adapter passing in the sample user data
        adapter = new BikeAdapter(bikes);
        // Attach the adapter to the recyclerview to populate items
        rvBikes.setAdapter(adapter);
        // Set layout manager to position the items
        rvBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBikes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // That's all!


        return root;
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