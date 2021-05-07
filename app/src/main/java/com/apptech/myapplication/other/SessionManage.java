package com.apptech.myapplication.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.apptech.myapplication.activity.MainActivity;

import java.util.HashMap;

public class SessionManage {

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences cart;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // userid address (make variable public to access from outside)
    public static final String USERID = "userid";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Email address (make variable public to access from outside)
    public static final String KEYID = "keyid";

    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBiLE = "mobile";

    // user avatar (make variable public to access from outside)
    public static final String KEY_PHOTO = "photo";

    // number of items in our cart
    public static final String KEY_CART = "cartvalue";

    // number of items in our wishlist
    public static final String KEY_WISHLIST = "wishlistvalue";

    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";



    /*
     *
     *
     *   My Lava
     *
     */

    public static final String CARD_DATA = "CARD_DATA";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String NOTIFICATION_LIST_STORE = "NOTIFICATION_LIST_STORE";


    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String MOBILE = "MOBILE";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String PASSWORD = "PASSWORD";
    public static final String GOVERNATE = "GOVERNATE";
    public static final String CITY = "CITY";
    public static final String LOCALITY = "LOCALITY";
    public static final String TIME = "TIME";
    public static final String ADDRESS = "ADDRESS";
    public static final String LOCALITY_ID = "LOCALITY_ID";

    public static final String FIRST_TIME_LANGUAGE = "FIRST_TIME_LANGUAGE";
    public static final String BRAND_ID = "BRAND_ID";
    public static final String BRAND_NAME = "BRAND_NAME";
    public static final String BRAND_NAME_AR = "BRAND_NAME_AR";

    private static SessionManage ourInstance = null;

    public static SessionManage getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SessionManage(context);
        }
        return ourInstance;
    }

    // Constructor
    private SessionManage(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user phone number
        user.put(KEY_MOBiLE, pref.getString(KEY_MOBiLE, null));

        // user avatar
        user.put(KEYID, pref.getString(KEYID, null));
        user.put(USERID, pref.getString(USERID, null));

//        CARD_DATA
        user.put(CARD_DATA, pref.getString(CARD_DATA, null));

        user.put(LANGUAGE, pref.getString(LANGUAGE, null));

        user.put(ID, pref.getString(ID, null));

        user.put(FIRST_TIME_LANGUAGE, pref.getString(FIRST_TIME_LANGUAGE, null));

        user.put(BRAND_ID, pref.getString(BRAND_ID, null));
        user.put(BRAND_NAME, pref.getString(BRAND_NAME, null));
        user.put(BRAND_NAME_AR, pref.getString(BRAND_NAME_AR, null));
        user.put(LOCALITY_ID, pref.getString(LOCALITY_ID, null));

        user.put(NOTIFICATION_LIST_STORE, pref.getString(NOTIFICATION_LIST_STORE, null));

        user.put(LOCALITY, pref.getString(LOCALITY, null));
        user.put(NAME, pref.getString(NAME, null));
        user.put(MOBILE, pref.getString(MOBILE, null));
        user.put(ADDRESS, pref.getString(ADDRESS, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /*
     *
     * lava session
     *
     * */


    public void addcard(String json) {
        editor.putString(CARD_DATA, json);
        editor.commit();
    }

    public void setlanguage(String language) {
        editor.putString(LANGUAGE, language);
        editor.commit();
    }


    public void clearaddcard() {
        editor.remove(CARD_DATA);
        editor.commit();
    }


    public void UserDetail(String id, String name, String email, String mobile, String user_type, String password, String governate, String city, String locality, String time, String address , String locality_id) {
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(MOBILE, mobile);
        editor.putString(USER_TYPE, user_type);
        editor.putString(PASSWORD, password);
        editor.putString(GOVERNATE, governate);
        editor.putString(CITY, city);
        editor.putString(LOCALITY, locality);
        editor.putString(TIME, time);
        editor.putString(ADDRESS, address);
        editor.putString(LOCALITY_ID, locality_id);
        editor.commit();
    }

    public void FirstTimeLanguage(String firsttime) {
        editor.putString(FIRST_TIME_LANGUAGE, firsttime);
        editor.commit();
    }

    public void brandSelect(String brandid , String brand_name , String brand_name_ar ) {
        editor.putString(BRAND_ID, brandid);
        editor.putString(BRAND_NAME, brand_name);
        editor.putString(BRAND_NAME_AR, brand_name_ar);
        editor.commit();
    }

    public void logout() {
        editor.remove(FIRST_TIME_LANGUAGE).commit();
        editor.remove(BRAND_ID).commit();
    }

    public void NotificationStore(String list) {
        editor.putString(NOTIFICATION_LIST_STORE, list).commit();
    }

    public void RemoveNotificationStore() {
        editor.remove(NOTIFICATION_LIST_STORE).commit();
    }


}




























