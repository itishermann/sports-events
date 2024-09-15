package com.etna.sportevents.payloads.response;

public class HttpResponse<T> {
    private String message;
    private String code;
    private Object errors = null;
    private T data = null;
    private Object meta = null;

    public HttpResponse(String message, T data, Object meta, String code, Object errors) {
        this.message = message;
        this.data = data;
        this.meta = meta;
        this.code = code;
        this.errors = errors;
    }

    public HttpResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }
}
