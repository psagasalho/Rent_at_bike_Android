package pt.rent_at_bike.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private LocalDate date;
    private int year, month, day;
    private boolean bool;

    public DatePickerDialogClass(LocalDate date, boolean type) {
        this.date = date;
        year = date.getYear();
        month = date.getMonthValue()-1;
        day = date.getDayOfMonth();

        bool = type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

        return datepickerdialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        LocalDate start =  ((BikeActivity) getActivity()).getStart();
        LocalDate stop =  ((BikeActivity) getActivity()).getStop();
        LocalDate res = LocalDate.of(year, month+1, day);
        if(bool) {
            if (res.isBefore(LocalDate.now())) {
                ((BikeActivity) getActivity()).setStart(LocalDate.now());
            } else if (res.isAfter(stop)) {
                ((BikeActivity) getActivity()).setStart(res);
                ((BikeActivity) getActivity()).setStop(res.plusDays(1));
            } else {
                ((BikeActivity) getActivity()).setStart(res);
            }
        } else {
            if (res.isBefore(LocalDate.now())) {
                ((BikeActivity) getActivity()).setStart(LocalDate.now());
                ((BikeActivity) getActivity()).setStop(LocalDate.now().plusDays(1));
            } else if (res.isBefore(start)) {
                ((BikeActivity) getActivity()).setStop(start.plusDays(1));
            } else {
                ((BikeActivity) getActivity()).setStop(res);
            }
        }
        ((BikeActivity)getActivity()).adapter.notifyDataSetChanged();
        long days = ChronoUnit.DAYS.between(((BikeActivity) getActivity()).getStart(), ((BikeActivity) getActivity()).getStop());
        ((BikeActivity)getActivity()).totalBike.setText("Total: "+ days*((BikeActivity)getActivity()).price +"€");
    }
}