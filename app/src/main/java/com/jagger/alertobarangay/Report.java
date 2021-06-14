package com.jagger.alertobarangay;

public  class Report {

    public Report(String date, double lon, double lat, String comments, String type,String mobile_no,String status) {
        this.lon = lon;
        this.lat = lat;
        this.comments = comments;
        this.type = type;
        this.mobile_no = mobile_no;
        this.date = date;
        this.status = status;
    }

    public double lon;
    public double lat;
    public String comments;
    public  String type;
    public  String mobile_no;
    public String date;
    public String status;
    public String id;

    public Report() {}
}