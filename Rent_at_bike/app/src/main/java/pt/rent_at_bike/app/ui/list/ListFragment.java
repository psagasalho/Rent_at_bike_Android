package pt.rent_at_bike.app.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.Bike;
import pt.rent_at_bike.app.BikeAdapter;
import pt.rent_at_bike.app.LatLon;
import pt.rent_at_bike.app.R;

public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    ArrayList<Bike> bikes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listViewModel =
                ViewModelProviders.of(this).get(ListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvBikes = (RecyclerView) root.findViewById(R.id.rvBikes);

        // Initialize contacts
        bikes.add(new Bike(1, "Bike 1", "rockrider_e_st", "Electric", 20, new LatLon(0,0), true));
        bikes.add(new Bike(2, "Bike 2", "cannondale_caadx", "Electric", 20, new LatLon(0,0), true));
        bikes.add(new Bike(3, "Bike 3", "btwin_hoptown", "Electric", 20, new LatLon(0,0), true));
        bikes.add(new Bike(4, "Bike 4", "specialized_tarmac", "Electric", 20, new LatLon(0,0), true));
        // Create adapter passing in the sample user data
        BikeAdapter adapter = new BikeAdapter(bikes);
        // Attach the adapter to the recyclerview to populate items
        rvBikes.setAdapter(adapter);
        // Set layout manager to position the items
        rvBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBikes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // That's all!

        return root;
    }
}