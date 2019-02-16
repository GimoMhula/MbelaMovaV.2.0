package mmconsultoria.co.mz.mbelamova.util;

import android.util.Log;

import static java.lang.String.valueOf;

public class Logger {
    public static <ClassTarget> void debug(ClassTarget target, Throwable cause) {
        Class t = target.getClass();
        Log.d(t.getSimpleName(), valueOf(t.getEnclosingMethod().getName()), cause);
    }
}
