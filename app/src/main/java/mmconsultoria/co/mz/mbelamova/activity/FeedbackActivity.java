package mmconsultoria.co.mz.mbelamova.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.activity.LoginActivity;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.feedbackMsg)
    public TextView texto;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);


        //Setting external font
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Gotham Light.otf");
        texto.setTypeface(typeface);


    }

    @OnClick(R.id.enviarOpiniao)
    public void sendFeedback(View view){
        // TODO: 12/27/2018 Remover essa frase de meu pu2
        Toast.makeText(this, "Nao sei porque Ele mandou isso para a login Activity", Toast.LENGTH_SHORT).show();
        startMyActivity(LoginActivity.class);
    }

}
