package com.ct.ct.concentrigger.Activity;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ct.ct.concentrigger.Fragment.EventFragment;
import com.ct.ct.concentrigger.Fragment.MeFragment;
import com.ct.ct.concentrigger.Fragment.MsgFragment;
import com.ct.ct.concentrigger.Fragment.SquareFragment;
import com.ct.ct.concentrigger.JPush.ExampleUtil;
import com.ct.ct.concentrigger.JPush.LocalBroadcastManager;
import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Util.CommonUtil;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    //当前用户
    private String userName;
    private String userId;

    //四个Tab对应的布局
    private LinearLayout mTabEvent;
    private LinearLayout mTabSquare;
    private LinearLayout mTabMsg;
    private LinearLayout mTabMe;

    //四个Tab对应的ImageButton
    private ImageButton mImgEvent;
    private ImageButton mImgSquare;
    private ImageButton mImgMsg;
    private ImageButton mImgMe;

    Fragment eventFragment;
    Fragment squareFragment;
    Fragment msgFragment;
    Fragment meFragment;

    ImageView mBtnNew;

    //JPush
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = CommonUtil.getUserNameFromLocal(MainActivity.this);
        userId = CommonUtil.getUserIdFromLocal(MainActivity.this);



        Log.e("tag","主页接收到用户名:"+userName);


        initViews();
        // 初始化数据
        initDatas();


        initEvents();//初始化事件

        //JPush
//        registerMessageReceiver();  // used for receive msg

        //发起新活动
        mBtnNew.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent toNewEvent = new Intent();
                toNewEvent.setClass(MainActivity.this,NewEventActivity.class);
                startActivity(toNewEvent);
            }


        });
    }


    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    private void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(eventFragment);
        mFragments.add(squareFragment);
        mFragments.add(msgFragment);
        mFragments.add(meFragment);


        //初始化适配器
        mAdapter = new FragmentPagerAdapter(MainActivity.this.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                fragment = mFragments.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return fragment;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                Fragment fragment = mFragments.get(position);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
        };

        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        //设置四个Tab的点击事件
        mTabEvent.setOnClickListener(this);
        mTabSquare.setOnClickListener(this);
        mTabMsg.setOnClickListener(this);
        mTabMe.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {

        eventFragment = new EventFragment();
        squareFragment = new SquareFragment();
        msgFragment = new MsgFragment();
        meFragment = new MeFragment();

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabEvent = (LinearLayout) findViewById(R.id.id_tab_event);
        mTabSquare = (LinearLayout) findViewById(R.id.id_tab_square);
        mTabMsg = (LinearLayout) findViewById(R.id.id_tab_msg);
        mTabMe = (LinearLayout) findViewById(R.id.id_tab_me);



        mImgEvent = (ImageButton) findViewById(R.id.id_tab_event_img);
        mImgSquare = (ImageButton) findViewById(R.id.id_tab_square_img);
        mImgMsg = (ImageButton) findViewById(R.id.id_tab_msg_img);
        mImgMe = (ImageButton) findViewById(R.id.id_tab_me_img);

        mBtnNew = (ImageView) findViewById(R.id.btn_new_event);


    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgEvent.setImageResource(R.mipmap.tab_event_pressed);
                break;
            case 1:
                mImgSquare.setImageResource(R.mipmap.tab_square_pressed);
                break;
            case 2:
                mImgMsg.setImageResource(R.mipmap.tab_msg_pressed);
                break;
            case 3:
                mImgMe.setImageResource(R.mipmap.tab_me_pressed);
                break;

        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为灰色
    private void resetImgs() {
        mImgEvent.setImageResource(R.mipmap.tab_event_normal);
        mImgSquare.setImageResource(R.mipmap.tab_square_normal);
        mImgMsg.setImageResource(R.mipmap.tab_msg_normal);
        mImgMe.setImageResource(R.mipmap.tab_me_normal);
    }


    public void onClick(View v) {

        //先将四个ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_event:
                selectTab(0);

                break;
            case R.id.id_tab_square:
                selectTab(1);


                break;
            case R.id.id_tab_msg:
                selectTab(2);

                break;
            case R.id.id_tab_me:
                selectTab(3);

                break;


        }

    }


    // 按两次退出程序
    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                //finish();

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


//    //JPush 新添加
//    @Override
//    protected void onResume() {
//        isForeground = true;
//        super.onResume();
//    }
//
//
//    @Override
//    protected void onPause() {
//        isForeground = false;
//        super.onPause();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        super.onDestroy();
//    }
//
//    //for receive customer msg from jpush server
//    private MessageReceiver mMessageReceiver;
//    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_MESSAGE = "message";
//    public static final String KEY_EXTRAS = "extras";
//
//    public void registerMessageReceiver() {
//        mMessageReceiver = new MessageReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
//    }
//
//    public class MessageReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                    String messge = intent.getStringExtra(KEY_MESSAGE);
//                    String extras = intent.getStringExtra(KEY_EXTRAS);
//                    StringBuilder showMsg = new StringBuilder();
//
//                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                    if (!ExampleUtil.isEmpty(extras)) {
//                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                    }
//
//
//                    //在此处获取到了用户自定义发来的消息
//                    //具体消息的格式还需要和后端对接
//
//                }
//            } catch (Exception e){
//            }
//        }
//    }

}
