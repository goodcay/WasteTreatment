package com.waste.treatment.bean;

import java.math.BigDecimal;
import java.util.List;

public class GetDriverBean {
    private List<UserContent> Content;
    private boolean IsSuccess;
    private String ErrorMsg;

    @Override
    public String toString() {
        return "GetCarsBean{" +
                "Content=" + Content +
                ", IsSuccess=" + IsSuccess +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                '}';
    }


    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public List<UserContent> getContent() {
        return Content;
    }

    public void setContent(List<UserContent> content) {
        Content = content;
    }


    public void setIsSuccess(boolean isSuccess) {
        IsSuccess =isSuccess;
    }
    public boolean getIsSuccess() {
        return IsSuccess;
    }

}
