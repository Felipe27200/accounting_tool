package com.accounting.accounting_tool.response;

public class BasicResponse <T>
{
    private T body;
    private String status;

    public BasicResponse() { }

    public BasicResponse(String status) {
        this.status = status;
    }

    public BasicResponse(T body, String status) {
        this.body = body;
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMessage() {
        return status;
    }

    public void setMessage(String status) {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
}
