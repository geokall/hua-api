package com.hua.api.exception;

public class HuaUnauthorized extends RuntimeException {

    public HuaUnauthorized() {
        super();
    }

    public HuaUnauthorized(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HuaUnauthorized(final String message) {
        super(message);
    }

    public HuaUnauthorized(final Throwable cause) {
        super(cause);
    }
}
