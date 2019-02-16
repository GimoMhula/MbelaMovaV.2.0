package mmconsultoria.co.mz.mbelamova.model;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {
    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return priority != Log.VERBOSE && priority != Log.DEBUG;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        if (priority == Log.ERROR && t == null)
            Log.e(tag, message);


        if (priority == Log.ERROR)
            Log.e(tag, message, t);
    }
}
