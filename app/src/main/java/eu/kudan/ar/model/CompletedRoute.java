package eu.kudan.ar.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Mathieu Virsolvy on 03/05/2017.
 */

public class CompletedRoute {

    private int id;
    private Date date;
    private float distance;
    private Timestamp duration;
    private int user;

    public CompletedRoute(int id, Date date, String distance, Timestamp duration, int user) {
        this.id = id;
        this.date = date;
        this.distance = Float.parseFloat(distance);
        this.duration = duration;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public float getDistance() {
        return distance;
    }

    public Timestamp getDuration() {
        return duration;
    }

    public int getUser() {
        return user;
    }
}
