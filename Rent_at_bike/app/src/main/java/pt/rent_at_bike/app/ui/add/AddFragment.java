package pt.rent_at_bike.app.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.R;
import pt.rent_at_bike.app.detail.Detail;
import pt.rent_at_bike.app.detail.DetailAdapter;

public class AddFragment extends Fragment {

    ArrayList<Detail> details = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        RecyclerView rvDetails = (RecyclerView) root.findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) root.findViewById(R.id.imageBike);
        Button buttonAdd = (Button) root.findViewById(R.id.buttonAdd);

        // Initialize contacts
        details.add(new Detail("ic_add","Name"));
        details.add(new Detail("ic_add","Type"));
        details.add(new Detail("ic_add","€ /Day"));
        details.add(new Detail("ic_add","Lat"));
        details.add(new Detail("ic_add","Lon"));

        imageBike.setImageDrawable(getResources().getDrawable(R.drawable.btwin_hoptown));

        // Create adapter passing in the sample user data
        DetailAdapter adapter = new DetailAdapter(details);
        // Attach the adapter to the recyclerview to populate items
        rvDetails.setAdapter(adapter);
        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // That's all!

        return root;
    }
}