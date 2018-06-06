package com.example.mani.studentapp.AttendanceRelated;

public class PastAttendance {

    String name;
    int status;

    public PastAttendance(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

