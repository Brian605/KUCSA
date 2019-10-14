package com.return0.Kucsa.Models;

public class User {
   private String phoneNumber,registrationNumber,UserName,Course,YearOfStudy,RegDate;
   private int id;

    public User(int id,String phoneNumber,String registrationNumber,String UserName,String Course,String YearOfStudy,String RegDate){
        this.phoneNumber=phoneNumber;
        this.registrationNumber=registrationNumber;
        this.UserName=UserName;
        this.Course=Course;
        this.YearOfStudy=YearOfStudy;
        this.RegDate=RegDate;
        this.id=id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public void setYearOfStudy(String yearOfStudy) {
        YearOfStudy = yearOfStudy;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCourse() {
        return Course;
    }

    public String getYearOfStudy() {
        return YearOfStudy;
    }

    public String getRegDate() {
        return RegDate;
    }
}
