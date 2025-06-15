package com.example.drmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drmeet.Database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule); // Ensure this is your correct XML file name


        setupSlotButton(R.id.btn_morning1, "06:30 To 07:30");
        setupSlotButton(R.id.btn_morning2, "08:30 To 09:00");
        setupSlotButton(R.id.btn_morning3, "09:30 To 12:30");
        setupSlotButton(R.id.btn_morning4, "02:30 To 04:00");
        setupSlotButton(R.id.btn_morning5, "09:30 To 12:30");
        setupSlotButton(R.id.btn_morning6, "02:30 To 04:00");
        setupSlotButton(R.id.btn_morning7, "02:30 To 04:00");
        setupSlotButton(R.id.btn_morning8, "02:30 To 04:00");
        setupSlotButton(R.id.btn_evening1, "07:30 To 08:00");
        setupSlotButton(R.id.btn_evening2, "08:20 To 09:00");
        setupSlotButton(R.id.btn_evening3, "09:00 To 09:30");
        setupSlotButton(R.id.btn_evening4, "10:30 To 11:00");
        setupSlotButton(R.id.btn_evening5, "09:00 To 09:30");
        setupSlotButton(R.id.btn_evening6, "10:30 To 11:00");
        setupSlotButton(R.id.btn_evening7, "10:30 To 11:00");
        setupSlotButton(R.id.btn_evening8, "10:30 To 11:00");
        setNextDayDate();
    }

    private void setupSlotButton(int buttonId, final String slotTime) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper = new DatabaseHelper(ScheduleActivity.this);
                boolean isAvailable = databaseHelper.isTimeAvailable(slotTime); // Check if the time is available

                if (!isAvailable){
                    Toast.makeText(ScheduleActivity.this, "This slot is not available anymore", Toast.LENGTH_SHORT).show();
                }else{
                    databaseHelper.insertTime(slotTime);    // Insert the selected time into the database
                    Intent intent = new Intent(ScheduleActivity.this, PaymentActivity.class);
                    intent.putExtra("slotTime", slotTime);
                    startActivity(intent);
                }
            }
        });
    }
    private void setNextDayDate() {
        TextView tvDate = findViewById(R.id.tv_date);
        // Get instance of Calendar and add 1 day
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        String nextDay = dateFormat.format(calendar.getTime());

        // Set the date in TextView
        tvDate.setText("Available Slots for: " + nextDay);
    }
}

