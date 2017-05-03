package eu.kudan.ar.model;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mathieu Virsolvy on 19/04/2017.
 */

public class Marker {
    private String id, title, description, fullDescription, clue;
    private LatLng  location;
    private float zoneRadius;
    private Bitmap targetImage, targetTexture;


    public Marker(String id, String title, String description, String fullDescription, LatLng location, float zoneRadius, String clue, Bitmap targetImage, Bitmap targetTexture) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fullDescription = fullDescription;
        this.location = location;
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

    public LatLng getLocation() {
        return location;
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
        this.location = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        this.zoneRadius = Float.parseFloat(zoneRadius);

        this.clue = clue;
        this.targetImage=targetImage;
        this.targetTexture=targetTexture;
    }
}
