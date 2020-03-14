package com.waste.treatment.bean;

public class TypesContent {
    private String Name;
    private int Index;
    private int Oid;


    @Override
    public String toString() {
        return "TypesContent{" +
                "Name='" + Name + '\'' +
                ", Index=" + Index +
                ", Oid=" + Oid +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public int getOid() {
        return Oid;
    }

    public void setOid(int oid) {
        Oid = oid;
    }
}
