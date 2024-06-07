package com.accounting.accounting_tool.response;

public class BasicResponse <T>
{
    private T body;
    private String message;

    public BasicResponse() { }

    public BasicResponse(String message) {
        this.message = message;
    }

    public BasicResponse(T body, String message) {
        this.body = body;
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
