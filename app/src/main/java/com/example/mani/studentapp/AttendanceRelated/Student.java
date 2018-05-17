package com.example.mani.studentapp.AttendanceRelated;

public class Student {

    private int student_id;
    private int student_roll_no;
    private String name;
    private boolean isPresent;

    public Student(int student_id,int student_roll_no, String name) {
        this.student_id = student_id;
        this.student_roll_no = student_roll_no;
        this.name = name;
        this.isPresent = false;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public int getStudent_roll_no() {
        return student_roll_no;
    }

    public void setStudent_roll_no(int student_roll_no) {
        this.student_roll_no = student_roll_no;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
