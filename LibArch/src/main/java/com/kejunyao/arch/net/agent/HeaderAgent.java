package com.kejunyao.arch.net.agent;

import com.kejunyao.arch.net.header.Header;

import java.util.List;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年07月18日
 */
public interface HeaderAgent {
    /**
     * 请求头信息
     */
    List<Header> getHeaders();
}
