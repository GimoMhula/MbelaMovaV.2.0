package mmconsultoria.co.mz.mbelamova.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import mmconsultoria.co.mz.mbelamova.R;
import mmconsultoria.co.mz.mbelamova.model.BaseFragment;
import mmconsultoria.co.mz.mbelamova.view_model.AuthModel;
import mmconsultoria.co.mz.mbelamova.view_model.AuthService;
import timber.log.Timber;

public class VerifySMSCodeFragment extends BaseFragment {
    @BindView(R.id.sms_verification_code_text)
    public EditText verificationCodeText;
    private AuthModel authService;
    private String phoneNumber = "";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    public static VerifySMSCodeFragment newInstance(String phoneNumber) {
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phoneNumber);
        VerifySMSCodeFragment fragment = new VerifySMSCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            phoneNumber = getArguments().getString(PHONE_NUMBER, "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_sms_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();

        authService = ViewModelProviders.of(getActivity())
                .get(AuthModel.class);
    }

    @OnClick(R.id.sms_verification_code_btn)
    public void verifyCode(View view) {
        if (validateText()) {
            final String code = verificationCodeText.getText().toString().trim();
            authService.sendConfirmCode(code)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onVerificationSuccessful, this::onVerificationOnError);
        }
    }

    private void onVerificationSuccessful(AuthService.AuthResult authResult) {
        Timber.d(authResult.name());

        if (authResult == AuthService.AuthResult.SIGN_IN_SUCCESSFUL) {
            swapFragment(R.id.login_container,new SignUpFragment());
            //startActivity(DriverMapsActivity.class, null, null);
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void onVerificationOnError(Throwable throwable) {
        Timber.d(throwable);
    }

    private boolean validateText() {
        if (verificationCodeText.getText().toString().trim().isEmpty()) {
            verificationCodeText.setError(getString(R.string.fill_text_message));
            return false;
        }
        return true;
    }

    @OnClick(R.id.sms_verification_return_to_login)
    public void returnToLogin(View view) {
        LoginFragment baseFragment = new LoginFragment();
        swapFragment(R.id.login_container, baseFragment);
    }

    @OnClick(R.id.sms_verification_resend_code_btn)
    public void resendCode(View view) {
        authService.resendCode(phoneNumber, getActivity());
    }
}
