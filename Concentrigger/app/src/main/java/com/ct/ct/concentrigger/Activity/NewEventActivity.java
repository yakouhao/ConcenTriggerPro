package com.ct.ct.concentrigger.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ct.ct.concentrigger.Model.Event;
import com.ct.ct.concentrigger.Model.Message;
import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Util.CommonUtil;
import com.ct.ct.concentrigger.Util.MsgAdapter;
import com.ct.ct.concentrigger.Util.ServiceURL;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Date;

import static android.view.View.VISIBLE;

public class NewEventActivity extends Activity {




    Event newEvent = new Event();

    String userId ;
    int concentrateId;
    int triggerId;
    String blogger;
    String key;
    Date time;
    String location;
    String locationX;
    String locationY;

    LinearLayout mPanelBilibili;
    LinearLayout mPanelWeibo;
    LinearLayout mPanelSubscription;
    LinearLayout mPanelWeather;

    ImageView mWeibo;
    ImageView mBilibili;
    ImageView mSubscription;
    ImageView mWeather;

    Button mBlRl;
    Button mWbRl;
    Button mSubRl;
    Button mWt;
    Button mDg;

    LinearLayout mUniformBlank;
    LinearLayout mRowWeather;
    LinearLayout mRowDegree;

    EditText mBlogger;
    EditText mKey;
    EditText mEdtWeather;
    EditText mEdtDegree;

    Button mBtnOk;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        userId = CommonUtil.getUserIdFromLocal(NewEventActivity.this);

        initItem();

        mBilibili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelInvisible();
                mPanelBilibili.setVisibility(VISIBLE);

            }
        });
        //发新的视频
        mBlRl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mBlRl.setBackgroundResource(R.color.colorPrimary);
                mUniformBlank.setVisibility(VISIBLE);
            }
        });


        mWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelInvisible();
                mPanelWeibo.setVisibility(VISIBLE);
            }
        });
        mSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelInvisible();
                mPanelSubscription.setVisibility(VISIBLE);
            }
        });
        //con是天气
        mWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelInvisible();
                mPanelWeather.setVisibility(VISIBLE);
            }
        });
        //选择了天气
        mWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRowInvisible();
                mRowWeather.setVisibility(VISIBLE);
            }
        });
        //选择了度数
        mDg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRowInvisible();
                mRowDegree.setVisibility(VISIBLE);
            }
        });




        //创建事件！
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEvent();
                createEvent();
            }
        });

    }

    public void initItem(){
        mPanelBilibili = (LinearLayout)findViewById(R.id.panel_bilibili);
        mPanelWeibo = (LinearLayout)findViewById(R.id.panel_weibo);
        mPanelSubscription = (LinearLayout)findViewById(R.id.panel_subscription);
        mPanelWeather = (LinearLayout)findViewById(R.id.panel_weather);

        mBilibili = (ImageView)findViewById(R.id.btn_bl);
        mWeibo = (ImageView)findViewById(R.id.btn_weibo);
        mSubscription = (ImageView)findViewById(R.id.btn_sub);
        mWeather = (ImageView)findViewById(R.id.btn_weather);

        mWbRl = (Button)findViewById(R.id.btn_rl_wb);
        mBlRl = (Button)findViewById(R.id.btn_rl_vd);
        mSubRl = (Button)findViewById(R.id.btn_rl_sp);
        mWt = (Button)findViewById(R.id.btn_choose_weather);
        mDg = (Button)findViewById(R.id.btn_choose_degree);

        mUniformBlank = (LinearLayout)findViewById(R.id.panel_form_social);

        mBlogger = (EditText)findViewById(R.id.edt_blogger);
        mKey = (EditText)findViewById(R.id.edt_key);

        mRowWeather = (LinearLayout)findViewById(R.id.row_weather);
        mRowDegree = (LinearLayout)findViewById(R.id.row_degree);

        mEdtWeather = (EditText)findViewById(R.id.edt_weather);
        mEdtDegree = (EditText)findViewById(R.id.edt_degree);

        mBtnOk = (Button)findViewById(R.id.btn_ok);

    }

    public void setPanelInvisible(){
        mPanelBilibili.setVisibility(View.INVISIBLE);
        mPanelWeibo.setVisibility(View.INVISIBLE);
        mPanelSubscription.setVisibility(View.INVISIBLE);
        mPanelWeather.setVisibility(View.INVISIBLE);
    }

    public void setRowInvisible(){
        mRowWeather.setVisibility(View.INVISIBLE);
        mRowDegree.setVisibility(View.INVISIBLE);
    }

    public void chooseEvent(){

        switch(concentrateId){
            case 1:
                //如果是新B站发布
                blogger = mBlogger.getText().toString();
                key = mKey.getText().toString();
                break;
            case 2:
                //如果是新微博发布
                blogger = mBlogger.getText().toString();
                key = mKey.getText().toString();
                break;
            case 3:
                //如果是新公众号文章
                blogger = mBlogger.getText().toString();
                key = mKey.getText().toString();
                break;
            case 4:
                //如果是天气
                blogger = "";
                key = mEdtWeather.getText().toString();
                break;
            case 5:
                //如果是温度
                blogger = "";
                key = mEdtDegree.getText().toString();
                break;
        }
    }

    public void createEvent(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        //构造参数
        RequestParams params = new RequestParams();

        params.put("userId", userId);
        params.put("concentrateId", String.valueOf(concentrateId));
        params.put("triggerId",String.valueOf(triggerId));
        params.put("blogger",blogger);
        params.put("key",newEvent.getKey());
        params.put("time",String.valueOf("saa"));
        params.put("location",location);
        params.put("locationX",locationX);
        params.put("locationY",locationY);


        Log.e("tag",params.toString());

        String url = ServiceURL.getUrl(NewEventActivity.this, R.string.URL_NEW_EVENT);

        //发送请求
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){

            public void onSuccess(String content){

                JSONObject jsonObject = JSONObject.parseObject(content);
                boolean success = jsonObject.getBoolean("success");

                String info = jsonObject.getString("info");


                if(success){
                    Toast.makeText(NewEventActivity.this,info,Toast.LENGTH_SHORT).show();
                    String list = jsonObject.getString("msgList");

                    //跳转到主页
                    Intent intent = new Intent(NewEventActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(NewEventActivity.this,info,Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Throwable error, String content){

                Toast.makeText(NewEventActivity.this, "连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
