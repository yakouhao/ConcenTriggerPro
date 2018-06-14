package com.ct.ct.concentrigger.Fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ct.ct.concentrigger.Model.Message;
import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Util.CommonUtil;
import com.ct.ct.concentrigger.Util.MsgAdapter;
import com.ct.ct.concentrigger.Util.ServiceURL;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment {

    ListView mMsgList;
    String userId;
    List<Message> msgList;
    MsgAdapter msgAdp;


    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        userId = CommonUtil.getUserIdFromLocal(getActivity());

        initItem(view);

        initData();

        return view;
    }

    public void initItem(View view) {
        mMsgList = (ListView) view.findViewById(R.id.msg_list);
    }

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

                JSONObject jsonObject = JSONObject.parseObject(content);
                boolean success = jsonObject.getBoolean("success");

                String info = jsonObject.getString("info");


                if(success){
                    Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();
                    String list = jsonObject.getString("msgList");
                    msgList = JSON.parseArray(list, Message.class);

                    //展示我关注的活动列表
                    msgAdp = new MsgAdapter(msgList);
                    mMsgList.setAdapter(msgAdp);



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

                Toast.makeText(getActivity(), "连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
