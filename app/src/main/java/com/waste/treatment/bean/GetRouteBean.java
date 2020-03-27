package com.waste.treatment.bean;

public class GetRouteBean {
    private boolean IsSuccess;
    private String ErrorMsg;
    private GetRouteContentBean Content;

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

    public GetRouteContentBean getContent() {
        return Content;
    }

    public void setContent(GetRouteContentBean content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "GetRouteBean{" +
                "IsSuccess=" + IsSuccess +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                ", Content=" + Content +
                '}';
    }
}
