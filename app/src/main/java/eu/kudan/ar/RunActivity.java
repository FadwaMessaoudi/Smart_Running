package eu.kudan.ar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import eu.kudan.ar.model.Marker;
import eu.kudan.ar.model.Route;

public class RunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Bundle extras = getIntent().getExtras();
//=========Route DATAS==========================================
        String id=(String)extras.get("description");
        String title=(String)extras.get("title");
        Route.Mode mode=(Route.Mode)extras.get("mode");
        int difficulty=(int)extras.get("difficulty");

        ArrayList<Marker> markers = (ArrayList<Marker>)extras.get("markers");
//==============================================================
    }


}
