package com.waste.treatment.bean;

public class GetPosBean {
    private boolean IsSuccess;
    private String ErrorMsg;
    private GetPosContentBean Content;

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

    public GetPosContentBean getContent() {
        return Content;
    }

    public void setContent(GetPosContentBean content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "GetPosBean{" +
                "IsSuccess=" + IsSuccess +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                ", Content=" + Content +
                '}';
    }
}
