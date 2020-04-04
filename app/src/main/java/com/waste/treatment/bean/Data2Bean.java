package com.waste.treatment.bean;

import java.util.List;

public class Data2Bean {
    private boolean IsSuccess;
    private String ErrorMsg;
    private Data1Bean Content;

    public boolean getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        IsSuccess =isSuccess;
    }


    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public Data1Bean getContent() {
        return Content;
    }

    public void setContent(Data1Bean content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "IsSuccess=" + IsSuccess +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                ", Content=" + Content +
                '}';
    }
}
