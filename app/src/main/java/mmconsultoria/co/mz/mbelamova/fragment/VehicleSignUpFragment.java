package mmconsultoria.co.mz.mbelamova.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.cloud.DatabaseValue;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;


public class VehicleSignUpFragment extends BaseFragment {

    Button submeter;
    private String userid;

    public static VehicleSignUpFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(DatabaseValue.user_id.name(), userId);
        VehicleSignUpFragment fragment = new VehicleSignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            userid = getArguments().getString(DatabaseValue.user_id.name());
    }

    public VehicleSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_vehicle, container, false);

        submeter = view.findViewById(R.id.btnsubmit);

//        submeter.setOnClickListener(view1 ->
//                VehicleSignUpFragment
//                .this
//                .startActivity(DriverMapsActivity.class,null,null)
//        );

        return view;
    }

}
