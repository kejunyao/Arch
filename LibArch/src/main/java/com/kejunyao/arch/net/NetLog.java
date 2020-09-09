package com.kejunyao.arch.net;

import android.util.Log;

import static android.util.Log.getStackTraceString;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年07月18日
 */
final class NetLog {

    private static final String LOG_PREFIX = "KKNet";

    private NetLog() {
    }

    private static String tag(String tag) {
        return String.format("%1$s_%2$s", LOG_PREFIX, tag);
    }

    static String msg(boolean range, Object... msg) {
        StringBuilder sb = new StringBuilder();
        sb.ensureCapacity(150);
        if (range) {
            sb.append("\n =====================================start=====================================\n");
        }
        if (msg == null || msg.length == 0) {
            sb.append(' ');
        } else if (msg.length == 1) {
            Object o = msg[0];
            sb.append(o == null ? "" : o.toString());
        } else {
            for (int i = 0; i < msg.length; i++) {
                sb.append(msg[i]);
            }
        }
        if (range) {
            sb.append("\n=====================================end=====================================\n");
        }
        return sb.toString();
    }

    public static void d(String tag, Object... msg) {
        Log.d(tag(tag), msg(true, msg));
    }

    public static void i(String tag, Object... msg) {
        Log.i(tag(tag), msg(true, msg));
    }

    public static void v(String tag, Object... msg) {
        Log.v(tag(tag), msg(true, msg));
    }

    public static void e(String tag, Object... msg) {
        Log.e(tag(tag), msg(true, msg));
    }

    public static void e(String tag, Throwable tr, Object... msg) {
        e(tag, msg(false, msg), getStackTraceString(tr));
    }
}
