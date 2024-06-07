package com.accounting.accounting_tool.error_handling.exception;

public class DuplicateRecordException extends RuntimeException
{
    public DuplicateRecordException (String message) { super(message); }
    public DuplicateRecordException (Throwable cause) { super(cause); }
    public DuplicateRecordException (String message, Throwable cause) { super(message, cause); }
}
