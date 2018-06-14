package com.ct.ct.concentrigger.Util;


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

    public static void saveUserNameToLocal(Context context,String userName){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",context.MODE_PRIVATE).edit();

        editor.putString("userName",userName);
        editor.apply();
    }

    public static void saveUserIdToLocal(Context context,String userId){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",context.MODE_PRIVATE).edit();

        editor.putString("userId",userId);
        editor.apply();
    }

    public static String getUserNameFromLocal(Context context){
        SharedPreferences pref = context.getSharedPreferences("data",context.MODE_PRIVATE);
        String userName = pref.getString("userName","");
        return userName;
    }

    public static String getUserIdFromLocal(Context context){
        SharedPreferences pref = context.getSharedPreferences("data",context.MODE_PRIVATE);
        String userId = pref.getString("userId","");
        return userId;
    }

}

