package com.waste.treatment.bean;

public class RuiKuBean {
    private boolean isCheck;
    private String abc;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    @Override
    public String toString() {
        return "RuiKuBean{" +
                "isCheck=" + isCheck +
                ", abc='" + abc + '\'' +
                '}';
    }
}
