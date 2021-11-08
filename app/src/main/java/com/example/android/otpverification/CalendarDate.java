package com.example.android.otpverification;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class CalendarDate {

    private String dayName;
    private String date;
    private String monthName;

    public CalendarDate(String dayName, String date, String monthName) {
        this.dayName = dayName;
        this.date = date;
        this.monthName = monthName;
    }

    public String getDayName() {
        return dayName;
    }

    public String getDate() {
        return date;
    }

    public String getMonthName() {
        return monthName;
    }

    public boolean equals(CalendarDate item){
        return dayName.equals(item.dayName) && date == item.date;
    }

    public static DiffUtil.ItemCallback<CalendarDate> itemCallback = new DiffUtil.ItemCallback<CalendarDate>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
            return oldItem.dayName.equals(newItem.dayName);
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
            return oldItem.equals(newItem);
        }
    };
}
