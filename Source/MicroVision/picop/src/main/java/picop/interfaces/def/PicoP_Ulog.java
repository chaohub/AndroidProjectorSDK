package picop.interfaces.def;

import android.util.Log;

/**
 * Created by carey.wang on 2017/3/29.
 *
 * We overwrite android log class,
 * in order to disable some useless log in release version.
 */
public class PicoP_Ulog {

    /* Enable in development stage, to get all the log.
     * If we set this to false, all the SDK log will be disabled.
     */
    public static final boolean DEBUG_LOG = true;

    // Log filter tag.
    public static final String LOG_TAG = "PicoP ";

    // Log tag for this file, it should be samed in all the files.
    public static final String TAG = "PicoP_Ulog ";

    public static void v(String tag, String msg){
        if(DEBUG_LOG){
            Log.v(LOG_TAG,tag + msg);
        }
    }

    public static void d(String tag, String msg){
        if(DEBUG_LOG){
            Log.d(LOG_TAG,tag + msg);
        }
    }

    public static void i(String tag, String msg){
        if(DEBUG_LOG){
            Log.i(LOG_TAG,tag + msg);
        }
    }

    public static void w(String tag, String msg){
        if(DEBUG_LOG){
            Log.w(LOG_TAG,tag + msg);
        }
    }

    public static void e(String tag, String msg){
        if(DEBUG_LOG){
            Log.e(LOG_TAG,tag + msg);
        }
    }

    /* Return current function name as a string. */
    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName() + " ";
    }
}
