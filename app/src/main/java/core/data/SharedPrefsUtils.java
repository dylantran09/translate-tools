package core.data;

import android.content.Context;
import android.content.SharedPreferences;

import core.base.BaseApplication;
import core.util.Utils;

public class SharedPrefsUtils {

    /**
     * The name of this storage in the system
     */
    private static final String KEY_SHARED_PREFERENCES = Utils.getSharedPreferenceKey();

    /**
     * The reference to SharedPreferences which actually read and write the data
     * to the storage
     */
    private static SharedPreferences prefs;

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = BaseApplication.getContext().getSharedPreferences(
                    KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
        return prefs;
    }

    /* NORMAL SHARED PREFERENCES STRING KEY SETTER AND GETTER */
    public static synchronized boolean saveString(String key, String value) {
        return getInstance().edit().putString(key, value).commit();
    }

    public static synchronized String getString(String key, String defValue) {
        return getInstance().getString(key, defValue);
    }

    public static synchronized boolean saveInt(String key, int value) {
        return getInstance().edit().putInt(key, value).commit();
    }

    public static synchronized int getInt(String key, int defValue) {
        return getInstance().getInt(key, defValue);
    }

    public static synchronized boolean saveBoolean(String key, boolean value) {
        return getInstance().edit().putBoolean(key, value).commit();
    }

    public static synchronized boolean getBoolean(String key, boolean defValue) {
        return getInstance().getBoolean(key, defValue);
    }

    public static synchronized boolean saveLong(String key, long value) {
        return getInstance().edit().putLong(key, value).commit();
    }

    public static synchronized long getLong(String key, long defValue) {
        return getInstance().getLong(key, defValue);
    }

    public static synchronized boolean saveFloat(String key, float value) {
        return getInstance().edit().putFloat(key, value).commit();
    }

    public static synchronized float getFloat(String key, float defValue) {
        return getInstance().getFloat(key, defValue);
    }
    /* END ** NORMAL SHARED PREFERENCES STRING KEY SETTER AND GETTER */
}
