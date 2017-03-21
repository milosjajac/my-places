package com.example.jajac.myplaces;

/**
 * Created by jajac on 3/20/17.
 */

public class MyPlace {
    String name;
    String description;
    String longitude;
    String latitude;
    long ID;

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

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public long getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
