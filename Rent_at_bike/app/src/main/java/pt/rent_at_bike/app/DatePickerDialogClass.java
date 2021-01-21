package pt.rent_at_bike.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int year, month, day;
    private boolean bool;

    public DatePickerDialogClass(LocalDate date, boolean type) {
        year = date.getYear();
        month = date.getMonthValue();
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
        if(bool) {
            ((BikeActivity) getActivity()).setStart(LocalDate.of(year, month, day));
        } else {
            ((BikeActivity) getActivity()).setStop(LocalDate.of(year, month, day));
        }
        ((BikeActivity)getActivity()).adapter.notifyDataSetChanged();
    }
}