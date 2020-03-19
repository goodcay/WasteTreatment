package com.waste.treatment.bean;

public class CodeBean {
    private int Oid;
    private String Code;

    public int getOid() {
        return Oid;
    }

    public void setOid(int oid) {
        Oid = oid;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @Override
    public String toString() {
        return "CodeBean{" +
                "Oid=" + Oid +
                ", Code='" + Code + '\'' +
                '}';
    }
}
