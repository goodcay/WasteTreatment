package com.waste.treatment.bean;

import java.util.List;

public class GetPosContentBean {
    private int RouteId;
    private String Begin;
    private String End;
    private List<PosBean> Pos;

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getBegin() {
        return Begin;
    }

    public void setBegin(String begin) {
        Begin = begin;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public List<PosBean> getPos() {
        return Pos;
    }

    public void setPos(List<PosBean> pos) {
        Pos = pos;
    }

    @Override
    public String toString() {
        return "GetPosContentBean{" +
                "RouteId=" + RouteId +
                ", Begin='" + Begin + '\'' +
                ", End='" + End + '\'' +
                ", Pos=" + Pos +
                '}';
    }
}
