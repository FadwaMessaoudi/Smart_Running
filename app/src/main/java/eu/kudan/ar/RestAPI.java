package eu.kudan.ar;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import eu.kudan.ar.API.CompletedRoutesAPI;
import eu.kudan.ar.API.DownloadImageTask;
import eu.kudan.ar.API.MarkersAPI;
import eu.kudan.ar.API.RoutesAPI;
import eu.kudan.ar.API.SignInAPI;
import eu.kudan.ar.model.CompletedRoute;
import eu.kudan.ar.model.Marker;
import eu.kudan.ar.model.Route;


/**
 * Created by Mathieu Virsolvy on 14/04/2017.
 */

public class RestAPI {

    /**
     * authentication token
     */
    private String token;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private String name, email, pwd;

    private RestAPI() {
        name = "";
        email = "";
        pwd = "";
        token = "";
    }


    /**
     * the singleton
     */
    private static RestAPI INSTANCE = new RestAPI();

    /**
     * get the singleton
     *
     * @return the singleton
     */
    public static RestAPI getINSTANCE() {
        return INSTANCE;
    }

    /**
     * connect to the server and get the authentication token
     *
     * @param pName  username of the user ( not required if email is given)
     * @param pPwd   password (required)
     * @param pEmail email of the user (not required if username is given)
     * @return 0 if everything ran smoothly. >0 otherwise.
     */
    public String connect(String pName, String pPwd, String pEmail) {

        SignInAPI signin = new SignInAPI();
        try {
            signin.execute(pName, pEmail, pPwd);
            token = signin.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "error";
        }
        if (!token.equals("error") && !token.equals("bad auth")) {
            name = pName;
            email = pEmail;
            pwd = pPwd;
        }
        return token;
    }

    /**
     * make a get request to the server
     *
     * @return the list of all routes available
     */
    public ArrayList<Route> getRoutesList() {

        ArrayList<Route> list = new ArrayList<Route>();

        RoutesAPI routesApi = new RoutesAPI();
        routesApi.execute(token);
        try {
            String output = routesApi.get();
            if (output.equals("error") || output.equals("auth error")) {
                return list;
            }
            JSONArray response = new JSONArray(output);

            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                JSONArray listMarkers = item.getJSONArray("markers");

                ArrayList<Marker> markers = new ArrayList<Marker>();

                for (int m = 0; m < listMarkers.length(); m++) {
                    Marker newMarker = new Marker(listMarkers.getString(m));
                    markers.add(newMarker);

                }

                Date published = dateFormat.parse(item.getString("date_published"));
                Date lastUpdated = dateFormat.parse(item.getString("last_updated"));
                Route.Mode routeMode;
                switch (item.getString("mode")) {
                    case "TOURISTIC":
                        routeMode = Route.Mode.TOURISTIC;
                        break;
                    case "SPORTY":
                        routeMode = Route.Mode.SPORTY;
                        break;
                    default:
                        routeMode = Route.Mode.UNSPECIFIED;
                        break;

                }

                DownloadImageTask imageDownload = new DownloadImageTask();
                imageDownload.execute(item.getString("thumbnail"));

                Bitmap thumbnail = imageDownload.get();

                Route route = new Route(item.getString("id"), item.getString("title"), item.getString("description"), item.getInt("difficulty"), item.getDouble("rating"), routeMode, published, lastUpdated, markers, thumbnail);

                list.add(route);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Marker getMarker(Marker marker) {

        MarkersAPI markersApi = new MarkersAPI();
        markersApi.execute(token, marker.getId());
        try {
            String output = markersApi.get();
            if (output.equals("error") || output.equals("auth error")) {
                return null;
            }
            JSONObject item = new JSONObject(output);

            DownloadImageTask imageDownload = new DownloadImageTask();
            imageDownload.execute(item.getString("target_image"));
            Bitmap targetImage = imageDownload.get();

            //imageDownload.execute(item.getString("target_texture"));
           // Bitmap targetTexture = imageDownload.get();

//            marker.initiate(item.getString("title"), item.getString("description"), item.getString("full_description"), item.getString("latitude"), item.getString("longitude"),item.getString("zone_radius"), item.getString("clue"), targetImage );
            marker.initiate(item.getString("title"), item.getString("description"), item.getString("full_description"), item.getString("latitude"), item.getString("longitude"),item.getString("zone_radius"), item.getString("clue"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marker;
    }

    public ArrayList<CompletedRoute> getCompletedRoutes() {
        ArrayList<CompletedRoute> list = new ArrayList<CompletedRoute>();

        CompletedRoutesAPI completedRoutesApi = new CompletedRoutesAPI();
        completedRoutesApi.execute(token);
        try {
            String output = completedRoutesApi.get();
            if (output.equals("error") || output.equals("auth error")) {
                return list;
            }
            JSONArray response = new JSONArray(output);

            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                Date date = dateFormat.parse(item.getString("when"));

                Timestamp duration = new Timestamp(item.getLong("duration"));

                CompletedRoute newRoute = new CompletedRoute( item.getInt("id"), date, item.getString("distance"),  duration, item.getInt("user"));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}



