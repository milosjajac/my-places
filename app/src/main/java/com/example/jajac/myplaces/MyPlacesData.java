package com.example.jajac.myplaces;

import java.util.ArrayList;

/**
 * Created by jajac on 3/20/17.
 */

public class MyPlacesData {
    private ArrayList<MyPlace> myPlaces;

    private MyPlacesData() {
        myPlaces = new ArrayList<MyPlace>();
        myPlaces.add(new MyPlace("Tvrdjava", "Opis tvrdjave."));
        myPlaces.add(new MyPlace("Park Svetog Save", "Na bulevaru Nemanjica."));
        myPlaces.add(new MyPlace("Zona 3"));
    }

    private static class SingletonHolder {
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance() {
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces() {
        return myPlaces;
    }

    public void addNewPlace(MyPlace place) {
        myPlaces.add(place);
    }

    public MyPlace getPlace(int index) {
        return myPlaces.get(index);
    }

    public void deletePlace(int index) {
        myPlaces.remove(index);
    }
}
