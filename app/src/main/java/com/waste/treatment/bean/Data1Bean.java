package com.waste.treatment.bean;

public class Data1Bean {
    private String Name;
    private String Code;
    private Double Weight;
    private String RecyleTime;
    private DataRouteIdBean RouteId;
    private CarsContent Company;
    private int Status;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }

    public String getRecyleTime() {
        return RecyleTime;
    }

    public void setRecyleTime(String recyleTime) {
        RecyleTime = recyleTime;
    }

    public DataRouteIdBean getRouteId() {
        return RouteId;
    }

    public void setRouteId(DataRouteIdBean routeId) {
        RouteId = routeId;
    }

    public CarsContent getCompany() {
        return Company;
    }

    public void setCompany(CarsContent company) {
        Company = company;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "Data1Bean{" +
                "Name='" + Name + '\'' +
                ", Code='" + Code + '\'' +
                ", Weight=" + Weight +
                ", RecyleTime='" + RecyleTime + '\'' +
                ", RouteId=" + RouteId +
                ", Company=" + Company +
                ", Status=" + Status +
                '}';
    }
}
