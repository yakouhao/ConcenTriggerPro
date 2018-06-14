package com.ct.ct.concentrigger.Util;

/**
 * Created by holic on 2018/6/7.
 */



import android.content.Context;

import com.ct.ct.concentrigger.R;


/**
 * Created by turtolic on 2017/7/6.
 * 辅助获取URL类
 */

public class ServiceURL {


    public static String getServerUrl(Context context)
    {
        return context.getString(R.string.URL_SERVER);
    }

    public static String getUrl(Context context,int resId)
    {
        return context.getString(R.string.URL_SERVER)+context.getString(resId);
    }






}


