package com.kejunyao.arch.net.agent;

import com.kejunyao.arch.net.header.Header;

import java.util.List;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年07月18日
 */
public interface CookieAgent {
    /**
     * 根据domain读取Cookie
     * @param domain 域名
     * @return Cookie
     */
    Header getCookie(String domain);

    /**
     * 写入Cookie
     * @param domain 主机域名
     * @param cookies 服务器返回的Cookie值
     */
    void setCookie(String domain, List<String> cookies);
}
