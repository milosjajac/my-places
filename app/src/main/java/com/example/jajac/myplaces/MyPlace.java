package com.example.jajac.myplaces;

/**
 * Created by jajac on 3/20/17.
 */

public class MyPlace {
    String name;
    String description;

    public MyPlace(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public MyPlace(String name) {
        this(name, "");
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
