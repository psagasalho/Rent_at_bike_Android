package pt.rent_at_bike.app.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.R;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DetailAdapter_2 extends
        RecyclerView.Adapter<DetailAdapter_2.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView iconDetail;
        public EditText textDetail;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            iconDetail = (ImageView) itemView.findViewById(R.id.iconDetail);
            textDetail = (EditText) itemView.findViewById(R.id.textDetail);
        }
    }

    // Store a member variable for the contacts
    private List<Detail> mDetails;

    private Context context;

    // Pass in the contact array into the constructor
    public DetailAdapter_2(List<Detail> details) {
        mDetails = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View detailView = inflater.inflate(R.layout.fragment_detail, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(detailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Detail detail = mDetails.get(position);

        // Set item views based on your views and data model
        ImageView iDetail = holder.iconDetail;
        iDetail.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(detail.getIcon(), "drawable", context.getPackageName())));

        EditText tDetail = holder.textDetail;
        tDetail.setHint(detail.getName());

    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }
}
