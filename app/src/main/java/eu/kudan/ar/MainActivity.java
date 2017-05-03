package eu.kudan.ar;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.kudan.ar.model.Marker;
import eu.kudan.ar.model.Route;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list2);

        ListView listView = (ListView) findViewById(R.id.routeList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


        RestAPI rest=RestAPI.getINSTANCE();
        ArrayList<Route> list =rest.getRoutesList();


        RoutesAdapter adapter = new RoutesAdapter(this,list);
        listView.setAdapter(adapter);

    }
}
