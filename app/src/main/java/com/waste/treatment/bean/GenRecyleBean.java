package com.waste.treatment.bean;

public class GenRecyleBean {
    private boolean IsSuccess;
    private String ErrorMsg;
    private GenRecyleContentBean Content;

    public void setIsSuccess(boolean isSuccess) {
        IsSuccess = isSuccess;
    }

    public boolean getIsSuccess() {
        return IsSuccess;
    }


    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public GenRecyleContentBean getContent() {
        return Content;
    }

    public void setContent(GenRecyleContentBean content) {
        Content = content;
    }
}
