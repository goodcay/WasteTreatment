package com.waste.treatment.bean;

import java.math.BigDecimal;

public class driverBean {
    private int routeId;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal z;

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getZ() {
        return z;
    }

    public void setZ(BigDecimal z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "driverBean{" +
                "routeId=" + routeId +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
