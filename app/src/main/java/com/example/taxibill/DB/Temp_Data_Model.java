package com.example.taxibill.DB;

public class Temp_Data_Model {
    private int T_COLUMN_ID;
    private String T_VEHICLE;
    private boolean IS_VEHICLE;
    private String T_DATE_MODEL;
    private boolean IS_DATEMODEL;
    private String T_PIC_DROP_MODEL;
    private boolean IS_PICDROPMODEL;
    private String T_DESC;
    private int T_TOTAL_KM;
    private boolean IS_TOTALKM;
    private int T_PER_KM;
    private boolean IS_PER_KM;
    private int T_TOLL_CHARGES;
    private boolean is_TOLL_CHARGES;
    private int T_TOTAL_FAR;
    private boolean IS_TOTAL_FAR;
    private int T_PROGRESS_VALUE;

    public Temp_Data_Model(int t_COLUMN_ID, String t_VEHICLE, boolean IS_VEHICLE, String t_DATE_MODEL, boolean IS_DATEMODEL, String t_PIC_DROP_MODEL, boolean IS_PICDROPMODEL, String T_DESC, int t_TOTAL_KM, boolean IS_TOTALKM, int t_PER_KM, boolean IS_PER_KM, int t_TOLL_CHARGES, boolean is_TOLL_CHARGES, int t_TOTAL_FAR, boolean IS_TOTAL_FAR, int t_PROGRESS_VALUE) {
        T_COLUMN_ID = t_COLUMN_ID;
        T_VEHICLE = t_VEHICLE;
        this.IS_VEHICLE = IS_VEHICLE;
        T_DATE_MODEL = t_DATE_MODEL;
        this.IS_DATEMODEL = IS_DATEMODEL;
        T_PIC_DROP_MODEL = t_PIC_DROP_MODEL;
        this.IS_PICDROPMODEL = IS_PICDROPMODEL;
        this.T_DESC = T_DESC;
        T_TOTAL_KM = t_TOTAL_KM;
        this.IS_TOTALKM = IS_TOTALKM;
        T_PER_KM = t_PER_KM;
        this.IS_PER_KM = IS_PER_KM;
        T_TOLL_CHARGES = t_TOLL_CHARGES;
        this.is_TOLL_CHARGES = is_TOLL_CHARGES;
        T_TOTAL_FAR = t_TOTAL_FAR;
        this.IS_TOTAL_FAR = IS_TOTAL_FAR;
        T_PROGRESS_VALUE = t_PROGRESS_VALUE;
    }

    public int getT_COLUMN_ID() {
        return T_COLUMN_ID;
    }

    public void setT_COLUMN_ID(int t_COLUMN_ID) {
        T_COLUMN_ID = t_COLUMN_ID;
    }

    public String getT_VEHICLE() {
        return T_VEHICLE;
    }

    public void setT_VEHICLE(String t_VEHICLE) {
        T_VEHICLE = t_VEHICLE;
    }

    public boolean isIS_VEHICLE() {
        return IS_VEHICLE;
    }

    public void setIS_VEHICLE(boolean IS_VEHICLE) {
        this.IS_VEHICLE = IS_VEHICLE;
    }

    public String getT_DATE_MODEL() {
        return T_DATE_MODEL;
    }

    public void setT_DATE_MODEL(String t_DATE_MODEL) {
        T_DATE_MODEL = t_DATE_MODEL;
    }

    public boolean isIS_DATEMODEL() {
        return IS_DATEMODEL;
    }

    public void setIS_DATEMODEL(boolean IS_DATEMODEL) {
        this.IS_DATEMODEL = IS_DATEMODEL;
    }

    public String getT_PIC_DROP_MODEL() {
        return T_PIC_DROP_MODEL;
    }

    public void setT_PIC_DROP_MODEL(String t_PIC_DROP_MODEL) {
        T_PIC_DROP_MODEL = t_PIC_DROP_MODEL;
    }

    public boolean isIS_PICDROPMODEL() {
        return IS_PICDROPMODEL;
    }

    public void setIS_PICDROPMODEL(boolean IS_PICDROPMODEL) {
        this.IS_PICDROPMODEL = IS_PICDROPMODEL;
    }

    public String getT_DESC() {
        return T_DESC;
    }

    public void setT_DESC(String T_DESC) {
        this.T_DESC = T_DESC;
    }

    public int getT_TOTAL_KM() {
        return T_TOTAL_KM;
    }

    public void setT_TOTAL_KM(int t_TOTAL_KM) {
        T_TOTAL_KM = t_TOTAL_KM;
    }

    public boolean isIS_TOTALKM() {
        return IS_TOTALKM;
    }

    public void setIS_TOTALKM(boolean IS_TOTALKM) {
        this.IS_TOTALKM = IS_TOTALKM;
    }

    public int getT_PER_KM() {
        return T_PER_KM;
    }

    public void setT_PER_KM(int t_PER_KM) {
        T_PER_KM = t_PER_KM;
    }

    public boolean isIS_PER_KM() {
        return IS_PER_KM;
    }

    public void setIS_PER_KM(boolean IS_PER_KM) {
        this.IS_PER_KM = IS_PER_KM;
    }

    public int getT_TOLL_CHARGES() {
        return T_TOLL_CHARGES;
    }

    public void setT_TOLL_CHARGES(int t_TOLL_CHARGES) {
        T_TOLL_CHARGES = t_TOLL_CHARGES;
    }

    public boolean isIs_TOLL_CHARGES() {
        return is_TOLL_CHARGES;
    }

    public void setIs_TOLL_CHARGES(boolean is_TOLL_CHARGES) {
        this.is_TOLL_CHARGES = is_TOLL_CHARGES;
    }

    public int getT_TOTAL_FAR() {
        return T_TOTAL_FAR;
    }

    public void setT_TOTAL_FAR(int t_TOTAL_FAR) {
        T_TOTAL_FAR = t_TOTAL_FAR;
    }

    public boolean isIS_TOTAL_FAR() {
        return IS_TOTAL_FAR;
    }

    public void setIS_TOTAL_FAR(boolean IS_TOTAL_FAR) {
        this.IS_TOTAL_FAR = IS_TOTAL_FAR;
    }

    public int getT_PROGRESS_VALUE() {
        return T_PROGRESS_VALUE;
    }

    public void setT_PROGRESS_VALUE(int t_PROGRESS_VALUE) {
        T_PROGRESS_VALUE = t_PROGRESS_VALUE;
    }
}
