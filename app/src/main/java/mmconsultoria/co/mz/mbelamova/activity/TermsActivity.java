package mmconsultoria.co.mz.mbelamova.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;

public class TermsActivity extends BaseActivity {

    TextView textView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        textView = findViewById(R.id.termsTitle);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/Gotham Bold.otf");
        textView.setTypeface(typeface);
    }
}
