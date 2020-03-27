package com.waste.treatment.bean;

public class GenRecyleContentBean {
    private int Status;
    private String Code;
    private String RecyleTime;
    private String Name;
    private String Weight;
    private CarsContent Company;
    private RouteBean RouteId;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRecyleTime() {
        return RecyleTime;
    }

    public void setRecyleTime(String recyleTime) {
        RecyleTime = recyleTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public CarsContent getCompany() {
        return Company;
    }

    public void setCompany(CarsContent company) {
        Company = company;
    }

    public RouteBean getRouteId() {
        return RouteId;
    }

    public void setRouteId(RouteBean routeId) {
        RouteId = routeId;
    }

    @Override
    public String toString() {
        return "GenRecyleContentBean{" +
                "Status=" + Status +
                ", Code='" + Code + '\'' +
                ", RecyleTime='" + RecyleTime + '\'' +
                ", Name='" + Name + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Company=" + Company +
                ", RouteId=" + RouteId +
                '}';
    }
}
