package com.kejunyao.arch.net.agent;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年07月18日
 */
public interface NetworkAgent {
    /**
     * 网络是否可以用
     */
    boolean isNetworkAvailable();

    /**
     * 是否为WiFi网络
     */
    boolean isInWifiNetwork();
}
