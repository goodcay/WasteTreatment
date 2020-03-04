package com.waste.treatment.bean;

public class CarsBean {
    private String Name;
    private int Oid;

    @Override
    public String toString() {
        return "CarsBean{" +
                "Name='" + Name + '\'' +
                ", Oid=" + Oid +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getOid() {
        return Oid;
    }

    public void setOid(int oid) {
        Oid = oid;
    }


}
