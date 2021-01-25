package pt.rent_at_bike.app.ui.add;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AddFragment extends Fragment {

    ArrayList<Detail> details = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        RecyclerView rvDetails = (RecyclerView) root.findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) root.findViewById(R.id.imageBike);
        Button buttonAdd = (Button) root.findViewById(R.id.buttonAdd);

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
}