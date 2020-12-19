package pt.rent_at_bike.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class BikeAdapter extends
        RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageBike;
        public TextView nameBike;
        public TextView detailsBike;
        public FloatingActionButton enterBike;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageBike = (ImageView) itemView.findViewById(R.id.imageBike);
            nameBike = (TextView) itemView.findViewById(R.id.nameBike);
            detailsBike = (TextView) itemView.findViewById(R.id.detailsBike);
            enterBike = (FloatingActionButton) itemView.findViewById(R.id.enterBike);
        }
    }

    // Store a member variable for the contacts
    private List<Bike> mBikes;

    private Context context;

    // Pass in the contact array into the constructor
    public BikeAdapter(List<Bike> bikes) {
        mBikes = bikes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bikeView = inflater.inflate(R.layout.fragment_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bikeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Bike bike = mBikes.get(position);

        // Set item views based on your views and data model
        ImageView iBike = holder.imageBike;
        iBike.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(bike.getProfileImg(), "drawable", context.getPackageName())));

        TextView nBike = holder.nameBike;
        nBike.setText(bike.getName());
        TextView dBike = holder.detailsBike;
        dBike.setText("€"+Integer.toString(bike.getPrice())+"/h - "+ bike.getTypebike());
    }

    @Override
    public int getItemCount() {
        return mBikes.size();
    }
}