package mmconsultoria.co.mz.mbelamova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;

public class MainActivity extends BaseActivity {

    Button btnLogin;
    Animation fromButtonAnimation,fromTitleAnimation ;
    TextView titulo ;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




     /*   requestWindowFeature(1);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
*/

        btnLogin = findViewById(R.id.btn_login);


        titulo = findViewById(R.id.text_view_title);

        fromButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.from_button);
        fromTitleAnimation = AnimationUtils.loadAnimation(this, R.anim.from_title);

        btnLogin.setAnimation(fromButtonAnimation);

        titulo.setAnimation(fromTitleAnimation);

        btnLogin.setOnClickListener( event ->{
            startMyActivity(LoginActivity.class);
        });





    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //signIn();
    }
}
