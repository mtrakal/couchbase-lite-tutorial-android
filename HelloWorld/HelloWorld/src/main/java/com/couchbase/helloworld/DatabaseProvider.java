package com.couchbase.helloworld;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by mtrakal on 25.08.2016.
 */

public class DatabaseProvider {
    private static final String TAG = "DatabaseProvider";
    private Database database;

    public DatabaseProvider(Context context) {
        connectDb(context);
    }

    private void connectDb(Context context) {
        // create a manager
        Manager.enableLogging("cblite", Log.VERBOSE);
        Manager manager;
        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            Log.d(TAG, "Manager created");
        } catch (IOException e) {
            Log.e(TAG, "Cannot create manager object");
            return;
        }

        // create a name for the database and make sure the name is legal
        String dbname = "local-hello";
        if (!Manager.isValidDatabaseName(dbname)) {
            Log.e(TAG, "Bad database name");
            return;
        }

        // same database is already opened
        if (database != null) {
            return;
        }

        // create a new database
        try {
            DatabaseOptions options = new DatabaseOptions();
            options.setCreate(true);
            options.setStorageType(Manager.FORESTDB_STORAGE);
            database = manager.openDatabase(dbname, options);
            Log.d(TAG, "Database created");

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
            return;
        }
    }

    public Database getDatabase() {
        return database;
    }


}
