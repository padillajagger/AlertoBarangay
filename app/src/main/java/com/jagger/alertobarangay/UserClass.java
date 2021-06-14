package com.jagger.alertobarangay;

public class UserClass {
    String name,address,email,password,phone,age,gender,dateofbirth,voters;

    public UserClass(String name, String email, String address, String phone, String age, String gender, String dateofbirth, String password, String voters) {

        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.voters = voters;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return phone;
    }

    public void setMobile_no(String mobile_no) {
        this.phone = mobile_no;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return dateofbirth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.dateofbirth = date_of_birth;
    }

    public String getVoters() {
        return voters;
    }

    public void setVoters(String voters) {
        this.voters = voters;
    }
}
