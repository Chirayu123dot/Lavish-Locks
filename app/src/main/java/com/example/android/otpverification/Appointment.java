package com.example.android.otpverification;

public class Appointment {
    private String userName;
    private String userPhoneNumber;
    private String appointmentTime;

    public Appointment(){}

    public Appointment(String userName, String userPhoneNumber, String appointmentTime) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.appointmentTime = appointmentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
