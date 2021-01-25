package pt.rent_at_bike.app.ui.add;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import pt.rent_at_bike.app.MainActivity;
import pt.rent_at_bike.app.ProfileActivity;
import pt.rent_at_bike.app.R;
import pt.rent_at_bike.app.RegisterActivity;
import pt.rent_at_bike.app.detail.Detail;
import pt.rent_at_bike.app.detail.DetailAddAdapter;
import pt.rent_at_bike.app.ui.map.MapFragment;

public class AddFragment extends Fragment {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;
    ArrayList<Detail> details = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        RecyclerView rvDetails = (RecyclerView) root.findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) root.findViewById(R.id.imageBike);
        Button buttonAdd = (Button) root.findViewById(R.id.buttonAdd);
        Button buttonLocation = (Button) root.findViewById(R.id.buttonLocation);

        // Initialize contacts
        details.add(new Detail("ic_info","Name"));
        details.add(new Detail("ic_category","Type"));
        details.add(new Detail("ic_money","€ /Day"));
        details.add(new Detail("ic_location","Lat"));
        details.add(new Detail("ic_location","Lon"));



        imageBike.setImageDrawable(getResources().getDrawable(R.drawable.btwin_hoptown));

        // Create adapter passing in the sample user data
        final DetailAddAdapter adapter = new DetailAddAdapter(details);

        // Attach the adapter to the recyclerview to populate items
        rvDetails.setAdapter(adapter);


        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // That's all!

        buttonLocation.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              location(adapter);
          }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.tValues.size()==5){
                    Map<String, Object> bikes = new HashMap<>();
                    bikes.put("available", true);
                    bikes.put("id", 9);
                    bikes.put("loc", new GeoPoint(Double.parseDouble(adapter.tValues.get(3)),Double.parseDouble(adapter.tValues.get(4))));
                    bikes.put("name", adapter.tValues.get(0));
                    bikes.put("price", Long.parseLong(adapter.tValues.get(2)));
                    bikes.put("profileImg", "rockrider_e_st");
                    bikes.put("typebike", adapter.tValues.get(1));

                    // Add a new document with a generated ID
                    FirebaseFirestore.getInstance().collection("/bikes")
                            .add(bikes)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(), "Bike added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("ERROR");
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Empty Parameters!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission")
                        .setMessage("Location Permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void location(final DetailAddAdapter adapter) {
        checkLocationPermission();
        final GeoPoint[] geo = new GeoPoint[1];
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //geo[0] = new GeoPoint(location.getLatitude(), location.getLongitude());
                                adapter.tValues.put(3,String.valueOf(location.getLatitude()));
                                adapter.tValues.put(4,String.valueOf(location.getLongitude()));
                                adapter.notifyDataSetChanged();
                            }
                            //Log.v("LOCATION", "Lat: "+Double.toString(location.getLatitude())+" Lon: "+Double.toString(location.getLongitude()));
                        }
                    });
        }
    }
}