package com.concentriggerpj.Activity;

import android.os.Bundle;
import com.concentriggerpj.R;
import android.content.Intent;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.concentriggerpj.Fragment.EventFragment;
import com.concentriggerpj.Fragment.MeFragment;
import com.concentriggerpj.Fragment.SquareFragment;
import com.concentriggerpj.Util.CommonUtil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    //当前用户
    private String curUser;

    //四个Tab对应的布局
    private LinearLayout mTabEvent;
    private LinearLayout mTabSquare;
    private LinearLayout mTabMe;



    //四个Tab对应的ImageButton
    private ImageButton mImgEvent;
    private ImageButton mImgSquare;
    private ImageButton mImgMe;

    Fragment eventFragment;
    Fragment squareFragment;
    Fragment meFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curUser = CommonUtil.getUserFromLocal(MainActivity.this);



        Log.e("tag","主页接收到用户名:"+curUser);

        // 初始化数据
        initViews();

        initDatas();

        initEvents();//初始化事件


        //发起新活动
//        mTabAdd.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                Intent toNewAct = new Intent();
//                toNewAct.setClass(ActHomeActivity.this,ActNewActivity.class);
//
//                //用Bundle携带数据
//                Bundle bundle=new Bundle();
//                //传递name参数为tinyphp
//                bundle.putString("curUser", curUser);
//                toNewAct.putExtras(bundle);
//
//                ActHomeActivity.this.startActivity(toNewAct);
//
//
//            }
//
//
//        });
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
        mTabMe.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {

        eventFragment = new EventFragment();
        squareFragment = new SquareFragment();
        meFragment = new MeFragment();

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabEvent = (LinearLayout) findViewById(R.id.id_tab_event);
        mTabSquare = (LinearLayout) findViewById(R.id.id_tab_square);

        mTabMe = (LinearLayout) findViewById(R.id.id_tab_me);



        mImgEvent = (ImageButton) findViewById(R.id.id_tab_event_img);
        mImgSquare = (ImageButton) findViewById(R.id.id_tab_square_img);

        mImgMe = (ImageButton) findViewById(R.id.id_tab_me_img);


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
            case R.id.id_tab_me:
                selectTab(2);

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


}
