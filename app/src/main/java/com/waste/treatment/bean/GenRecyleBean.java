package com.waste.treatment.bean;

public class GenRecyleBean {
    private  boolean IsSuccess;
    private String ErrorMsg;
    private CodeBean Content;

    public void setIsSuccess(boolean isSuccess) {
        IsSuccess =isSuccess;
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

    public CodeBean getContent() {
        return Content;
    }

    public void setContent(CodeBean content) {
        Content = content;
    }
}
