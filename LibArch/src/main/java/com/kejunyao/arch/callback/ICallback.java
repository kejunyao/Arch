package com.kejunyao.arch.callback;

/**
 * 返回结果Callback
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public interface ICallback<T> {
    /**
     * 返回结果
     * @param data T
     */
    void onCallback(T data);
}
