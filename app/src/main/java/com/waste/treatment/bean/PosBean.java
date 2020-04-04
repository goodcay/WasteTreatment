package com.waste.treatment.bean;

public class PosBean {
    private double X;
    private double Y;

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "PosBean{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
