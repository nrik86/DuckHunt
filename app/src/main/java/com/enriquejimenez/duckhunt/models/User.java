package com.enriquejimenez.duckhunt.models;

public class User {

    private String name;
    private Long ducks;

    public User() {
    }

    public User(String name, Long ducks) {
        this.name = name;
        this.ducks = ducks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDucks() {
        return ducks;
    }

    public void setDucks(Long ducks) {
        this.ducks = ducks;
    }
}
