package com.kejunyao.arch.permission;

import android.app.Activity;
import androidx.core.app.ActivityCompat;
import com.kejunyao.arch.util.Utility;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 权限Action管理器
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
final class ActionManager {

    private volatile static ActionManager sInstance;

    private ActionManager() {
    }

    static ActionManager getInstance() {
        if (sInstance == null) {
            synchronized (ActionManager.class) {
                if (sInstance == null) {
                    sInstance = new ActionManager();
                }
            }
        }
        return sInstance;
    }

    private final LinkedHashMap<Integer, List<Permission>> mPermissions = new LinkedHashMap<>();

    public void addPermissions(int requestCode, List<Permission> permissions) {
        if (Utility.isNullOrEmpty(permissions)) {
            return;
        }
        mPermissions.put(requestCode, permissions);
    }

    public void doPermissionsRequest(Activity activity) {
        List<Permission> doRequest = new ArrayList<>();
        for (List<Permission> permissions : mPermissions.values()) {
            for (Permission permission : permissions) {
                permission.success = permission.isGranted(activity);
                if (!permission.success) {
                    doRequest.add(permission);
                }
            }
        }
        if (doRequest.isEmpty()) {
            // TODO
            return;
        }
        String[] permissions = Utility.toStringArray(doRequest);
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                PermissionRequestActivity.REQUEST_CODE
        );
    }

    public void checkPermissionGranted() {
    }
}
