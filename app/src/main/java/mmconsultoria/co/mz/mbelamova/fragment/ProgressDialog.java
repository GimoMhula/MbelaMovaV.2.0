package mmconsultoria.co.mz.mbelamova.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import mmconsultoria.co.mz.mbelamova.R;

public class ProgressDialog extends LinearLayout {
    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    private View rootView;
    private AlertDialog dialog;

    public ProgressDialog(Context context) {
        super(context);
        initView(context);
    }

    public ProgressDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        rootView = inflate(context, R.layout.progress_dialog, this);

        progressBar = rootView.findViewById(R.id.progress_dialog_progress);
        textView = rootView.findViewById(R.id.progress_dialog_text);
        dialog = new AlertDialog.Builder(context)
                .setView(rootView)
                .setCancelable(false)
                .create();
        dialog.show();

    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setText(@StringRes int resId) {
        textView.setText(resId);
    }


}
