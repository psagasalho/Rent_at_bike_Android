package pt.rent_at_bike.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BikeActivity extends AppCompatActivity {

    ArrayList<Detail> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bike bike = (Bike) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        RecyclerView rvDetails = (RecyclerView) findViewById(R.id.recyclerView);
        ImageView imageBike = (ImageView) findViewById(R.id.imageBike);
        TextView nameBike = (TextView) findViewById(R.id.nameBike);
        TextView totalBike = (TextView) findViewById(R.id.totalBike);
        FloatingActionButton buyBike = (FloatingActionButton) findViewById(R.id.buy);

        // Initialize contacts
        details.add(new Detail("ic_add","ID", Integer.toString(bike.getId())));
        details.add(new Detail("ic_add","Price", Integer.toString(bike.getPrice())));
        details.add(new Detail("ic_add","Location", "Porto"));
        details.add(new Detail("ic_add","Category", bike.getTypebike()));
        details.add(new Detail("ic_add","Start Day", ""));
        details.add(new Detail("ic_add","Stop Day", ""));

        imageBike.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(bike.getProfileImg(), "drawable", getPackageName())));

        nameBike.setText(bike.getName());
        totalBike.setText("Total: 0€");

        // Create adapter passing in the sample user data
        DetailAdapter_2 adapter = new DetailAdapter_2(details);
        // Attach the adapter to the recyclerview to populate items
        rvDetails.setAdapter(adapter);
        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // That's all!
    }
}