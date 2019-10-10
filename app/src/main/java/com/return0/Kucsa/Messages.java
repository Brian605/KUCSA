package com.return0.Kucsa;

public class Messages {
    private String message,status,category,phoneNumber;
    private int id;

    public Messages(int id,String message,String status,String category,String phoneNumber){
        this.id=id;
        this.category=category;
        this.message=message;
        this.status=status;
        this.phoneNumber=phoneNumber;

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
