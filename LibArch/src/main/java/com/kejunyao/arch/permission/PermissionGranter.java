package com.kejunyao.arch.permission;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限请求总入口
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
public final class PermissionGranter {

    private int requestCode;

    private List<Permission> mPermissions;

    private PermissionGranter() {
        mPermissions = new ArrayList<>();
    }

    public static PermissionGranter with(int requestCode) {
        PermissionGranter granter = new PermissionGranter();
        granter.requestCode = requestCode;
        return granter;
    }

    public PermissionGranter addPermission(String permission) {
        Permission p = new Permission();
        p.name = permission;
        if (!mPermissions.contains(p)) {
            mPermissions.add(p);
        }
        return this;
    }

    public void request(Context context) {
        if (context == null) {
            return;
        }
        ActionManager.getInstance().addPermissions(requestCode, mPermissions);
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        context.startActivity(intent);
    }

}
