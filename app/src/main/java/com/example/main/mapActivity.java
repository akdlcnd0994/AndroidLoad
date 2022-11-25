package com.example.main;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.ExecutionException;


public class mapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private static final String LOG_TAG = "mapActivity";
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Button Search, post, check, up, reportBtn, repBtn;
    ImageButton Write, x, refresh, x2, x3, x4;
    String nickname, address, revNum;
    EditText EditTitle, EditContent, EditAdd, repContent, repTitle;
    TextView text, textv, titleday, cont;
    Double lon = 700.0, lat = 700.0;
    ConstraintLayout listLayout, f, paper,report;
    LinearLayout list;



    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    String[] info = new String[3];
    ArrayList<String[]> dbinfo = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), true);


        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }
        marking();
        init();
    }

    void marking() {
        String[] sp;
        String[] temp;

        String result;
        reviewListActivity task = new reviewListActivity();


        try {
            result = task.execute().get();
            dbinfo.clear();
            if(!result.equals("작성된 글이 없습니다.")) {
                sp = result.split("@#@~#!#@#~#%");
                for (int i = 0; i < sp.length; i++) {
                    temp = sp[i].split("§§§§§§§%~#");

                    dbinfo.add(new String[]{temp[0],temp[1],temp[2],temp[3],temp[4], temp[5], temp[6], temp[7], temp[8]});

                    int n = Integer.parseInt(temp[0]); // 리뷰번호
                    String title = temp[1]; //제목
                    String content = temp[2]; //내용
                    Double lat = Double.valueOf(temp[3]); //위도
                    Double lon = Double.valueOf(temp[4]); //경도
                    String nick = temp[5];
                    String add = temp[6];
                    String fNow = temp[7];
                    int up = Integer.parseInt(temp[8]); //추천수
                    
                    MapPOIItem marker = new MapPOIItem();
                    MapPoint mp = MapPoint.mapPointWithGeoCoord(lon, lat);

                    marker.setItemName(add);
                    marker.setTag(0);
                    marker.setMapPoint(mp);
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);




                    mapView.addPOIItem(marker);
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ;

    void init() {
        Search = (Button) findViewById(R.id.searchBtn);
        repBtn = (Button) findViewById(R.id.repBtn);
        up = (Button) findViewById(R.id.upBtn);
        reportBtn = (Button) findViewById(R.id.reportBtn);
        check = (Button) findViewById(R.id.checkAdd);
        post = (Button) findViewById(R.id.regPost);
        text = (TextView) findViewById(R.id.address);
        titleday = (TextView) findViewById(R.id.titleday);
        cont = (TextView) findViewById(R.id.cont);
        refresh = (ImageButton) findViewById(R.id.refresh);
        Write = (ImageButton) findViewById(R.id.writeButton);
        x = (ImageButton) findViewById(R.id.XButton);
        x2 = (ImageButton) findViewById(R.id.XButton2);
        x3 = (ImageButton) findViewById(R.id.XButton3);
        x4 = (ImageButton) findViewById(R.id.XButton4);
        f = (ConstraintLayout) findViewById(R.id.frame);
        EditTitle = (EditText) findViewById(R.id.EditTitle);
        EditContent = (EditText) findViewById(R.id.EditContent);
        EditAdd = (EditText) findViewById(R.id.EditAdd);
        listLayout = (ConstraintLayout) findViewById(R.id.listLayout);
        EditText EditSearch = (EditText) findViewById(R.id.EditSearch);
        textv = (TextView)findViewById(R.id.textv);
        f.setVisibility(View.INVISIBLE);
        list = (LinearLayout) findViewById(R.id.list);
        paper = (ConstraintLayout) findViewById(R.id.paper);
        paper.setVisibility(View.INVISIBLE);
        repContent =(EditText) findViewById(R.id.repContent);
        repTitle =(EditText) findViewById(R.id.repTitle);
        report = (ConstraintLayout) findViewById(R.id.report);
        report.setVisibility(View.INVISIBLE);


        Search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                geoActivity geo = new geoActivity(EditSearch.getText());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                info = geo.getInfo();

                if (info[0] == null) {
                    Toast.makeText(getApplicationContext(), "주소를 찾지 못했습니다. 다시 시도하거나 주소를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    showMsg(info);
                }
            }
        });


        refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (listLayout.getVisibility() == View.INVISIBLE) {
//                    listLayout.setVisibility(View.VISIBLE);
//                } else {
//                    listLayout.setVisibility(View.INVISIBLE);
//                }
                marking();
            }
        });


        post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;
                if (lat != 700.0) {

                    nickname = getIntent().getStringExtra("nickname");
                    String title = String.valueOf(EditTitle.getText());
                    String content = String.valueOf(EditContent.getText());
                    String latitude = String.valueOf(lat);
                    String longitude = String.valueOf(lon);
                    reviewActivity task = new reviewActivity();
                    try {
                        result = task.execute(title, content, longitude, latitude, nickname, address).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                geoActivity geo = new geoActivity(EditAdd.getText());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                info = geo.getInfo();

                if (info[0] == null) {
                    Toast.makeText(getApplicationContext(), "주소를 찾지 못했습니다. 다시 시도하거나 주소를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    checkMsg(info);
                }
            }
        });

        Write.setOnClickListener(new OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         f.bringToFront();
                                         if (f.getVisibility() == View.INVISIBLE) {
                                             f.setVisibility(View.VISIBLE);
                                         } else {
                                             f.setVisibility(View.INVISIBLE);
                                         }
                                     }
                                 }
        );
        x.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setVisibility(View.INVISIBLE);
            }
        });
        x2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listLayout.setVisibility(View.INVISIBLE);
                paper.setVisibility(View.INVISIBLE);
            }
        });
        x3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                paper.setVisibility(View.INVISIBLE);
            }
        });
        x4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                report.setVisibility(View.INVISIBLE);
            }
        });

        up.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String result;

                etcActivity task = new etcActivity();
                try {
                    result = task.execute("1", revNum, getIntent().getStringExtra("nickname"), "", "").get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        reportBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                report.setVisibility(View.VISIBLE);

            }
        });

        repBtn.setOnClickListener(new View.OnClickListener(){
            String result;
            @Override
            public void onClick(View v) {
                LocalDateTime now = LocalDateTime.now();
                String title = String.valueOf(repTitle.getText());
                String content = String.valueOf(repTitle.getText());
                String fNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));

                //신고하기 누르면 간단하게 나오는 창을 만들고 해당 정보들 가져오기
                etcActivity task = new etcActivity();
                try {
                    result = task.execute("2", title, content, fNow, revNum).get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                report.setVisibility(View.INVISIBLE);
            }
        });

    }


    public void checkMsg(String info[]) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage(info[0] + "\n 찾으시는 주소가 맞습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lat = Double.parseDouble(info[1]);
                lon = Double.parseDouble(info[2]);
                address = info[0];
                text.setText("현재주소\n" + info[0]);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "주소를 조금 더 상세히 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void showMsg(String info[]) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage(info[0] + "\n 찾으시는 주소가 맞습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MapPoint mp = MapPoint.mapPointWithGeoCoord(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
                //주소로 이동처리
                mapView.setMapCenterPoint(mp, true);

            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "주소를 조금 더 상세히 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        //주소로 이동처리

        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();

        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        //여기 수정해야됨 !!!
        list.removeAllViewsInLayout();
        for(int i=0;i<dbinfo.size();i++) {
            if (dbinfo.get(i)[6].equals(mapPOIItem.getItemName())){
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(800,200);
                pa.setMarginStart(70);
                System.out.println("번호 = "+dbinfo.get(i)[0] + "제목 = " + dbinfo.get(i)[1] + "내용 = " + dbinfo.get(i)[2]);
                //버튼 동적생성하고 list 비져블 처리
                listLayout.bringToFront();
                listLayout.setVisibility(View.VISIBLE);
                Button b = new Button(this);
                b.setText("제목 : " + dbinfo.get(i)[1] + "\n작성자 : " + dbinfo.get(i)[5] + "\t추천 :" + dbinfo.get(i)[8] +"\n작성일 : " + dbinfo.get(i)[7]);
                int tempI = i;
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        revNum = String.valueOf(dbinfo.get(tempI)[0]);
                        titleday.setText("제목 : " + dbinfo.get(tempI)[1] + "\n작성자 : " + dbinfo.get(tempI)[5] + "\n작성일 : " + dbinfo.get(tempI)[7]);
                        cont.setText(dbinfo.get(tempI)[2]);
                        paper.bringToFront();
                        paper.setVisibility(View.VISIBLE);

                    }
                });

                list.addView(b, pa);


            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}