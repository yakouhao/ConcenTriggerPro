package com.ct.ct.concentrigger.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper;
import com.ct.ct.concentrigger.Model.User;
import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Util.CommonUtil;
import com.ct.ct.concentrigger.Util.ServiceURL;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//import java.util.Set;
//import cn.jpush.android.api.TagAliasCallback;
//import static android.content.ContentValues.TAG;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_ADD;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_CHECK;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_CLEAN;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_DELETE;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_GET;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.ACTION_SET;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.TagAliasBean;
//import static com.ct.ct.concentrigger.JPush.TagAliasOperatorHelper.sequence;



public class LoginActivity extends Activity {

    EditText mUserName;
    EditText mPassword;
    Button mLogin;
    Button mSignup;

    User curUser;
    String userName;
    String password;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initItem();

        //登录
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mUserName.getText().toString();
                password = mPassword.getText().toString();

                if(userName == null || password == null){
                    Toast.makeText(LoginActivity.this,"Pls fill in the blanks",Toast.LENGTH_SHORT).show();
                } else if (userName != null && password != null){
                    loginSignup("login");
                }

            }
        });

        //注册
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = mUserName.getText().toString();
                password = mPassword.getText().toString();

                if(userName == null || password == null){
                    Toast.makeText(LoginActivity.this,"Pls fill in the blanks",Toast.LENGTH_SHORT).show();
                } else if (userName != null && password != null){
                    loginSignup("signup");
                }

            }
        });

    }

    public void initItem(){
        mLogin = (Button)findViewById(R.id.btn_login);
        mSignup = (Button)findViewById(R.id.btn_signup);
        mPassword = (EditText)findViewById(R.id.edt_password);
        mUserName = (EditText)findViewById(R.id.edt_username);
    }

    public void loginSignup(String flag){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        //构造参数
        RequestParams params = new RequestParams();

        params.put("name", userName);
        params.put("password", password);
        params.put("flag", flag);

        String url = ServiceURL.getUrl(this, R.string.URL_LOGIN_SIGNUP);

        //发送请求
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){

            public void onSuccess(String content){

                JSONObject jsonObject = JSONObject.parseObject(content);

                boolean success = jsonObject.getBoolean("success");

                String info = jsonObject.getString("info");

                if(success){
                    Toast.makeText(LoginActivity.this,info,Toast.LENGTH_SHORT).show();
                    String userId = jsonObject.getString("userId");

//                    //在此处设置极光别名
//                    String alias = userId;
//                    int action = -1;
//                    if(TextUtils.isEmpty(alias)){
//                        return;
//                    }
//                    boolean isAliasAction = false;
//                    isAliasAction = true;
//                    action = ACTION_SET;
//                    TagAliasBean tagAliasBean = new TagAliasBean();
//                    tagAliasBean.setAction(action);
//                    sequence++;
//                    tagAliasBean.setAliasAction(isAliasAction) ;
//                    TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),sequence,tagAliasBean);


                    CommonUtil.saveUserNameToLocal(LoginActivity.this,userName);
                    CommonUtil.saveUserIdToLocal(LoginActivity.this,userId);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);


                }else{
                    Toast.makeText(LoginActivity.this,info,Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Throwable error, String content){

                Toast.makeText(LoginActivity.this, "连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

//    TagAliasCallback tagAlias = new TagAliasCallback() {
//        @Override
//        public void gotResult(int responseCode, String alias, Set<String> tags) {
//            Log.e(TAG,"responseCode:"+responseCode+",alias:"+alias+",tags:"+tags);
//        }
//    };


}
