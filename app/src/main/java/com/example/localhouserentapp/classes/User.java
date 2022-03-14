package com.example.localhouserentapp.classes;

public class User {
    String Name, Mobile, Email, Password, Address, ProfilePicUrl;

    public User(String name, String mobile, String email, String password, String address, String profilePicUrl) {
        Name = name;
        Mobile = mobile;
        Email = email;
        Password = password;
        Address = address;
        ProfilePicUrl = profilePicUrl;
    }
    public User(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }
}
