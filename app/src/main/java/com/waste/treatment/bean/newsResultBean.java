package com.waste.treatment.bean;

import java.util.List;

public class newsResultBean {
    private String stat;
    private List<newsDataBean> data;

    @Override
    public String toString() {
        return "newsResultBean{" +
                "stat='" + stat + '\'' +
                ", data=" + data +
                '}';
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<newsDataBean> getData() {
        return data;
    }

    public void setData(List<newsDataBean> data) {
        this.data = data;
    }
}
