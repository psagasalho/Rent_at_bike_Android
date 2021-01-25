package pt.rent_at_bike.app.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.R;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class HistoryAdapter extends
        RecyclerView.Adapter<pt.rent_at_bike.app.history.HistoryAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView histID;
        public TextView bikeID;
        public TextView price;
        public TextView begin;
        public TextView end;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            histID = (TextView) itemView.findViewById(R.id.histID);
            bikeID = (TextView) itemView.findViewById(R.id.bikeID);
            price = (TextView) itemView.findViewById(R.id.price);
            begin = (TextView) itemView.findViewById(R.id.begin);
            end = (TextView) itemView.findViewById(R.id.end);
        }
    }

    // Store a member variable for the contacts
    private List<History> mHistory;

    private Context context;

    // Pass in the contact array into the constructor
    public HistoryAdapter(List<History> details) {
        mHistory = details;
    }

    @NonNull
    @Override
    public pt.rent_at_bike.app.history.HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View detailView = inflater.inflate(R.layout.fragment_history, parent, false);

        // Return a new holder instance
        pt.rent_at_bike.app.history.HistoryAdapter.ViewHolder viewHolder = new pt.rent_at_bike.app.history.HistoryAdapter.ViewHolder(detailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull pt.rent_at_bike.app.history.HistoryAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        History history = mHistory.get(position);

        // Set item views based on your views and data model
        TextView i_histID = holder.histID;
        i_histID.setText(Long.toString(history.getHistID()));

        TextView i_bikeID = holder.bikeID;
        i_bikeID.setText(Long.toString(history.getBikeID()));

        TextView i_price = holder.price;
        i_price.setText(Long.toString(history.getPriceTotal())+"€");

        TextView i_begin = holder.begin;
        i_begin.setText(history.getStart().toString());

        TextView i_end = holder.end;
        i_end.setText(history.getStop().toString());

    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }
}
