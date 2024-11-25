package com.example.project;

public class UserData {
    private String first;
    private String last;
    private  String id;
    private boolean present;
    private  String userID;

    public UserData(String first, String last, String id,String userID) {
        this.first = first;
        this.last = last;
        this.id = id;
        this.present=true;
        this.userID=userID;
    }

    public UserData(String first, String last, String id) {
        this.first = first;
        this.last = last;
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
