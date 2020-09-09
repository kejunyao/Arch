package com.kejunyao.arch.recycler;

/**
 * 数据适配器
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public class AdapterData<T> {

    public int type;
    public T data;

    public AdapterData(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public AdapterData(int type) {
        this(type, null);
    }
}
