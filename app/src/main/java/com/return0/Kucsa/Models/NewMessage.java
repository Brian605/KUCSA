package com.return0.Kucsa.Models;

public class NewMessage {
    private String number,name,year;
    private boolean isSelected;

    public NewMessage(String number, String name, String year, boolean isSelected){
      this.number=number;
      this.name=name;
      this.year=year;
      this.isSelected=isSelected;

    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setIselected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getYear() {
        return year;
    }
    public boolean getIselected(){
        return isSelected;
    }

}
