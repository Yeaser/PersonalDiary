package com.example.user.personaldiary;

/**
 * Created by User on 24-Jul-17.
 */

public class Travel_des {

    private String TRAVEL_PLAN, image;

    public Travel_des(){

    }

    public Travel_des(String TRAVEL_PLAN, String image){
        this.TRAVEL_PLAN = TRAVEL_PLAN;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTRAVEL_PLAN() {
        return TRAVEL_PLAN;
    }

    public void setTRAVEL_PLAN(String TRAVEL_PLAN) {
        this.TRAVEL_PLAN = TRAVEL_PLAN;
    }
}
