package eu.kudan.ar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SigninSignup extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        TextView t = (TextView) findViewById(R.id.link_signup);
        final EditText textemail = (EditText) findViewById(R.id.input_email);
        final EditText textpassword = (EditText) findViewById(R.id.input_password);
        Button buttonlogin = (Button) findViewById(R.id.button);

        t.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                RestAPI singleton = RestAPI.getINSTANCE();

                String token = singleton.connect(textemail.getText().toString(),textpassword.getText().toString(),"");
                if(token.equals("error")){
                    Toast.makeText(SigninSignup.this, "impossible to connect", Toast.LENGTH_LONG)
                            .show();
                }else if(token.equals("bad auth")) {
                    Toast.makeText(SigninSignup.this, "bad auth", Toast.LENGTH_LONG)
                            .show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}