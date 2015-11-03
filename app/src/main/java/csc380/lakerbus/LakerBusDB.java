package csc380.lakerbus;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by IAmACrayon on 10/25/2015.
 */
public class LakerBusDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "busStopDB.db";
    private static final int DATABASE_VERSION = 1;

    public LakerBusDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}