package com.kejunyao.arch.permission;

/**
 * 权限授予回调
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
public interface Callback {
    /**
     * 权限授权成功
     */
    void onSuccess();

    /**
     * 权限授权失败
     * @param permissions 授权失败的权限
     */
    void onFailure(String[] permissions);
}
