package com.skala.basic.data;

import lombok.Data;

@Data
public class XingEntity {

    public static final String TYPE_PEDESTRIAN = "횡단보도";
    public static final String YPE_INTERSECTION = "교차로";

    private Long id;
    private String name;
    private String address;
    private double lat;
    private double lng;
    private int seconds;

    public XingEntity(Long id, String name, String address, double lat, double lng, int seconds) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.seconds = seconds;
    }

}
