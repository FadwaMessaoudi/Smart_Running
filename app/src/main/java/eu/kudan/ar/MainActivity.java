package eu.kudan.ar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img_course=(ImageView) findViewById(R.id.img_course);
        ImageView img_profil=(ImageView) findViewById(R.id.img_profil);
        ImageView img_hist=(ImageView) findViewById(R.id.img_hist);
        ImageView img_stat=(ImageView) findViewById(R.id.img_stat);
        ImageView img_setting=(ImageView) findViewById(R.id.img_setting);
        ImageView img_logout=(ImageView) findViewById(R.id.img_logout);
        img_course.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RunsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
