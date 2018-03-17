package com.inusak.android.moviesapp.data;

/**
 * This class holds Trailer Data.
 * Created by Nilanka on 3/7/2018.
 */

public class TrailerData {

    private String trailerId;

    private String key;

    private String name;

    private String site;

    public TrailerData(String trailerId, String key, String name, String site) {
        this.trailerId = trailerId;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
