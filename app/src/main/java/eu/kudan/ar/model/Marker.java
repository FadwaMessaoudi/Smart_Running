package eu.kudan.ar.model;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by Mathieu Virsolvy on 19/04/2017.
 */

public class Marker implements Serializable{
    private String id, title, description, fullDescription, clue;
//    private LatLng  location;
    private double latitude = 0;
    private double longitude = 0;
    private float zoneRadius;
    private Bitmap targetImage, targetTexture;
    final private double stdLat = 45.783; // deviation < 1/1000
    final private double stdLon = 4.875; // deviation < 1/2000


    public Marker(String id, String title, String description, String fullDescription, LatLng location, float zoneRadius, String clue, Bitmap targetImage, Bitmap targetTexture) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fullDescription = fullDescription;
//        this.location = location;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.zoneRadius = zoneRadius;

        this.clue = clue;
        this.targetImage=targetImage;
        this.targetTexture=targetTexture;

    }

    public String getClue() {
        return clue;
    }

    public Bitmap getTargetImage() {
        return targetImage;
    }

    public Bitmap getTargetTexture() {
        return targetTexture;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFullDescription() {
        return fullDescription;
    }

//    public LatLng getLocation() {
//        return location;
//    }
    public double getLatitude() {
        return  latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getZoneRadius() {
        return zoneRadius;
    }

    public Marker(String id) {
        this.id = id;
    }

    public void initiate(String title, String description, String fullDescription, String lat, String lon, String zoneRadius, String clue, Bitmap targetImage, Bitmap targetTexture){
        this.title = title;
        this.description = description;
        this.fullDescription = fullDescription;
//        this.location = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        this.latitude = Double.parseDouble(lat);
        this.longitude = Double.parseDouble(lon);
        this.zoneRadius = Float.parseFloat(zoneRadius);

        this.clue = clue;
        this.targetImage=targetImage;
        this.targetTexture=targetTexture;
    }

    public String toString() {
        String markerInfo = "";
//        markerInfo += "getId : " + this.getId() + "\n";
//        markerInfo += "getTitle : " + this.getTitle() + "\n";
//        markerInfo += "getDescription : " + this.getDescription() + "\n";
//        markerInfo += "getFullDescription : " + this.getFullDescription() + "\n";

        if (this.getLatitude() == 0 || this.getLongitude() == 0) {
            this.setLocation(generateLocation());
        }
        markerInfo += "getLocation : " + this.getLatitude() + "," + this.getLongitude() + "\n";

//        markerInfo += "getRadius : " + this.getZoneRadius() + "\n";
//        markerInfo += "getClue : " + this.getClue() + "\n";
        markerInfo += "**************************** \n";
        return markerInfo;
    }

    private void setLocation(LatLng ll) {
        this.latitude = ll.latitude;
        this.longitude = ll.longitude;
    }

    private LatLng generateLocation () {
        double latitude = stdLat + Math.random() / 1000.0;
        double longitude = stdLon + Math.random() / 2000.0;

        DecimalFormat df = new DecimalFormat(".000000");
        latitude = Double.parseDouble(df.format(latitude));
        longitude = Double.parseDouble(df.format(longitude));
        LatLng ll = new LatLng(latitude, longitude);
        return ll;
    }
}
