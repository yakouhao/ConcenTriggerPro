package com.concentriggerpj.Util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abc on 2017/7/12.
 */

public class CommonUtil {
    public static  void removeFromLocal(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("info",context.MODE_PRIVATE);
        pref.edit().remove(key).clear().commit();
    }

    public static void saveUserToLocal(Context context,String curUser){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",context.MODE_PRIVATE).edit();
        editor.putString("curUser",curUser);
        editor.apply();
    }

    public static String getUserFromLocal(Context context){
        SharedPreferences pref = context.getSharedPreferences("data",context.MODE_PRIVATE);
        String curUser = pref.getString("curUser","");
        return curUser;
    }

}

