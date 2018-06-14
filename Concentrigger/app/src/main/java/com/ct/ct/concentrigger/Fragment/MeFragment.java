package com.ct.ct.concentrigger.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ct.ct.concentrigger.R;

import com.ct.ct.concentrigger.Util.ServiceURL;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ct.ct.concentrigger.Util.RoundImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    TextView mUserName;
    TextView mEventNum;
    RoundImageView mAvatar;


    private Context context = this.getContext();
    private static final int PIC = 100;
    private static final int CAM = 200;
    private static final int ADDR = 300;
    private String picturePath = "";


    private String userName;
    private String userId;
    String avatarImg;

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    List<String> mPermissionList = new ArrayList<>();



    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getActivity(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] mpermissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }


        initItem(view);
        initData();

        //点击头像更换头像
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

                try {
                    changeAvatar();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        });

        return view;
    }

    //初始化控件
    public void initItem(View view){
        mAvatar = (RoundImageView)view.findViewById(R.id.img_avatar);
        mEventNum = (TextView)view.findViewById(R.id.txt_event_num);
        mUserName = (TextView)view.findViewById(R.id.txt_user_name);
    }


    //加载用户数据
    public void initData(){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        //构造参数
        RequestParams params = new RequestParams();

        params.put("userId", userId);



        Log.e("tag",params.toString());

        String url = ServiceURL.getUrl(getActivity(), R.string.URL_CHANGE_AVATAR);

        //发送请求
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){

            public void onSuccess(String content){
                Log.e("tag","进了修改头像的onsuccess");

                JSONObject jsonObject = JSONObject.parseObject(content);
                Log.e("tag","json对象建立");

                boolean success = jsonObject.getBoolean("success");
                Log.e("tag","收到success");
                String info = jsonObject.getString("info");
                Log.e("tag","收到info");

                if(success){
                    Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();

                    avatarImg = jsonObject.getString("avatar");
                    //这里用毕加索


//                    str_profile = jsonObject.getString("profile");
//
//                    if (str_profile != null) {
//                        Picasso.with(context).load(ServiceURL.getServerUrl(context) + str_profile).into(barHeadImg);
//
//                    } else {
//                        Log.e("tag","header空");
//                    }


                }else{
                    Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Throwable error, String content){
                Log.e("tag","进了修改头像的onfailure");
                Toast.makeText(getActivity(), "连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        });



    }


    //选择图片
    private void chooseImage(){

        String[] items = new String[]{"图库","相机"};


        new android.app.AlertDialog.Builder(this.getActivity())
                .setTitle("选择来源")
                                    .setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            switch (i) {
                                                case 0 ://图库
                                                    //启动图库的activity，需要用隐式意图,括号里的参数是固定写法
                                Intent pic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                //PIC 表示请求码，便于在onActivityResult里进行判断，这里需要声明一个静态成员变量
                                startActivityForResult(pic,PIC);
                                break;
                            case 1 ://相机

                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAM);

                                break;
                        }
                    }
                }).setCancelable(true)
                .show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case  PIC:
                if(resultCode==RESULT_OK && data!=null)
                {

                    //因为读取本地图库，会存在权限问题，需要在manifest文件里添加<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
                    //这个可以先不和学生讲，由他们根据报错信息自行查找解决方案，网络上可以搜索到解决方案
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    mAvatar.setImageBitmap(bitmap);


                }
                break;
            case  CAM:
                if(resultCode==RESULT_OK && data!=null)
                {
                    String path = Environment.getExternalStorageDirectory()+"/ct/";

                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        return;
                    }

                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    FileOutputStream b = null;
                    File file = new File(path);
                    file.mkdirs();// 创建文件夹
                    picturePath = path+name;

                    Log.e("tag",picturePath);

                    try {
                        b = null;
                        b = new FileOutputStream(picturePath);
                        Log.e("tag","b成功");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                        if(bitmap!=null)
                            Log.e("tag","bitmap不空");

                        Log.e("tag","bitmap写入成功");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    mAvatar.setImageBitmap(bitmap);

                    Log.e("tag","set成功");
                }
                break;

        }
    }

    //修改头像
    public void changeAvatar() throws FileNotFoundException {
//        chooseImage();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

//
        //构造参数
        RequestParams params = new RequestParams();
        Log.e("tag","修改头像参数构建成功");
        Log.e("tag","修改头像的picturePath是"+picturePath);
        File file = new File(picturePath);

        params.put("userId", userId);
        params.put("avatar", file);


        Log.e("tag",params.toString());

        String url = ServiceURL.getUrl(getActivity(), R.string.URL_USER_INFO);

        //发送请求
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){

            public void onSuccess(String content){
                Log.e("tag","进了修改头像的onsuccess");

                JSONObject jsonObject = JSONObject.parseObject(content);
                Log.e("tag","json对象建立");

                boolean success = jsonObject.getBoolean("success");
                Log.e("tag","收到success");
                String info = jsonObject.getString("info");
                Log.e("tag","收到info");

                if(success){
                    Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();

//                    str_profile = jsonObject.getString("profile");
//
//                    if (str_profile != null) {
//                        Picasso.with(context).load(ServiceURL.getServerUrl(context) + str_profile).into(barHeadImg);
//
//                    } else {
//                        Log.e("tag","header空");
//                    }


                }else{
                    Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Throwable error, String content){
                Log.e("tag","进了修改头像的onfailure");
                Toast.makeText(getActivity(), "连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if(ContextCompat.checkSelfPermission(
                            getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                1);

                    }else{
                        Log.d(TAG,"授权了1");
                    }


                } else {
                    Log.d(TAG,"授权了2");
                }
                break;
        }

    }

}
