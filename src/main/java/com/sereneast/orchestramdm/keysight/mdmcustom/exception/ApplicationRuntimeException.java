package com.sereneast.orchestramdm.keysight.mdmcustom.exception;

import org.springframework.core.NestedRuntimeException;

public class ApplicationRuntimeException extends NestedRuntimeException {
    public ApplicationRuntimeException(String msg) {
        super(msg);
    }

    public ApplicationRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
