package com.kejunyao.arch.thread;

public interface Processor<T> {

    T onProcess();

    void onResult(T result);

}
