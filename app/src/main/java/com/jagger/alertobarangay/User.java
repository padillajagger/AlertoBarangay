package com.jagger.alertobarangay;

public class User {
    public String name;
    public String email;
    public String mobile_no;
    public String address;
    public String password;
    public String gender;
    public String date_of_birth;
    public String age;

    public User(String name, String email, String mobile_no, String address, String password, String age, String gender, String date_of_birth) {
        this.name = name;
        this.email = email;
        this.mobile_no = mobile_no;
        this.address = address;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }
}

