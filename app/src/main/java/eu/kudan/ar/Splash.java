package eu.kudan.ar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Splash extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);

    Thread myThread = new Thread(){
        @Override
        public void run() {
            try {
                sleep(3000);
                Intent intent = new Intent(getApplicationContext(),SigninSignup.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    myThread.start();
}
}