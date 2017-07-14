package ourcompany.mylovepet.daummap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;

import ourcompany.mylovepet.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
/**
 * Created by KDM on 2017-07-12.
 */

public class GpsMapActivity extends AppCompatActivity implements MapView.MapViewEventListener {

    private static final String LOG_TAG = "SearchDemoActivity";
    View mCalloutBalloon;
    public Context mContext;
    private ActionBar actionBar;
    private MapView mMapView;
    // private EditText mEditTextQuery;
    private Button buttonfind;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    double longitude, latitude;

    LocationManager manager;
    Location location;
    static int result=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_map);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        buttonfind = (Button) findViewById(R.id.buttonfind);

        mMapView = (MapView) findViewById(R.id.gps_map_view);
        mMapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMapView.zoomIn(true);
        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapType(MapView.MapType.Standard);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar =  getSupportActionBar();
        actionBar.setTitle("위치 찾기");
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonfind.setOnClickListener(new View.OnClickListener() { // 펫 위치 찾기
            @Override

            public void onClick(View v) {
                mMapView.removeAllPOIItems();
                mMapView.removeAllCircles();
                hideSoftKeyboard(); // 키보드 숨김
                addCircles();

                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(35.896422, 128.623446); // 강아지 위치
/*        mMapView.setMapCenterPoint(mapPoint,true);*/
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName("Alaskan Malamute"); // 강아지 이름

                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mMapView.addPOIItem(marker); // 마커 추가


            }
        });
    }
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.gps_map_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void addCircles() {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        MapCircle circle1 = new MapCircle(

                MapPoint.mapPointWithGeoCoord(latitude,longitude), // center
                500, // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(128, 0, 255, 0) // fillColor
        );

        circle1.setTag(1234);
        mMapView.addCircle(circle1);

        // 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정.
        MapPointBounds[] mapPointBoundsArray = { circle1.getBound()};
        MapPointBounds mapPointBounds = new MapPointBounds(mapPointBoundsArray);
        int padding = 50; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(mEditTextQuery.getWindowToken(), 0);
    }

    public void onMapViewInitialized(MapView mapView) {


        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
        double longitude,latitude;
        latitude = geoCoordinate.latitude;
        longitude = geoCoordinate.longitude;
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true);
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 4,true);


        // String query = mEditTextQuery.getText().toString();

        int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1;
        String apikey = MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY;


    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(GpsMapActivity.this, text, LENGTH_SHORT).show();
            }
        });
    }

    Item item;

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
    }

}
