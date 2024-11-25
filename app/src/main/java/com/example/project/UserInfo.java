package com.example.project;


public class UserInfo {
    private String  name;
    private String sub;
    private String id;
    private String cun;

    public UserInfo(String name, String sub, String id, String cun) {
        this.name = name;
        this.sub = sub;
        this.id = id;
        this.cun = cun;
    }

    public UserInfo(String name, String sub) {
        this.name = name;
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCun() {
        return cun;
    }

    public void setCun(String cun) {
        this.cun = cun;
    }
}