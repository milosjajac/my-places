package com.example.jajac.myplaces;

import android.app.Application;
import android.content.Context;

/**
 * Created by jajac on 3/21/17.
 */

public class MyPlacesApplication extends Application {
    private static MyPlacesApplication instance;

    public MyPlacesApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
