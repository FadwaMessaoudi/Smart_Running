package eu.kudan.ar;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import eu.kudan.ar.model.Marker;
import eu.kudan.ar.model.Route;

public class RunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        TextView newChallangeChosen = (TextView) findViewById(R.id.viewMarkerInfo);
        newChallangeChosen.setMovementMethod(ScrollingMovementMethod.getInstance());

        Bundle extras = getIntent().getExtras();
//=========Route DATAS==========================================
        String id=(String)extras.get("description");
        String title=(String)extras.get("title");
        Route.Mode mode=(Route.Mode)extras.get("mode");
        int difficulty=(int)extras.get("difficulty");
        RestAPI rapi = RestAPI.getINSTANCE();

        final ArrayList<Marker> markers = (ArrayList<Marker>)extras.getSerializable("markers");

        for (int i = 0; i < markers.size(); i++) {
            rapi.getMarker(markers.get(i));
            String mInfo = markers.get(i).toString();
            newChallangeChosen.append(mInfo);
        }

//==============================================================

        Button testmapBtn = (Button) findViewById(R.id.totestmap);
        testmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), testmap.class);
//                startActivityForResult(intent, FIND_BALISE_REQUEST);
//                intent.putExtra("locatedMarkers", markers);
                intent.putExtra("locatedMarkers",(Serializable) markers);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
      });
    }


}
