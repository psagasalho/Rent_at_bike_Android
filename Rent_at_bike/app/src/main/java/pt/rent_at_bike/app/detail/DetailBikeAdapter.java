package pt.rent_at_bike.app.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.BikeActivity;
import pt.rent_at_bike.app.DatePickerDialogClass;
import pt.rent_at_bike.app.R;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DetailBikeAdapter extends
        RecyclerView.Adapter<DetailBikeAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView iconDetail;
        public TextView nameDetail;
        public TextView textDetail;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            iconDetail = (ImageView) itemView.findViewById(R.id.iconDetail2);
            nameDetail = (TextView) itemView.findViewById(R.id.nameDetail2);
            textDetail = (TextView) itemView.findViewById(R.id.textDetail2);
        }
    }

    // Store a member variable for the contacts
    private List<Detail> mDetails;

    private Context context;

    // Pass in the contact array into the constructor
    public DetailBikeAdapter(List<Detail> details) {
        mDetails = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View detailView = inflater.inflate(R.layout.fragment_bike_detail, parent, false);

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

        TextView nDetail = holder.nameDetail;
        nDetail.setText(detail.getName());

        final LocalDate start = ((BikeActivity) context).getStart();
        final LocalDate stop = ((BikeActivity) context).getStop();

        final TextView tDetail = holder.textDetail;
        if (detail.getName().equals("Price")) {
            tDetail.setText(detail.getText() + "€ /day");
        } else if (detail.getName().equals("Start Day")) {
            tDetail.setText(start.toString());
            tDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialogfragment = new DatePickerDialogClass(start, true);
                    dialogfragment.show(((BikeActivity) context).getSupportFragmentManager(), "Date Picker Dialog");
                }
            });
        } else if (detail.getName().equals("Stop Day")) {
            tDetail.setText(stop.toString());
            tDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialogfragment = new DatePickerDialogClass(stop, false);
                    dialogfragment.show(((BikeActivity) context).getSupportFragmentManager(), "Date Picker Dialog");
                }
            });
        }else {
            tDetail.setText(detail.getText());
        }
    }

    public Date date() {
        return new Date();
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }
}


