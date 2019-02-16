package mmconsultoria.co.mz.mbelamova.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNameFragment extends BaseFragment {

    TextView backToSettings;

    public EditNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_name, container, false);
        backToSettings = view.findViewById(R.id.back_to_Settings);

        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(R.id.settings_frame, new SettingsFragment());
            }
        });

        return view;
    }

}
