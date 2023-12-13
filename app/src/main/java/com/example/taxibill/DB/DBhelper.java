package com.example.taxibill.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    //TRIP TABLE
    public static final String BILL_TABLE = "billTable";
    public static final String COLUMN_ID = "columnId";
    public static final String VEHICLE = "vehicle";
    public static final String DATE = "date";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String MONTH_TXT = "monthTxt";
    public static final String YEAR = "year";
    public static final String DATE_OBJ = "dateObj";
    public static final String PICKUP_LOC = "pickupLoc";
    public static final String DROP_LOC = "dropLoc";
    public static final String TOTAL_KM = "totalKm";
    public static final String PER_KM = "perKm";
    public static final String TOLL_CHARGES = "tollCharges";
    public static final String TOTAL_FAR = "totalFar";

// VEHICLE TABLE

    public static final String V_TABLE = "vehicleTable";
    public static final String V_COLUMN_ID = "columnId";
    public static final String VEHICLE_IMG = "vehicleImg";
    public static final String V_NUMBER = "vehicleNumber";
    public static final String DRIVER_NAME = "driverName";
    public static final String KM_RUNNING = "kmRunning";
    public static final String V_MAKE = "vehicleMake";
    public static final String V_MODEL = "vehicleModel";
    public static final String V_YEAR = "vehicleYear";


    // Temp Data
    public static final String TEMP_DATA_TABLE = "tempData";
    public static final String T_COLUMN_ID = "columnId";
    public static final String T_VEHICLE = "vehicle";
    public static final String IS_VEHICLE = "isVehicle";
    public static final String T_DATE_MODEL = "dateModel";
    public static final String IS_DATEMODEL = "isDateModel";
    public static final String T_PIC_DROP_MODEL = "picDropModel";
    public static final String IS_PICDROPMODEL = "isPicDropModel";
    public static final String T_TOTAL_KM = "totalKm";
    public static final String IS_TOTALKM = "isTotalKm";
    public static final String T_PER_KM = "perKm";
    public static final String IS_PER_KM = "isPerKm";
    public static final String T_TOLL_CHARGES = "tollCharges";
    public static final String is_TOLL_CHARGES = "isTollCharges";
    public static final String T_TOTAL_FAR = "totalFar";
    public static final String IS_TOTAL_FAR = "isTotalFar";
    public static final String T_PROGRESS_VALUE = "progressValue";


    public DBhelper(@Nullable Context context) {
        super(context, "Bill.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + BILL_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VEHICLE + " TEXT, " +
                DATE + " TEXT, " +
                DAY + " TEXT, " +
                MONTH + " TEXT, " +
                MONTH_TXT + " TEXT, " +
                YEAR + " TEXT, " +
                DATE_OBJ + " DATE, " +
                PICKUP_LOC + " TEXT, " +
                DROP_LOC + " TEXT, " +
                TOTAL_KM + " INTEGER, " +
                PER_KM + " INTEGER, " +
                TOLL_CHARGES + " INTEGER, " +
                TOTAL_FAR + " INTEGER) ";

        db.execSQL(table);

        String vehicleTable = "CREATE TABLE " + V_TABLE + "(" +
                V_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VEHICLE_IMG + " TEXT, " +
                V_NUMBER + " TEXT, " +
                DRIVER_NAME + " TEXT, " +
                KM_RUNNING + " TEXT, " +
                V_MAKE + " TEXT, " +
                V_MODEL + " TEXT, " +
                V_YEAR + " DATE ) ";

        db.execSQL(vehicleTable);

        String tempTable = "CREATE TABLE " + TEMP_DATA_TABLE + "(" +
                T_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T_VEHICLE + " TEXT, " +
                IS_VEHICLE + " BOOLEAN, " +
                T_DATE_MODEL + " TEXT, " +
                IS_DATEMODEL + " BOOLEAN, " +
                T_PIC_DROP_MODEL + " TEXT, " +
                IS_PICDROPMODEL + " BOOLEAN, " +
                T_TOTAL_KM + " INTEGER, " +
                IS_TOTALKM + " BOOLEAN, " +
                T_PER_KM + " INTEGER, " +
                IS_PER_KM + " BOOLEAN, " +
                T_TOLL_CHARGES + " INTEGER, " +
                is_TOLL_CHARGES + "BOOLEAN, " +
                T_TOTAL_FAR + "INTEGER, " +
                IS_TOTAL_FAR + "BOOLEAN, " +
                T_PROGRESS_VALUE + "INTEGER ) ";
        db.execSQL(tempTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Data Table
    public void InsetData(DB_Model db_model) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VEHICLE, db_model.getVEHICLE());
        values.put(DATE, db_model.getDATE());
        values.put(DAY, db_model.getDAY());
        values.put(MONTH, db_model.getMONTH());
        values.put(MONTH_TXT, db_model.getMONTH_TXT());
        values.put(YEAR, db_model.getYEAR());
        values.put(DATE_OBJ, db_model.getDATE_OBJ());
        values.put(PICKUP_LOC, db_model.getPICKUP_LOC());
        values.put(DROP_LOC, db_model.getDROP_LOC());
        values.put(TOTAL_KM, db_model.getTOTAL_KM());
        values.put(PER_KM, db_model.getPER_KM());
        values.put(TOLL_CHARGES, db_model.getTOLL_CHARGES());
        values.put(TOTAL_FAR, db_model.getTOTAL_FAR());

        DB.insert(BILL_TABLE, null, values);
    }

    public ArrayList<DB_Model> getEveryOne(String Year, String Month) {
        SQLiteDatabase DB = this.getReadableDatabase();
        String filterQuery = "SELECT * FROM " + BILL_TABLE + " WHERE " + YEAR + "=? AND " + MONTH + "=? ";
        String[] selectionArgs = {Year, Month};
        Cursor c = DB.rawQuery(filterQuery, selectionArgs);
        ArrayList<DB_Model> data = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String vehicle = c.getString(1);
                String date = c.getString(2);
                String day = c.getString(3);
                String month = c.getString(4);
                String monthTxt = c.getString(5);
                String year = c.getString(6);
                String dateObj = c.getString(7);
                String pickupLoc = c.getString(8);
                String dropLoc = c.getString(9);
                int totalKm = c.getInt(10);
                int perKm = c.getInt(11);
                int tollCharges = c.getInt(12);
                int totalFar = c.getInt(13);

                data.add(new DB_Model(id, vehicle, date, day, month, monthTxt, year, dateObj, pickupLoc, dropLoc, totalKm, perKm, tollCharges, totalFar));
            } while (c.moveToNext());
        }
        c.close();
        return data;

    }

    public void updateData(int id,
                           String vehicleImg,
                           String vehicle,
                           String date,
                           String day,
                           String month,
                           String monthTxt,
                           String year,
                           String dateObj,
                           String pickupLoc,
                           String dropLoc,
                           int totalKm,
                           int perKm,
                           String tollCharges,
                           String totalFar) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VEHICLE_IMG, vehicleImg);
        values.put(VEHICLE, vehicle);
        values.put(DATE, date);
        values.put(DAY, day);
        values.put(MONTH, month);
        values.put(MONTH_TXT, monthTxt);
        values.put(YEAR, year);
        values.put(DATE_OBJ, dateObj);
        values.put(PICKUP_LOC, pickupLoc);
        values.put(DROP_LOC, dropLoc);
        values.put(TOTAL_KM, totalKm);
        values.put(PER_KM, perKm);
        values.put(TOLL_CHARGES, tollCharges);
        values.put(TOTAL_FAR, totalFar);

        DB.update(BILL_TABLE, values, COLUMN_ID + "=" + id, null);

    }

    public void deleteData(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = " DELETE FROM " + BILL_TABLE + " WHERE " + COLUMN_ID + "=" + id;
        DB.execSQL(query);
        DB.close();
    }

    public String getVehicleModel(String vNumber) {
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "SELECT * FROM " + V_TABLE + " WHERE " + V_NUMBER + "= ? ";
        String[] selectArgs = {vNumber};
        Cursor cursor = DB.rawQuery(query, selectArgs);

        String vehicleModel = null;
        if (cursor != null && cursor.moveToFirst()) {
            vehicleModel = cursor.getString(cursor.getColumnIndexOrThrow(V_MODEL));
            cursor.close();
        }
        return vehicleModel;
    }


    // Vehicle Table
    public void InsertVehicle(Vehicle_Model vehicleModel) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VEHICLE_IMG, vehicleModel.getVEHICLE_IMG());
        values.put(V_NUMBER, vehicleModel.getV_NUMBER());
        values.put(DRIVER_NAME, vehicleModel.getDRIVER_NAME());
        values.put(KM_RUNNING, vehicleModel.getKM_RUNNING());
        values.put(V_MAKE, vehicleModel.getV_MAKE());
        values.put(V_MODEL, vehicleModel.getV_MODEL());
        values.put(V_YEAR, vehicleModel.getV_YEAR());

        DB.insert(V_TABLE, null, values);
    }

    public ArrayList<Vehicle_Model> getEveryVehicle() {
        SQLiteDatabase DB = this.getReadableDatabase();
        String filterQuery = "SELECT * FROM " + V_TABLE;
        Cursor c = DB.rawQuery(filterQuery, null);
        ArrayList<Vehicle_Model> data = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String vehicleImg = c.getString(1);
                String vNumber = c.getString(2);
                String driverName = c.getString(3);
                String kmRunning = c.getString(4);
                String vMake = c.getString(5);
                String vModel = c.getString(6);
                String vYear = c.getString(7);

                data.add(new Vehicle_Model(id, vehicleImg, vNumber, driverName, kmRunning, vMake, vModel, vYear));
            } while (c.moveToNext());
        }
        c.close();
        return data;

    }

    public void updateVehicleData(int id, String vehicleImg,
                                  String vNumber,
                                  String driverName,
                                  String kmRunning,
                                  String vMake,
                                  String vModel,
                                  String vYear) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VEHICLE_IMG, vehicleImg);
        values.put(V_NUMBER, vNumber);
        values.put(DRIVER_NAME, driverName);
        values.put(KM_RUNNING, kmRunning);
        values.put(V_MAKE, vModel);
        values.put(V_MODEL, vMake);
        values.put(V_YEAR, vYear);

        DB.update(V_TABLE, values, COLUMN_ID + "=" + id, null);

    }

    public void deleteVehicle(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = " DELETE FROM " + V_TABLE + " WHERE " + COLUMN_ID + "=" + id;
        DB.execSQL(query);
        DB.close();
    }


    // Temp Table
    public void InsertTempData(Temp_Data_Model tempDataModel) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_VEHICLE, tempDataModel.getT_VEHICLE());
        values.put(IS_VEHICLE, tempDataModel.isIS_VEHICLE());
        values.put(T_DATE_MODEL, tempDataModel.getT_DATE_MODEL());
        values.put(IS_DATEMODEL, tempDataModel.isIS_DATEMODEL());
        values.put(T_PIC_DROP_MODEL, tempDataModel.getT_PIC_DROP_MODEL());
        values.put(IS_PICDROPMODEL, tempDataModel.isIS_PICDROPMODEL());
        values.put(T_TOTAL_KM, tempDataModel.getT_TOTAL_KM());
        values.put(IS_TOTALKM, tempDataModel.isIS_TOTALKM());
        values.put(T_PER_KM, tempDataModel.getT_PER_KM());
        values.put(IS_PER_KM, tempDataModel.isIS_PER_KM());
        values.put(T_TOLL_CHARGES, tempDataModel.getT_TOLL_CHARGES());
        values.put(is_TOLL_CHARGES, tempDataModel.isIs_TOLL_CHARGES());
        values.put(T_TOTAL_FAR, tempDataModel.getT_TOTAL_FAR());
        values.put(IS_TOTAL_FAR, tempDataModel.isIS_TOTAL_FAR());
        values.put(T_PROGRESS_VALUE, tempDataModel.getT_PROGRESS_VALUE());

        DB.insert(TEMP_DATA_TABLE, null, values);
    }

    public Temp_Data_Model getTempData(){
        SQLiteDatabase DB=this.getReadableDatabase();
        String filterQuery="SELECT * FROM "+TEMP_DATA_TABLE ;
        Cursor c=DB.rawQuery(filterQuery,null);
        Temp_Data_Model tempDataModel=null;

        if(c.moveToFirst()){
            do {
                int T_COLUMN_ID=c.getInt(1);
                String T_VEHICLE=c.getString(2);
                boolean IS_VEHICLE=(c.getInt(3)==1);
                String T_DATE_MODEL=c.getString(4);
                boolean IS_DATEMODEL=(c.getInt(5)==1);
                String T_PIC_DROP_MODEL=c.getString(6);
                boolean IS_PICDROPMODEL=(c.getInt(7)==1);
                int T_TOTAL_KM=c.getInt(7);
                boolean IS_TOTALKM=(c.getInt(7)==1);
                int T_PER_KM=c.getInt(7);
                boolean IS_PER_KM=(c.getInt(7)==1);
                int T_TOLL_CHARGES=c.getInt(7);
                boolean is_TOLL_CHARGES=(c.getInt(7)==1);
                int T_TOTAL_FAR=c.getInt(7);
                boolean IS_TOTAL_FAR=(c.getInt(7)==1);
                int T_PROGRESS_VALUE=c.getInt(7);

                tempDataModel =new Temp_Data_Model(T_COLUMN_ID,
                        T_VEHICLE,
                        IS_VEHICLE,
                        T_DATE_MODEL,
                        IS_DATEMODEL,
                        T_PIC_DROP_MODEL,
                        IS_PICDROPMODEL,
                        T_TOTAL_KM,
                        IS_TOTALKM,
                        T_PER_KM,
                        IS_PER_KM,
                        T_TOLL_CHARGES,
                        is_TOLL_CHARGES,
                        T_TOTAL_FAR,
                        IS_TOTAL_FAR,
                        T_PROGRESS_VALUE);
            }while (c.moveToNext());
        }
        c.close();
        return tempDataModel;

    }

}

