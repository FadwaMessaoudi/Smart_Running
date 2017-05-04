package eu.kudan.ar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    ArrayList<LatLng> centers = new ArrayList<LatLng>();
    String nearestTarget = "";
    Double nearestDist = 6371000.0; // initialize as radius of the Earth
    final int criticalDist = 300;

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
                intent.putExtra("picture", checkout());
                startActivityForResult(intent, FIND_BALISE_REQUEST);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        final FloatingActionButton vbtn = (FloatingActionButton) findViewById(R.id.validatebtn);
        vbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int index = nearestTarget.charAt(6);
//                markers.get(index).
                vbtn.setVisibility(View.INVISIBLE);
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
            centers.add(location);

            Circle c = mMap.addCircle(co);
            mMap.addMarker(new MarkerOptions().position(location).
                    title("Target" + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();

        }
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

            if (nearestTarget != "") {
                String info2 = "Your nearest target is" + nearestTarget +
                        "\n The distance is " + nearestDist.intValue() + "m";
                Toast.makeText(testmap.this, info2, Toast.LENGTH_LONG).show();
            }
            getnearest(lat, lon);
            refreshMap(mMap, lat, lon);
        }
    };

    public void getnearest(double lat, double lon) {
        LatLng currentPosition = new LatLng(lat, lon);

        for (int i = 0; i < centers.size(); i++) {
            if (nearestDist > getDistance(currentPosition, centers.get(i))){
                nearestDist = getDistance(currentPosition, centers.get(i));
                nearestTarget = "Target" + i;
            }
            if (nearestDist < criticalDist) {
                findViewById(R.id.validatebtn).setVisibility(View.VISIBLE);
            }
        }
    }

    public File checkout() {
        //configure inJustDecodeBoudns=true to acquire the size of resource image
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        File file;
        switch (nearestTarget) {
            case "Target0" :
                Bitmap bm0 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_0.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm0.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target1" :
                Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_1.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target2" :
                Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_2.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm2.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target3" :
                Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_3.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm3.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target4" :
                Bitmap bm4 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_4.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm4.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target5" :
                Bitmap bm5 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_5.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm5.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target6" :
                Bitmap bm6 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_6.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm6.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Target7" :
                Bitmap bm7 = BitmapFactory.decodeResource(getResources(), R.drawable.balise_1, opt);

                file = new File("/File/balise_7.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm7.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default :
                return null;
        }

        return file;
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