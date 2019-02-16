package mmconsultoria.co.mz.mbelamova.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.Person;
import mmconsultoria.co.mz.mbelamova.view_model.AuthModel;
import mmconsultoria.co.mz.mbelamova.view_model.AuthService;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class StatsActivity extends AppCompatActivity {
    @BindView(R.id.stats_toolbar)
    public Toolbar toolbar;
    @BindView(R.id.stats_pager)
    public ViewPager pager;
    @BindView(R.id.stats_tabs)
    public TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        StatsPagerAdapter adapter = new StatsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager, true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ViewModelProviders.of(this)
                .get(AuthModel.class)
                .getUser().observe(this, this::updateUserOnView);
    }

    private void updateUserOnView(Person person) {
        toolbar.setTitle(person.getFullName());
    }
}
