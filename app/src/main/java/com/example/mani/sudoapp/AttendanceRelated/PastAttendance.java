package com.example.mani.sudoapp.AttendanceRelated;

public class PastAttendance {

    private int attendance_id;
    private int roll_no;
    private String name;
    private boolean status;

    public PastAttendance(int attendance_id,int roll_no, String name, int status) {
        this.attendance_id = attendance_id;
        this.roll_no = roll_no;
        this.name = name;
        if(status == 0)
            this.status = false;
        else
            this.status = true;

    }

    public int getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(int attendance_id) {
        this.attendance_id = attendance_id;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(int roll_no) {
        this.roll_no = roll_no;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

