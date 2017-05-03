package eu.kudan.ar;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        for (int i = 0; i < markers.size(); i++) {

        }
//==============================================================

        Button testmapBtn = (Button) findViewById(R.id.totestmap);
        testmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), testmap.class);
//                startActivityForResult(intent, FIND_BALISE_REQUEST);
                startActivity(intent);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
      });
    }


}
