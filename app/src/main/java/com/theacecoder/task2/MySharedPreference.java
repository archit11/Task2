package com.theacecoder.task2;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Archit on 27-07-2016.
 */
public class MySharedPreference {
    private SharedPreferences prefs;
    private Context context;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String USER_ID = "userid";
    public static final String MY_TOKEN = "mytoken";
    public static final String CHAT_TOKEN = "chattoken";
    public static final String CHAT_OPEN = "chatopen";

    public MySharedPreference(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }

    public void saveUserID(String uid){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(USER_ID, uid);
        edits.apply();
    }

    public String getUserId() {
        return prefs.getString(USER_ID, "unset");
    }


    public void saveMyToken(String token){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(MY_TOKEN, token);
        edits.apply();
    }

    public String getMyToken() {
        return prefs.getString(MY_TOKEN, "unset");
    }

    public void saveChatToken(String token){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(CHAT_TOKEN, token);
        edits.apply();
    }

    public String getChatToken() {
        return prefs.getString(CHAT_TOKEN, "unset");
    }

    public void setChatStatus(boolean status) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(CHAT_OPEN, status);
        editor.apply();
    }

    public Boolean getChatStatus() {
        return prefs.getBoolean(CHAT_OPEN, false);
    }

    public void setFromNotification(boolean status) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Notification", status);
        editor.apply();
    }

    public Boolean getFromNotification() {
        return prefs.getBoolean("Notification", false);
    }
}
