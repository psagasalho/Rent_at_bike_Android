package pt.rent_at_bike.app.ui.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Map;

import pt.rent_at_bike.app.BikeActivity;
import pt.rent_at_bike.app.LoginActivity;
import pt.rent_at_bike.app.MainActivity;
import pt.rent_at_bike.app.R;
import pt.rent_at_bike.app.RegisterActivity;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.BikeAdapter;
import pt.rent_at_bike.app.bike.LatLon;

public class MapFragment extends Fragment implements View.OnClickListener {

    MapView mMapView;
    private GoogleMap googleMap;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;

    private FirebaseFirestore db;
    private CollectionReference colRefBikes;

    public static final String EXTRA_MESSAGE = "pt.ua.cm.rent_at_bike.MESSAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        db = FirebaseFirestore.getInstance();
        colRefBikes = db.collection("/bikes");

        FloatingActionButton locationButton = (FloatingActionButton) root.findViewById(R.id.location);
        locationButton.setOnClickListener(this);
        FloatingActionButton qrcodeButton = (FloatingActionButton) root.findViewById(R.id.qrcode);
        qrcodeButton.setOnClickListener(this);

        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;
                location();
                colRefBikes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    ArrayList<Bike> bikes= new ArrayList<Bike>();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for ( DocumentSnapshot document : task.getResult()) {
                                final Map<String,Object> databasebikes = document.getData();
                                if((boolean)databasebikes.get("available")== true){
                                    GeoPoint point = (GeoPoint) databasebikes.get("loc");
                                    final LatLon lt = new LatLon(point.getLatitude(),point.getLongitude());
                                    bikes.add(new Bike((long)databasebikes.get("id"),(String)databasebikes.get("name"),(String)databasebikes.get("profileImg"),
                                            (String)databasebikes.get("typebike"),(long)databasebikes.get("price"),lt,(boolean)databasebikes.get("available")));
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lt.getLat(),lt.getLon())));

                                }
                            }
                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    for(Bike b : bikes){
                                        if(b.getLoc().getLon()==marker.getPosition().longitude && b.getLoc().getLat()==marker.getPosition().latitude){
                                            Intent intent = new Intent(getContext(), BikeActivity.class);
                                            intent.putExtra(EXTRA_MESSAGE, b);
                                            startActivity(intent);

                                        }
                                    }
                                    return true;
                                }
                            });
                        } else {
                            System.out.println("Error");
                        }
                    }
                });


            }
        });

        return root;
    }


    public void location() {
        checkLocationPermission();
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
                                LatLng locat = new LatLng(location.getLatitude(), location.getLongitude());
                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(locat).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                            //Log.v("LOCATION", "Lat: "+Double.toString(location.getLatitude())+" Lon: "+Double.toString(location.getLongitude()));
                        }
                    });
        }
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


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location:
                location();
                break;
            case R.id.qrcode:
                ((MainActivity) getContext()).scanQRcode();
                break;
        }
    }

}