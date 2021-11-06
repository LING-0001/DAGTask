package work.lingling.dagtask;

import android.util.Log;

public class LogUtil {

    private static String mClassName;
    private static String mMethod;
    private static int mLineNum;
    private static boolean mIsOpen = true;

    private LogUtil() {
    }

    public static void openLog(boolean mIsOpen) {
        LogUtil.mIsOpen = mIsOpen;
    }

    // 侵入性过强、换个写法
    /*public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }*/

    private static boolean isDebuggable() {
        return mIsOpen;
    }

    private static String getLogData(String tag, String msg) {
        return mMethod + "(" + mClassName + ":" + mLineNum + ")" + " " + tag + ": " + msg;
    }

    private static void getClassData(StackTraceElement[] stackTraceElements) {
        mClassName = stackTraceElements[1].getFileName();
        mMethod = stackTraceElements[1].getMethodName();
        mLineNum = stackTraceElements[1].getLineNumber();
    }

    public static void v(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.v(mClassName, getLogData(tag, msg));
        }
    }

    public static void d(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.d(mClassName, getLogData(tag, msg));
        }
    }

    public static void i(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.i(mClassName, getLogData(tag, msg));
        }
    }

    public static void w(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.w(mClassName, getLogData(tag, msg));
        }
    }

    public static void e(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.e(mClassName, getLogData(tag, msg));
        }
    }

    public static void wtf(String tag, String msg) {
        if (isDebuggable()) {
            getClassData(new Throwable().getStackTrace());
            Log.wtf(mClassName, getLogData(tag, msg));
        }
    }

}
