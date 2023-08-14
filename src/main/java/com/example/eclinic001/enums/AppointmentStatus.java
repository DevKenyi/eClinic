package com.example.eclinic001.enums;

public enum AppointmentStatus {
    Scheduled("Scheduled"),
    InProcess("In Progress"),
    Completed("Completed"),
    Cancelled("Cancelled"),
    Missed("Missed"),
    Rescheduled("Rescheduled"),
    Pending("Pending");

    private String status;

    AppointmentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
