package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.tanyixiu.mimo.R;

import java.util.List;

public class FindUFragment extends Fragment {

    private View contentView;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocationManager mLocationManager;

    public static FindUFragment getNewInstance() {
        return new FindUFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_find_u, container, false);
        initViews(contentView);
        return contentView;
    }

    private void initViews(View rootView) {
        mMapView = (MapView) rootView.findViewById(R.id.fu_mapview);
        mBaiduMap = mMapView.getMap();

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        mBaiduMap.setMyLocationEnabled(true);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);



        Location currentLocation = getCurrentLocation();
        if (null == currentLocation) {
            return;
        }
        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(currentLocation.getAccuracy())
                .direction(100)
                .latitude(currentLocation.getLatitude())
                .longitude(currentLocation.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(locationData);
    }

    private Location getCurrentLocation() {
        List<String> providerList = mLocationManager.getProviders(true);
        for (String provider : providerList) {
            Location currentLocation = mLocationManager.getLastKnownLocation(provider);
            if (null != currentLocation) {
                return currentLocation;
            }
        }
        return null;
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
