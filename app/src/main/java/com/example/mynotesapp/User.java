package com.example.mynotesapp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String Name;
    String Password;
    ArrayList<String> Notes = new ArrayList<>();

    public User(String Name, String password) {
        this.Name = Name;
        this.Password = password;
        this.Notes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", Notes=" + Notes +
                '}';
    }
}
