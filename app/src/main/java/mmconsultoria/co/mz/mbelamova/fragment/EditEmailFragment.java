package mmconsultoria.co.mz.mbelamova.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEmailFragment extends BaseFragment {

    TextView backToSettings1;
    Button confirmBtn;
    EditText emailEditText;


    public EditEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);

        backToSettings1 = view.findViewById(R.id.back_to_Settings1);
        confirmBtn = view.findViewById(R.id.confirm_email);
        emailEditText = view.findViewById(R.id.user_email);


        backToSettings1.setOnClickListener(v -> swapFragment(R.id.settings_frame, new SettingsFragment()));
        return view;
    }

}
