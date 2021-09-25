package com.example.android.otpverification;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentBooking extends AppCompatActivity {

    private TimePicker timePicker;
    private Button bookAppointmentButton;
    private TextView serviceNameField;

    FirebaseDatabase database;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_booking_activity);

        timePicker = findViewById(R.id.timePicker1);
        bookAppointmentButton = findViewById(R.id.book_appointment_button);
        serviceNameField = findViewById(R.id.service_name_field);

        serviceNameField.setText(getIntent().getStringExtra("serviceName"));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Appointments");

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String appointmentTime = hour + ":" + minute;

        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
