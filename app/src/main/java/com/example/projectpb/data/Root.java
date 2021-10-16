package com.example.projectpb.data;

import java.util.HashMap;
import java.util.Map;

public class Root{
    public String method;
    public boolean status;
    public Result result;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMethod() {
        return method;
    }

    public boolean isStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setResults(Result result) {
        this.result = result;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}
