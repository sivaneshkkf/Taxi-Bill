package com.example.taxibill.DB;

public class PicDrop_Model {
    String num;
    String picLoc;
    String DropLoc;

    public PicDrop_Model(String num, String picLoc, String dropLoc) {
        this.num = num;
        this.picLoc = picLoc;
        DropLoc = dropLoc;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPicLoc() {
        return picLoc;
    }

    public void setPicLoc(String picLoc) {
        this.picLoc = picLoc;
    }

    public String getDropLoc() {
        return DropLoc;
    }

    public void setDropLoc(String dropLoc) {
        DropLoc = dropLoc;
    }
}
