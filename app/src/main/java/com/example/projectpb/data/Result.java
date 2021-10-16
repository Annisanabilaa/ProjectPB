package com.example.projectpb.data;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public String title;
    public String thumb;
    public String key;
    public String times;
    public String portion;
    public String dificulty;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }

    public String getKey() {
        return key;
    }

    public String getTimes() {
        return times;
    }

    public String getPortion() {
        return portion;
    }

    public String getDificulty() {
        return dificulty;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public void setDificulty(String dificulty) {
        this.dificulty = dificulty;
    }

    public Map<String, Object> getAdditionalProperties(){
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String title, Object value){
        this.additionalProperties.put(title, value);
    }
}
