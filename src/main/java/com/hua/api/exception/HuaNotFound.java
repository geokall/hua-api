package com.hua.api.exception;

public class HuaNotFound extends RuntimeException {

    public HuaNotFound() {
        super();
    }

    public HuaNotFound(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HuaNotFound(final String message) {
        super(message);
    }

    public HuaNotFound(final Throwable cause) {
        super(cause);
    }
}
