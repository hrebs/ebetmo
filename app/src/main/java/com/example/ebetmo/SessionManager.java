package com.example.ebetmo;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    //Initialize Variable
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Create Constructor
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("Appkey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    //create set login method
    public void setLogin(boolean login){
        editor.putBoolean("KEY_LOGIN",login);
        editor.commit();
    }
    //create set login method
    public boolean getLogin() {
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    //create set email method
    public void setEmail(String email){
        editor.putString("KEY_EMAIL", email);
        editor.commit();
    }
    public String getEmail(){
        return sharedPreferences.getString("KEY_EMAIL", "");}

    //create set id method
    public void setId(String id){
        editor.putString("KEY_ID", id);
        editor.commit();
    }
    public String getId(){
        return sharedPreferences.getString("KEY_ID", "");}

    //create set COIN method
    public void setCoin(String id){
        editor.putString("KEY_COIN", id);
        editor.commit();
    }
    public String getCoin(){
        return sharedPreferences.getString("KEY_COIN", "");}

    //create set full name method
    public void setName(String name){
        editor.putString("KEY_NAME", name);
        editor.commit();
    }
    public String getName(){
        return sharedPreferences.getString("KEY_NAME", "");}

    //create set validation method
    public void setValid(String valid){
        editor.putString("KEY_VALID", valid);
        editor.commit();
    }
    public String getValid(){
        return sharedPreferences.getString("KEY_VALID", "");}





}
