package com.hua.api.exception;

public class HuaForbidden extends RuntimeException {

    public HuaForbidden() {
        super();
    }

    public HuaForbidden(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HuaForbidden(final String message) {
        super(message);
    }

    public HuaForbidden(final Throwable cause) {
        super(cause);
    }
}
