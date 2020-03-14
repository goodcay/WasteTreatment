package com.waste.treatment.bean;

public class RouteBean {
    private CarsContent CarId;
    private UserContent Driver;
    private String BeginTime;
    private UserContent BeginOperator;
    private String EndTime;
    private int Oid;

    @Override
    public String toString() {
        return "RouteBean{" +
                "CarId=" + CarId +
                ", Driver=" + Driver +
                ", BeginTime='" + BeginTime + '\'' +
                ", BeginOperator=" + BeginOperator +
                ", EndTime='" + EndTime + '\'' +
                ", Oid=" + Oid +
                '}';
    }

    public CarsContent getCarId() {
        return CarId;
    }

    public void setCarId(CarsContent carId) {
        CarId = carId;
    }

    public UserContent getDriver() {
        return Driver;
    }

    public void setDriver(UserContent driver) {
        Driver = driver;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public UserContent getBeginOperator() {
        return BeginOperator;
    }

    public void setBeginOperator(UserContent beginOperator) {
        BeginOperator = beginOperator;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public int getOid() {
        return Oid;
    }

    public void setOid(int oid) {
        Oid = oid;
    }
}
