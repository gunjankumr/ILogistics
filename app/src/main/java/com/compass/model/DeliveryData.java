package com.compass.model;

public class DeliveryData {
    private String shopLocation;
    private String startLocation;
    private String dropLocation;
    private String distance;
    private String arrivesBefore;

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getArrivesBefore() {
        return arrivesBefore;
    }

    public void setArrivesBefore(String arrivesBefore) {
        this.arrivesBefore = arrivesBefore;
    }
}
