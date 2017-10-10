package com.sereneast.orchestramdm.keysight.mdmcustom.exception;

import com.orchestranetworks.service.OperationException;

public class ApplicationOperationException extends OperationException {
    public ApplicationOperationException(String s) {
        super(s);
    }

    public ApplicationOperationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
