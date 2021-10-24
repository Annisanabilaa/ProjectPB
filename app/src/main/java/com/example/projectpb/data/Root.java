package com.example.projectpb.data;

public class Root{
    public String method;
    public boolean status;
    public Result result;

    public String getMethod() {
        return method;
    }

    public boolean isStatus() {
        return status;
    }

    public Result getResult() {
        return result;
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

}
