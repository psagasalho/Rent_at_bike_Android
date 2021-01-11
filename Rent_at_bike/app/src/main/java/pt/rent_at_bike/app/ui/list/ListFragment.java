package pt.rent_at_bike.app.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.bike.BikeAdapter;
import pt.rent_at_bike.app.bike.Bike;
import pt.rent_at_bike.app.bike.LatLon;
import pt.rent_at_bike.app.R;

public class ListFragment extends Fragment {

    ArrayList<Bike> bikes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvBikes = (RecyclerView) root.findViewById(R.id.rvBikes);

        // Initialize contacts
        bikes.add(new Bike(1, "Bike 1", "rockrider_e_st", "Electric", 20, new LatLon(41.15716181008212, -8.659623235501876), true));
        bikes.add(new Bike(2, "Bike 2", "cannondale_caadx", "Electric", 20, new LatLon(41.14152128955986, -8.637350172004071), true));
        bikes.add(new Bike(3, "Bike 3", "btwin_hoptown", "Electric", 20, new LatLon(41.15716181008212, -8.659623235501876), true));
        bikes.add(new Bike(4, "Bike 4", "specialized_tarmac", "Electric", 20, new LatLon(41.14152128955986, -8.637350172004071), true));
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