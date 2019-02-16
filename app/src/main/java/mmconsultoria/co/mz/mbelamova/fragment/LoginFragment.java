package mmconsultoria.co.mz.mbelamova.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.activity.DriverMapsActivity;
import mmconsultoria.co.mz.mbelamova.cloud.DatabaseValue;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;
import mmconsultoria.co.mz.mbelamova.model.SimpleCallback;
import mmconsultoria.co.mz.mbelamova.view_model.AuthModel;
import mmconsultoria.co.mz.mbelamova.view_model.AuthService;
import timber.log.Timber;

import static java.lang.String.valueOf;
import static mmconsultoria.co.mz.mbelamova.R2.id.login_container;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements SimpleCallback<String> {
    @BindView(R.id.login_fragment_phone_number_text)
    public EditText phoneNumber;
    @BindView(R.id.login_fragment_btn)
    public ImageButton loginBtn;

    private final static String MOZ_AREA_CODE = "+258";
    private ProgressDialog dialog;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginBtn.setOnClickListener(this::login);
    }

    public void login(View view) {
        if (validatePhoneNumberText()) {
            login(MOZ_AREA_CODE + phoneNumber.getText().toString().trim());
        }
    }

    private void login(String number) {
        dialog = new ProgressDialog(getActivity());
        dialog.show();
        AuthModel auth = ViewModelProviders.of(getActivity())
                .get(AuthModel.class);

        auth.searchUser(number)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    Timber.d(valueOf(next));
                    if (next.equals(DatabaseValue.NULL)) {
                        signUpUser(number, auth);
                    } else {
                        auth.signIn(number, getActivity())
                                .subscribe(authResult -> signInSuccess(authResult, number,false), this::onSignInError);

                    }

                });

    }

    private void signUpUser(String number, AuthModel auth) {
        auth.signIn(number, getActivity())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(authResult -> {
                    signInSuccess(authResult, number,true);

                }, this::onSignInError);
    }

    private void signInSuccess(AuthService.AuthResult authResult, String number, boolean isNewUser) {
        dialog.dismiss();
        Timber.d(authResult.name());
        if (authResult == AuthService.AuthResult.ERR_NETWORK) {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        if (authResult == AuthService.AuthResult.CODE_SENT) {
            swapFragment(R.id.login_container, VerifySMSCodeFragment.newInstance(number));
            return;
        }
        if (authResult == AuthService.AuthResult.SIGN_IN_SUCCESSFUL) {
            if (!isNewUser) {
                if (getActivity() instanceof BaseActivity) {
                    BaseActivity activity = (BaseActivity) getActivity();
                    activity.startMyActivity(DriverMapsActivity.class);
                }
            } else {
                swapFragment(login_container, SignUpFragment.newInstance(number));
            }
        }
    }


    private void onSignInError(Throwable throwable) {
        Timber.e(throwable);
        dialog.dismiss();
    }


    private boolean validatePhoneNumberText() {
        if (phoneNumber.getText().toString().trim().isEmpty()) {
            phoneNumber.setError(getActivity().getString(R.string.write_number));
            return false;
        }

        return true;
    }

    @Override
    public void onSuccess(String data) {
        String trim = data.trim();
        if (trim.startsWith(MOZ_AREA_CODE)) {
            trim = trim.substring(4, 13);
        }
        phoneNumber.setText(trim);
    }
}
