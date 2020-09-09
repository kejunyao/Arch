package com.kejunyao.arch.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * 权限工具类
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class PermissionUtils {

    /**
     * 外部存储设备读取权限请求码
     */
    public static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 16121;
    public static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 16122;
    public static final int REQUEST_CODE_PERMISSION_READ_WRITE_EXTERNAL_STORAGE = 16123;
    public static final int REQUEST_CODE_PERMISSION_SYSTEM_ALERT_WINDOW = 16124;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 16125;

    /**
     * Private Constructor
     */
    private PermissionUtils() {
    }

    /**
     * 是否有指定的权限
     *
     * @param context    {@link Context}
     * @param permission 指定的权限标志
     * @return true，有权限；false，无权限
     */
    private static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    /**
     * 请求一组权限
     *
     * @param activity    {@link Activity}
     * @param permissions 指定权限标志组
     */
    private static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                requestCode
        );
    }

    public static boolean hasPermission(Context context, int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                return hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                return hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            case REQUEST_CODE_PERMISSION_READ_WRITE_EXTERNAL_STORAGE:
                return hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            case REQUEST_CODE_PERMISSION_SYSTEM_ALERT_WINDOW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return Settings.canDrawOverlays(context);
                }
                return hasPermission(context, Manifest.permission.SYSTEM_ALERT_WINDOW);
            case REQUEST_CODE_PERMISSION_CAMERA:
                return hasPermission(context, Manifest.permission.CAMERA);
            default:
                return false;
        }
    }

    public static void requestPermission(Activity activity, int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                requestPermission(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                break;
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                break;
            case REQUEST_CODE_PERMISSION_READ_WRITE_EXTERNAL_STORAGE:
                requestPermission(
                        activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCode
                );
                break;
            case REQUEST_CODE_PERMISSION_SYSTEM_ALERT_WINDOW: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(activity)) {
                        Intent intent = new Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse(String.format("package:%s", activity.getPackageName()))
                        );
                        activity.startActivityForResult(intent, requestCode);
                    } else {
                        Toast.makeText(activity, "请到【设置】中打开【显示悬浮窗】权限", Toast.LENGTH_LONG).show();
                    }
                } else {
                    requestPermission(activity, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, requestCode);
                }
                break;
            }
            case REQUEST_CODE_PERMISSION_CAMERA: {
                requestPermission(
                        activity,
                        new String[]{Manifest.permission.CAMERA},
                        requestCode
                );
                break;
            }
            default:
                break;
        }
    }
}
