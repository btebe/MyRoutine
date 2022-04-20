package com.myroutine.myroutine;

/**
 * Created by basmatebe on 4/1/17.
 */

public class SuperUser {
    private int id;
    private String password, email;

    public SuperUser(){}

    public SuperUser(String password, String email){
        this.password = password;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
