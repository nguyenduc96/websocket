package com.chainprotocol.gateway.exception;


public class ModuleException extends RuntimeException {

    private final String responseCode;

    public ModuleException(final String message) {
        super(message);
        this.responseCode = null;
    }

    public ModuleException(final String responseCode, final String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public ModuleException(final String responseCode, final String message, final Throwable throwable) {
        super(message, throwable);
        this.responseCode = responseCode;
    }

    public String getResponseCode() {
        return responseCode;
    }
}
