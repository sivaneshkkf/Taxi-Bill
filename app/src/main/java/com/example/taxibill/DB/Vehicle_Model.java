package com.example.taxibill.DB;

public class Vehicle_Model {
    private int COLUMN_ID;
    private String VEHICLE_IMG;
    private String V_NUMBER;
    private String V_MAKE;
    private String V_MODEL;
    private String DRIVER_NAME;
    private String MOBILE_NUMBER;
    private String EMAIL;
    private String V_YEAR;

    public Vehicle_Model(int COLUMN_ID, String VEHICLE_IMG, String v_NUMBER, String v_MAKE, String v_MODEL, String DRIVER_NAME, String MOBILE_NUMBER, String EMAIL, String v_YEAR) {
        this.COLUMN_ID = COLUMN_ID;
        this.VEHICLE_IMG = VEHICLE_IMG;
        V_NUMBER = v_NUMBER;
        V_MAKE = v_MAKE;
        V_MODEL = v_MODEL;
        this.DRIVER_NAME = DRIVER_NAME;
        this.MOBILE_NUMBER = MOBILE_NUMBER;
        this.EMAIL = EMAIL;
        V_YEAR = v_YEAR;
    }

    public int getCOLUMN_ID() {
        return COLUMN_ID;
    }

    public void setCOLUMN_ID(int COLUMN_ID) {
        this.COLUMN_ID = COLUMN_ID;
    }

    public String getVEHICLE_IMG() {
        return VEHICLE_IMG;
    }

    public void setVEHICLE_IMG(String VEHICLE_IMG) {
        this.VEHICLE_IMG = VEHICLE_IMG;
    }

    public String getV_NUMBER() {
        return V_NUMBER;
    }

    public void setV_NUMBER(String v_NUMBER) {
        V_NUMBER = v_NUMBER;
    }

    public String getV_MAKE() {
        return V_MAKE;
    }

    public void setV_MAKE(String v_MAKE) {
        V_MAKE = v_MAKE;
    }

    public String getV_MODEL() {
        return V_MODEL;
    }

    public void setV_MODEL(String v_MODEL) {
        V_MODEL = v_MODEL;
    }

    public String getDRIVER_NAME() {
        return DRIVER_NAME;
    }

    public void setDRIVER_NAME(String DRIVER_NAME) {
        this.DRIVER_NAME = DRIVER_NAME;
    }

    public String getMOBILE_NUMBER() {
        return MOBILE_NUMBER;
    }

    public void setMOBILE_NUMBER(String MOBILE_NUMBER) {
        this.MOBILE_NUMBER = MOBILE_NUMBER;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getV_YEAR() {
        return V_YEAR;
    }

    public void setV_YEAR(String v_YEAR) {
        V_YEAR = v_YEAR;
    }
}
