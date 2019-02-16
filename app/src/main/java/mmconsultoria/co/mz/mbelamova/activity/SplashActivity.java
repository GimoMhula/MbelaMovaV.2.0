package mmconsultoria.co.mz.mbelamova.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModelProviders;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.view_model.AuthModel;

public class SplashActivity extends BaseActivity {

    //Splash_Screen
    private final static int SPLASH_TIME_OUT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //Splash Screen
        new Handler().postDelayed(() -> {
            boolean userSignedIn = ViewModelProviders.of(this).get(AuthModel.class).isUserSignedIn();

            if (!userSignedIn) {
                startMyActivity(MainActivity.class);
            } else startMyActivity(ClientMapActivity.class);

            finish();

        }, SPLASH_TIME_OUT);
    }
}
