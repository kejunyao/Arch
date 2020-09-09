package com.kejunyao.arch.net.header;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年07月18日
 */
public class HeaderImpl implements Header {

    private final String name;
    private final String value;

    public HeaderImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String value() {
        return this.value;
    }
}
