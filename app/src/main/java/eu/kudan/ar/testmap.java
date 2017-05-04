package eu.kudan.ar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import a4if_insa.mapsa.test.R;

public class testmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider; // location provider
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ArrayList<eu.kudan.ar.model.Marker> markers;
    List<LatLng> centers;
    String nearestTarget = "";
    Double nearestDist = 6371000.0; // initialize as radius of the Earth

    //    static int meCounter;
    final static int FIND_BALISE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmap);

        if(getIntent().getExtras() != null)
        {
            markers = (ArrayList<eu.kudan.ar.model.Marker>)getIntent().getExtras().get("locatedMarkers");
//            markers2 = (ArrayList<eu.kudan.ar.model.Marker>)getIntent().getExtras().get("Markers");
//            markers3 = (ArrayList<eu.kudan.ar.model.Marker>)getIntent().getSerializableExtra("locatedMarkers");
//            markers4 = (ArrayList<eu.kudan.ar.model.Marker>)getIntent().getSerializableExtra("Markers");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // instanciate locaionManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get all available providers
        List<String> providerList = locationManager.getProviders(true);
        //LocationManager.Provider
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            // we appreciate here usage of GPS
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // when there is no available location provider
            Toast.makeText(testmap.this, "please check if you have available location provider", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // print device's postion coordinate
            String firstInfo = "first response of your request";
            Toast.makeText(this, firstInfo, Toast.LENGTH_LONG).show();
        } else {
            String info = "Patiently waiting for 10sec and we will get you on the map.";
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        }

        // renew the postion coordinate
        locationManager.requestLocationUpdates(provider, 10 * 1000, 1,locationListener);

        FloatingActionButton rabtn = (FloatingActionButton) findViewById(R.id.ra_btn);
        rabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindBalise.class);

                ApplicationInfo appInfo = getApplicationInfo();
                int resId = getResources().getIdentifier("balise_xing.jpg", "drawable", appInfo.packageName);
                intent.putExtra("picture", BitmapFactory.decodeResource(getResources(),resId));

                startActivityForResult(intent, FIND_BALISE_REQUEST);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            // remove the listener when program's exiting
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int poiColor= Color.argb(100,253,48,152);
        int poiStrokeColor = Color.argb(200,253,48,152);

        int startColor = Color.argb(100,68,251,0);
        int startStrokeColor=Color.argb(200,68,251,0);

        // Add a marker in La Doua and move the camera
        LatLng lyon = new LatLng(45.782969, 4.873652);
        mMap.addMarker(new MarkerOptions().position(lyon).title("La Doua")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon,15));

        CircleOptions circleOptions3 = new CircleOptions()
                .center(lyon)
                .radius(50) // In meters
                .fillColor(startColor)
                .strokeColor(startStrokeColor);
        Circle circle3= mMap.addCircle(circleOptions3);

        CircleOptions co = new CircleOptions()
                .center(lyon)
                .radius(50) // In meters
                .fillColor(startColor)
                .strokeColor(startStrokeColor);

        for (int i = 0; i < markers.size(); i++) {
            double lat = markers.get(i).getLatitude();
            double lon = markers.get(i).getLongitude();
            LatLng location = new LatLng(lat, lon);
            co.center(location);

            Circle c = mMap.addCircle(co);
            mMap.addMarker(new MarkerOptions().position(location).
                    title("Target" + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();

            centers.add(location);
        }

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(45.784031, 4.880026))
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);
        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mMap.addMarker(new MarkerOptions().position(new LatLng(45.784031, 4.880026)).title("Matthew").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();

        CircleOptions circleOptions2 = new CircleOptions()
                .center(new LatLng(45.784730, 4.872096))
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);

        mMap.addMarker(new MarkerOptions().position(new LatLng(45.784730, 4.872096)).title("soDomy").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();

        Circle circle2 = mMap.addCircle(circleOptions2);
        circle2.setFillColor(startColor);
    }

    public void refreshMap (GoogleMap myGoogleMap, double latitude, double longitude) {
        int poiColor= Color.argb(100,253,48,152);
        int poiStrokeColor = Color.argb(200,253,48,152);
        LatLng currentPosition = new LatLng(latitude, longitude);

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(currentPosition)
                .radius(50) // In meters
                .fillColor(poiColor)
                .strokeColor(poiStrokeColor);
        // Get back the mutable Circle
        Circle circle = myGoogleMap.addCircle(circleOptions);

        for (int i = 0; i < centers.size(); i++) {

        }
//        myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Me" + meCounter).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
//        meCounter += 1;
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location location) {
            // code run when device's position changed
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String info = "refresh every moving 10 seconds \n" + sdf.format(new Date())
                    + ", \n longitude : " + location.getLongitude() + ", \n latitude : "
                    + location.getLatitude();
            Toast.makeText(testmap.this, info, Toast.LENGTH_LONG).show();
//            currentLat = lat;
//            currentLon = lon;
            refreshMap(mMap, lat, lon);
        }
    };

    public static boolean validateCheckPoints (LatLng latlng, Circle c) {
        double r = c.getRadius();
        double distance = getDistance(latlng, c.getCenter());
        return (r > distance);
    }

    public static double getDistance(LatLng first, LatLng second) {
        double lat1 = (Math.PI / 180) * first.latitude;
        double lat2 = (Math.PI / 180) * second.latitude;
        double lon1 = (Math.PI / 180) * first.longitude;
        double lon2 = (Math.PI / 180) * second.longitude;

        //radious of the Earth
        final double R = 6371.004;
        //calculate linear distance between two points
        double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        //returned value is in meters
        return distance * 1000;
    }
}