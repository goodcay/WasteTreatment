package com.waste.treatment.bean;

import java.util.List;

public class GetCarsBean {
    private List<CarsBean> Content;
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

    public List<CarsBean> getContent() {
        return Content;
    }

    public void setContent(List<CarsBean> content) {
        Content = content;
    }

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }


}
