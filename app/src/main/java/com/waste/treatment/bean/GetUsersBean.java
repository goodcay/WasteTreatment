package com.waste.treatment.bean;

public class GetUsersBean {
    private boolean IsSuccess;
    private String ErrorMsg;
    private UserContent Content;


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

    public UserContent getContent() {
        return Content;
    }

    public void setContent(UserContent content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "GetUsersBean{" +
                "IsSuccess=" + IsSuccess +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                ", Content=" + Content +
                '}';
    }
}
