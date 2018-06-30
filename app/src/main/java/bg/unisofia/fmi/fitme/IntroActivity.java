package bg.unisofia.fmi.fitme;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class IntroActivity extends AppCompatActivity {

    private static int INTRO_SPLASH_TIME_OUT = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, INTRO_SPLASH_TIME_OUT);

    }
}
