package com.ct.ct.concentrigger.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends Activity implements GeocodeSearch.OnGeocodeSearchListener, TextWatcher,Inputtips.InputtipsListener{

    private MapView mMapView = null;
    private AMap aMap;
    private Marker marker;
    private Marker serMarker;

    //地理搜索类
    private GeocodeSearch geocodeSearch;
    private GeocodeSearch geocoderSearch;
    private String addr ;

    private AutoCompleteTextView mKeywordText;
    ImageView mBtnSearch;
    private ListView minputlist;

    double location_x = 0.0;
    double location_y = 0.0;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        checkPermissions(needPermissions);

        mKeywordText = (AutoCompleteTextView)findViewById(R.id.edt_search);
        mKeywordText.addTextChangedListener(this);

        mBtnSearch = (ImageView) findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = mKeywordText.getText().toString();
                getLatlon(address);
                if(minputlist.getVisibility()==View.VISIBLE)
                {
                    minputlist.setVisibility(View.INVISIBLE);
                }//IF
            }
        });

        minputlist = (ListView)findViewById(R.id.lst_input);
        minputlist.setVisibility(View.INVISIBLE);
        minputlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //minputlist.setVisibility(View.INVISIBLE);
                String addrName;
                HashMap<String,String> map = (HashMap<String,String>)minputlist.getItemAtPosition(i);
                addrName = map.get("address")+map.get("name");
                mKeywordText.setText(addrName);
            }
        });

        //给按钮添加监听，获取当前地址，并回传给调用者activity
        Button bt_addr_confirm = (Button) findViewById(R.id.btn_ok);
        bt_addr_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("addr", addr);
                intent.putExtra("location_x",String.valueOf(location_x));
                intent.putExtra("location_y",String.valueOf(location_y));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        /////////////////初始化地图///////////////////
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        ///////////////初始化定位蓝点样式类///////////////////
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        ////定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置是否定位到当前位置，并显示定位小蓝点，默认为true
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置放大的倍数
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
        //位置
        options.position(aMap.getCameraPosition().target);
        options.title("title");
        //子标题
        //options.snippet("这是我的位置");
        marker = aMap.addMarker(options);
        //地理信息获取类：根据经纬度获取地址
        geocodeSearch = new GeocodeSearch(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        /////////////////地图选点功能，移动地图的监听事件/////////////////////
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            //移动过程中
            public void onCameraChange(CameraPosition cameraPosition) {
                marker.setPosition(cameraPosition.target);
            }

            @Override
            //移动结束后，获得具体的地址信息
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLonPoint lp = new LatLonPoint(cameraPosition.target.latitude,cameraPosition.target.longitude);
                location_x = cameraPosition.target.latitude;
                location_y = cameraPosition.target.longitude;
                Log.w("地址：",String.valueOf(location_x));
                RegeocodeQuery query = new RegeocodeQuery(lp, 500f, GeocodeSearch.AMAP);
                //异步查询,根据经纬度查地址
                geocodeSearch.getFromLocationAsyn(query);
                geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                        String formatAddress = regeocodeAddress.getFormatAddress();
                        addr = formatAddress.toString();

                        Log.w("addr",addr);
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
                marker.setTitle(addr);
            }
        });
    }


    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        GeocodeQuery query = new GeocodeQuery(name, "");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub
        Log.d("beforeTextChanged被执行：",s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, "");
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
        if(minputlist.getVisibility()==View.INVISIBLE)
        {
            minputlist.setVisibility(View.VISIBLE);
        }//IF
        Log.d("onTextChanged被执行：",s.toString());

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        Log.d("afterTextChanged被执行：",s.toString());
    }


    /**
     * 输入提示结果的回调
     * @param tipList
     * @param rCode
     */
    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getDistrict());
                listString.add(map);
            }
            SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
                    new String[] {"name","address"}, new int[] {R.id.poi_field_id, R.id.poi_value_id});

            minputlist.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

        } else {
            ToastUtil.showerror(this.getApplicationContext(), rCode);
        }

    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }
    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(address.getLatLonPoint()), 15));
                serMarker.setPosition(convertToLatLng(address
                        .getLatLonPoint()));
                addr = address.getFormatAddress();
                location_x = address.getLatLonPoint().getLatitude();
                location_y = address.getLatLonPoint().getLongitude();
                String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
                ToastUtil.show(MapActivity.this, addressName);
            } else {
                ToastUtil.show(MapActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }


    /**
     * 检查权限
     *
     * @param
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        //获取权限列表
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            //list.toarray将集合转化为数组
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                showMissingPermissionDialog();              //显示提示信息
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Request");
        builder.setMessage("Permissions");

        // 拒绝, 退出应用
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("Setting",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
