package com.waste.treatment.bean;

public class newsBean {
    private String reason;
    private newsResultBean result;

    @Override
    public String toString() {
        return "newsBean{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public newsResultBean getResult() {
        return result;
    }

    public void setResult(newsResultBean result) {
        this.result = result;
    }
}
