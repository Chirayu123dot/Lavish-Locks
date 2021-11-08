package com.example.android.otpverification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentBooking extends AppCompatActivity {

    private TimePicker timePicker;
    private Button bookAppointmentButton;
    ImageView backButton, userProfileButton;
    private TextView serviceNameField;
    private dateOnClickInterface onClickInterface;

    private ArrayList<CalendarDate> horizontalCalendarDates = new ArrayList<>();

    private String dateSelected, daySelected, monthSelected;

    FirebaseDatabase database;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_booking_activity);

        timePicker = findViewById(R.id.appointment_booking_activity_timePicker);
        backButton = findViewById(R.id.appointment_booking_activity_back_button);
        userProfileButton = findViewById(R.id.appointment_booking_activity_user_profile_button);
        bookAppointmentButton = findViewById(R.id.appointment_booking_activity_booking_button);
        serviceNameField = findViewById(R.id.appointment_booking_activity_service_name_field);

        serviceNameField.setText(getIntent().getStringExtra("serviceName"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Appointments");

        TextView month = findViewById(R.id.appointment_booking_activity_month_name_field);
        Calendar calendar = Calendar.getInstance();

        month.setText(getMonthName(calendar.get(Calendar.MONTH)));

        getDrawable(R.drawable.selected_date_background);
        horizontalCalendarDates.add(new CalendarDate(getDayName(calendar.get(Calendar.DAY_OF_WEEK)), ""+calendar.get(Calendar.DATE),
                getMonthName(calendar.get(Calendar.MONTH))));

        for(int i=0 ; i<14 ; i++){
            calendar.add(Calendar.DATE, 1);
            horizontalCalendarDates.add(new CalendarDate(getDayName(calendar.get(Calendar.DAY_OF_WEEK)), ""+calendar.get(Calendar.DATE),
                    getMonthName(calendar.get(Calendar.MONTH))));
        }

        onClickInterface = new dateOnClickInterface() {
            @Override
            public void setClick(int position) {
                daySelected = horizontalCalendarDates.get(position).getDayName();
                monthSelected = horizontalCalendarDates.get(position).getMonthName();
                dateSelected = horizontalCalendarDates.get(position).getDate();

                Log.v("AppointmentBooking", daySelected + dateSelected + " of " + monthSelected);
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        CalendarAdapter calendarAdapter = new CalendarAdapter(this, CalendarDate.itemCallback, month, onClickInterface);
        recyclerView.setAdapter(calendarAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        calendarAdapter.submitList(horizontalCalendarDates);


        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();

                String timeSelected = getTime(hour, min);
                String appointmentTime = daySelected + dateSelected + " of " + monthSelected + " (" + timeSelected + ")";

                Appointment newAppointment = new Appointment(getIntent().getStringExtra("userName"),
                        getIntent().getStringExtra("userPhoneNumber"),
                        appointmentTime);

                reference.child(getIntent().getStringExtra("serviceName"))
                        .child(getIntent().getStringExtra("userPhoneNumber"))
                        .setValue(newAppointment);

                Toast.makeText(AppointmentBooking.this, "Booking Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private String getDayName(int dayNumber){
        switch (dayNumber){
            case Calendar.SUNDAY   : return "Sun";
            case Calendar.MONDAY   : return "Mon";
            case Calendar.TUESDAY  : return "Tue";
            case Calendar.WEDNESDAY: return "Wed";
            case Calendar.THURSDAY : return "Thu";
            case Calendar.FRIDAY   : return "Fri";
            case Calendar.SATURDAY : return "Sat";
            default                : return "Error";
        }
    }

    private String getMonthName(int monthNumber){
        switch(monthNumber){
            case Calendar.JANUARY  : return "January";
            case Calendar.FEBRUARY : return "February";
            case Calendar.MARCH    : return "March";
            case Calendar.APRIL    : return "April";
            case Calendar.MAY      : return "May";
            case Calendar.JUNE     : return "June";
            case Calendar.JULY     : return "July";
            case Calendar.AUGUST   : return "August";
            case Calendar.SEPTEMBER: return "September";
            case Calendar.OCTOBER  : return "October";
            case Calendar.NOVEMBER : return "November";
            case Calendar.DECEMBER : return "December";
            default                : return "Error";
        }
    }

    public String getTime(int hour, int min) {

        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return hour + " : " + min + " " + format;

    }
}
