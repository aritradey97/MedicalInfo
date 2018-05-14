package com.btechviral.android.bloodbank;

public class Item {
    private String name,place, blood, mh ;

    public Item(){
    }
    public Item(String name, String place, String blood, String mh){
        this.name = name;
        this.blood = blood;
        this.place = place;
        this.mh = mh;
    }

    public String getName() {
        return name;
    }

    public String getBloodGroup() {
        return blood;
    }

    public String getPlace() {
        return place;
    }

    public String getMedicHist() {
        return mh;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public void setBlood(String blood){
        this.blood = blood;
    }

    public void setMedicHist(String mh){
        this.mh = mh;
    }
}
