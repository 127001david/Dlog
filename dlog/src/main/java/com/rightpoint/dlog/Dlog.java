package com.rightpoint.dlog;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.util.List;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/5/20 10:08 AM 
 */
public class Dlog {
    private static IDlogManager mDlogManager;
    /*
    // 行号
    Thread.currentThread().getStackTrace()[1].getLineNumber();
    // 方法名
    Thread.currentThread().getStackTrace()[1].getMethodName();
    // 类名
    Thread.currentThread().getStackTrace()[1].getClassName();
     */

    private Dlog() {

    }

    private static class SingleInstance {
        private static Dlog INSTANCE = new Dlog();
    }

    public static Dlog getInstance() {
        return SingleInstance.INSTANCE;
    }

    private Application mApplication;

    public void init(Application application) {
        mApplication = application;

        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");

        String processName = getProcessName(application);
        String logPath = application.getFilesDir() + "/xlog/log/" + processName;

        String cachePath = application.getFilesDir() + "/xlog/cache/" + processName;

        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath,
                    "Dlog", 0, "PUB_KEY");
            Xlog.setConsoleLogOpen(true);
        } else {
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath,
                    "Dlog", 0, "PUB_KEY");
            Xlog.setConsoleLogOpen(false);
        }

        Log.setLogImp(new Xlog());

        application.bindService(new Intent(application, DlogManagerService.class),
                getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void appenderClose() {
        Log.appenderClose();
    }

    public static void appenderFlush(boolean isSync) {
        Log.appenderFlush(isSync);
    }

    public static int getLogLevel() {
        return Log.getLogLevel();
    }

    public static void setLevel(final int level, final boolean jni) {
        Log.setLevel(level, jni);
    }

    public static void f(final String tag, final String msg) {
        f(tag, msg, (Object[]) null);
    }

    /**
     * 支持多 tag
     * @param tags tag数组
     * @param msg msg
     */
    public static void f(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            f(tag, msg);
        }
    }

    public static void e(final String tag, final String msg) {
        e(tag, msg, (Object[]) null);
    }

    public static void e(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            e(tag, msg);
        }
    }

    public static void w(final String tag, final String msg) {
        w(tag, msg, (Object[]) null);
    }

    public static void w(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            w(tag, msg);
        }
    }

    public static void i(final String tag, final String msg) {
        i(tag, msg, (Object[]) null);
    }

    public static void i(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            i(tag, msg);
        }
    }

    public static void d(final String tag, final String msg) {
        d(tag, msg, (Object[]) null);
    }

    public static void d(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            d(tag, msg);
        }
    }

    public static void v(final String tag, final String msg) {
        v(tag, msg, (Object[]) null);
    }

    public static void v(@NonNull final String[] tags, final String msg) {
        for (String tag : tags) {
            v(tag, msg);
        }
    }

    public static void f(String tag, final String format, final Object... obj) {
        Log.f(tag, format, obj);
    }

    public static void f(@NonNull final String[] tags, final String msg, final Object... obj) {
        for (String tag : tags) {
            f(tag, msg, obj);
        }
    }

    public static void e(String tag, final String format, final Object... obj) {
        Log.e(tag, format, obj);
    }

    public static void e(@NonNull final String[] tags, final String msg, final Object... obj) {
        for (String tag : tags) {
            e(tag, msg, obj);
        }
    }

    public static void w(String tag, final String format, final Object... obj) {
        Log.w(tag, format, obj);
    }

    public static void w(@NonNull final String[] tags, final String msg, final Object... obj) {
        for (String tag : tags) {
            w(tag, msg, obj);
        }
    }

    public static void i(String tag, final String format, final Object... obj) {
        Log.i(tag, format, obj);
    }

    public static void i(@NonNull final String[] tags, final String format, final Object... obj) {
        for (String tag : tags) {
            i(tag, format, obj);
        }
    }

    public static void d(String tag, final String format, final Object... obj) {
        Log.d(tag, format, obj);
    }

    public static void d(@NonNull final String[] tags, final String format, final Object... obj) {
        for (String tag : tags) {
            d(tag, format, obj);
        }
    }

    public static void v(String tag, final String format, final Object... obj) {
        Log.v(tag, format, obj);
    }

    public static void v(@NonNull final String[] tags, final String format, final Object... obj) {
        for (String tag : tags) {
            v(tag, format, obj);
        }
    }

    public static void printErrStackTrace(String tag, Throwable tr, final String format,
                                          final Object... obj) {
        Log.printErrStackTrace(tag, tr, format, obj);
    }

    public static void printErrStackTrace(@NonNull final String[] tags, Throwable tr,
                                          final String format,
                                          final Object... obj) {
        for (String tag : tags) {
            printErrStackTrace(tag, tr, format, obj);
        }
    }

    /**
     * 该方法实现了多进程同步调用，存在耗时较长可能，所以建议在子线程调用
     * @return APP目录下所有.xlog日志文件的路径
     */
    public static List<String> prepareLogFiles() {
        if (null != mDlogManager) {
            try {
                return mDlogManager.prepareLogFiles();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDlogManager = IDlogManager.Stub.asInterface(service);

                d("dlog", "onServiceConnected");

                try {
                    mDlogManager.registerListener(new IOnNeedFlushListener.Stub() {
                        @Override
                        public void onNeedFlushLogCache() throws RemoteException {
                            d("flushDlog", getProcessName(mApplication) + " onFlushLogCache");
                            Log.appenderFlush(true);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }
}
