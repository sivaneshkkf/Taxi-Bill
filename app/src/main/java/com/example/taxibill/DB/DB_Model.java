package com.example.taxibill.DB;

public class DB_Model {
    private int COLUMN_ID;
    private String VEHICLE;
    private String DATE;
    private String DAY;
    private String MONTH;
    private String MONTH_TXT;
    private String YEAR;
    private String DATE_OBJ;
    private String PICKUP_LOC;
    private String DROP_LOC;
    private int TOTAL_KM;
    private int PER_KM;
    private int TOLL_CHARGES;
    private int TOTAL_FAR;

    public DB_Model(int COLUMN_ID, String VEHICLE, String DATE, String DAY, String MONTH, String MONTH_TXT, String YEAR, String DATE_OBJ, String PICKUP_LOC, String DROP_LOC, int TOTAL_KM, int PER_KM, int TOLL_CHARGES, int TOTAL_FAR) {
        this.COLUMN_ID = COLUMN_ID;
        this.VEHICLE = VEHICLE;
        this.DATE = DATE;
        this.DAY = DAY;
        this.MONTH = MONTH;
        this.MONTH_TXT = MONTH_TXT;
        this.YEAR = YEAR;
        this.DATE_OBJ = DATE_OBJ;
        this.PICKUP_LOC = PICKUP_LOC;
        this.DROP_LOC = DROP_LOC;
        this.TOTAL_KM = TOTAL_KM;
        this.PER_KM = PER_KM;
        this.TOLL_CHARGES = TOLL_CHARGES;
        this.TOTAL_FAR = TOTAL_FAR;
    }

    public int getCOLUMN_ID() {
        return COLUMN_ID;
    }

    public void setCOLUMN_ID(int COLUMN_ID) {
        this.COLUMN_ID = COLUMN_ID;
    }

    public String getVEHICLE() {
        return VEHICLE;
    }

    public void setVEHICLE(String VEHICLE) {
        this.VEHICLE = VEHICLE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getMONTH_TXT() {
        return MONTH_TXT;
    }

    public void setMONTH_TXT(String MONTH_TXT) {
        this.MONTH_TXT = MONTH_TXT;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getDATE_OBJ() {
        return DATE_OBJ;
    }

    public void setDATE_OBJ(String DATE_OBJ) {
        this.DATE_OBJ = DATE_OBJ;
    }

    public String getPICKUP_LOC() {
        return PICKUP_LOC;
    }

    public void setPICKUP_LOC(String PICKUP_LOC) {
        this.PICKUP_LOC = PICKUP_LOC;
    }

    public String getDROP_LOC() {
        return DROP_LOC;
    }

    public void setDROP_LOC(String DROP_LOC) {
        this.DROP_LOC = DROP_LOC;
    }

    public int getTOTAL_KM() {
        return TOTAL_KM;
    }

    public void setTOTAL_KM(int TOTAL_KM) {
        this.TOTAL_KM = TOTAL_KM;
    }

    public int getPER_KM() {
        return PER_KM;
    }

    public void setPER_KM(int PER_KM) {
        this.PER_KM = PER_KM;
    }

    public int getTOLL_CHARGES() {
        return TOLL_CHARGES;
    }

    public void setTOLL_CHARGES(int TOLL_CHARGES) {
        this.TOLL_CHARGES = TOLL_CHARGES;
    }

    public int getTOTAL_FAR() {
        return TOTAL_FAR;
    }

    public void setTOTAL_FAR(int TOTAL_FAR) {
        this.TOTAL_FAR = TOTAL_FAR;
    }
}
