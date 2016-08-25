package com.couchbase.helloworld;

import android.support.multidex.MultiDexApplication;

/**
 * Created by mtrakal on 25.08.2016.
 */
public class Application extends MultiDexApplication {

    static DatabaseProvider databaseProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseProvider = new DatabaseProvider(this);
    }

    public static DatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }
}
