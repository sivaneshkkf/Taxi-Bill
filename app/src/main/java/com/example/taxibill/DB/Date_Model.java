package com.example.taxibill.DB;

public class Date_Model {
     private String date;
     private String month;
     private String year;
     private String dateObj;
     private String monthTxt;
     private String day;
     private String dateTxt;

    public Date_Model(String date, String month, String year, String dateObj, String monthTxt, String day, String dateTxt) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.dateObj = dateObj;
        this.monthTxt = monthTxt;
        this.day = day;
        this.dateTxt = dateTxt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDateObj() {
        return dateObj;
    }

    public void setDateObj(String dateObj) {
        this.dateObj = dateObj;
    }

    public String getMonthTxt() {
        return monthTxt;
    }

    public void setMonthTxt(String monthTxt) {
        this.monthTxt = monthTxt;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public void setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
    }
}
