package mmconsultoria.co.mz.mbelamova.activity;

import android.app.Application;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mmconsultoria.co.mz.mbelamova.BuildConfig;
import mmconsultoria.co.mz.mbelamova.model.ReleaseTree;
import timber.log.Timber;

public class MbelaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected @Nullable String createStackElementTag(@NotNull StackTraceElement element) {
                    return super.createStackElementTag(element) + ": line"+element.getLineNumber()+"/[" + element.getMethodName()+"]";
                }
            });
        } else Timber.plant(new ReleaseTree());
    }
}
