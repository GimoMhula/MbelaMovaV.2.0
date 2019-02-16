package mmconsultoria.co.mz.mbelamova.model;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();

    public void startMyActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    public <Frag extends BaseFragment> void swapFragment(@NonNull int container, @NonNull Frag fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, fragment.getTAG())
                .commit();
    }

    public <Frag extends Fragment> void swapFragmentAndAddToBackstack(@NonNull int container, @NonNull Frag fragment, @Nullable String tag, @Nullable String stackName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(stackName)
                .commit();
    }


    public void callImageFromStorage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escolha a Imagem"), requestCode);

    }

    public void changeFont(String path, TextView target) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), path);
        target.setTypeface(typeface);
    }

    public String getFileExtension(Uri path) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(path));
    }
}
