package com.waste.treatment.bean;

public class UserContent {
    private int ID;
    private String ChineseName;
    private String Remark;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getChineseName() {
        return ChineseName;
    }

    public void setChineseName(String chineseName) {
        ChineseName = chineseName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }


    @Override
    public String toString() {
        return "UserContent{" +
                "ID=" + ID +
                ", ChineseName='" + ChineseName + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
