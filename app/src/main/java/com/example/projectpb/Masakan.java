package com.example.projectpb;

public class Masakan {
    private String judul;
    private String info;
    private final int imageResource;

    Masakan(String judul, String info,int imageResource){
        this.judul=judul;
        this.info=info;
        this.imageResource=imageResource;
    }

    public String getJudul(){
        return judul;
    }

    public String getInfo(){
        return info;
    }

    public int getImageResource(){
        return imageResource;
    }

}
