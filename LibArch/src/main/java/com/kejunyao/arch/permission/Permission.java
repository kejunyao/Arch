package com.kejunyao.arch.permission;

import android.content.Context;

/**
 * 权限
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
final class Permission {
    String name;
    boolean success;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return name.equals(that.name);
    }

    boolean isGranted(Context context) {
        return PermissionUtils.hasPermission(context, name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
