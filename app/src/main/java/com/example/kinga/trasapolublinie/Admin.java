package com.example.kinga.trasapolublinie;

/**
 * Created by kinga on 11.09.2018.
 */

public class Admin {

    private String email;
    private String Uid;

    public Admin(){}


    public Admin(String email, String uid) {
        this.email = email;
        this.Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return Uid;
    }
}
