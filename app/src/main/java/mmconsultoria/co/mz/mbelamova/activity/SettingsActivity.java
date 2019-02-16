package mmconsultoria.co.mz.mbelamova.activity;

import android.os.Bundle;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.fragment.SettingsFragment;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().add(R.id.settings_frame, new SettingsFragment()).commit();
    }
}
