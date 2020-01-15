package com.sfmap.map.demo.basic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sfmap.api.maps.CameraUpdateFactory;
import com.sfmap.api.maps.MapController;
import com.sfmap.api.maps.MapOptions;
import com.sfmap.api.maps.MapView;
import com.sfmap.api.maps.model.CameraPosition;
import com.sfmap.api.maps.model.LatLng;
import com.sfmap.api.maps.model.LatLngBounds;
import com.sfmap.api.maps.model.Marker;
import com.sfmap.api.maps.model.MarkerOptions;
import com.sfmap.api.maps.model.Poi;
import com.sfmap.api.maps.model.TileOverlayOptions;
import com.sfmap.api.services.core.SearchException;
import com.sfmap.api.services.poisearch.PoiItem;
import com.sfmap.api.services.poisearch.PoiSearch;
import com.sfmap.map.demo.R;
import com.sfmap.map.demo.util.AppInfo;
import com.sfmap.map.demo.util.ToastUtil;

import java.util.List;

import timber.log.Timber;

/**
 * sfmapMap中介绍如何显示一个基本地图
 */
public class BasicMapActivity extends Activity implements MapController.OnMapLoadedListener, MapController.OnCameraChangeListener, MapController.OnPOIClickListener {
	private String TAG = BasicMapActivity.class.getSimpleName();
	private MapView mapView;
	private MapController mMapController;
	private boolean boundSet;
	View infoWindow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basicmap);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须调用
		init();
	}
//113.667339,34.760746
	/**
	 * 初始化地图控制器
	 */
	private void init() {
		mMapController = mapView.getMap();
		//设置比例尺绝对位置， 第一个参数表示横坐标，第二个表示纵坐标
//		mMapController.setViewPosition(MapOptions.POSITION_SCALE,100,800);
		mMapController.getUiSettings().setScaleControlsEnabled(true);
//		mMapController.getUiSettings().setLogoPosition(-50);
		mMapController.setOnMapLoadedListener(this);
        mMapController.setOnPOIClickListener(this);
		String loc = AppInfo.getMapCenterLocation(this);
		String[] locs = loc.split(",");
		mMapController.moveCamera(
				CameraUpdateFactory.newLatLngZoom(
						new LatLng(Double.parseDouble(locs[0]),Double.parseDouble(locs[1])), //28.6880478, 115.852852
						18)
		);
		mMapController.setOnCameraChangeListener(this);
		Timber.v("Start loading map...");

	}
	/**
	 * 方法重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onMapLoaded() {
		Timber.v("onMapLoaded() callback called");
		ToastUtil.show(this, "地图加载完成");
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {

	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		Timber.v("OnCameraChangeFinish:%s", cameraPosition.toString());
//		if(!boundSet) {
//			LatLngBounds bounds = LatLngBounds.builder()
//					.include(new LatLng(22.382, 114.188))
//					.include(new LatLng(22.382, 114.188))
//					.include(new LatLng(22.382, 114.188))
//					.include(new LatLng(22.382, 114.188))
//					.include(new LatLng(0, 0))
//					.build();
//			mMapController.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));
//			boundSet = true;
//		}
	}

    @Override
    public void onPOIClick(final Poi poi) {
        ToastUtil.show(getApplicationContext(),"poiid:"+poi.getPoiId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                PoiSearch poiSearch = new PoiSearch(getApplicationContext());
                List<PoiItem> results= null;
                try {
                    results = poiSearch.searchPOIId(poi.getPoiId(),"410100");
                } catch (SearchException e) {
                    e.printStackTrace();
                }
                if(results == null){
					Log.i(TAG,"results:空");
				}else {
					Log.i(TAG,"results:"+  results.size());
				}
            }
        }).start();
	}
}
