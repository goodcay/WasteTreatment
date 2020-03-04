package com.waste.treatment.bean;

public class DouBan {
    private String mas;
    private int code;
    private String request;

    @Override
    public String toString() {
        return "DouBan{" +
                "mas='" + mas + '\'' +
                ", code=" + code +
                ", request='" + request + '\'' +
                '}';
    }

    public String getMas() {
        return mas;
    }

    public void setMas(String mas) {
        this.mas = mas;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
