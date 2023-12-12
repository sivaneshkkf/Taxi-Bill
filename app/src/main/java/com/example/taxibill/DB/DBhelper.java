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
    public static final String BILL_TABLE="billTable";
    public static final String COLUMN_ID="columnId";
    public static final String VEHICLE="vehicle";
    public static final String DATE="date";
    public static final String DAY="day";
    public static final String MONTH="month";
    public static final String MONTH_TXT="monthTxt";
    public static final String YEAR="year";
    public static final String DATE_OBJ="dateObj";
    public static final String PICKUP_LOC="pickupLoc";
    public static final String DROP_LOC="dropLoc";
    public static final String DESC="description";
    public static final String TOTAL_KM="totalKm";
    public static final String PER_KM="perKm";
    public static final String TOLL_CHARGES="tollCharges";
    public static final String TOTAL_FAR="totalFar";

// VEHICLE TABLE

    public static final String V_TABLE="vehicleTable";
    public static final String V_COLUMN_ID="columnId";
    public static final String VEHICLE_IMG="vehicleImg";
    public static final String V_NUMBER="vehicleNumber";
    public static final String DRIVER_NAME="driverName";
    public static final String KM_RUNNING="kmRunning";
    public static final String V_MAKE="vehicleMake";
    public static final String V_MODEL="vehicleModel";
    public static final String V_YEAR="vehicleYear";
    public DBhelper(@Nullable Context context) {
        super(context, "Bill.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table= "CREATE TABLE " +BILL_TABLE+ "(" +
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                VEHICLE+ " TEXT, " +
                DATE+ " TEXT, " +
                DAY+ " TEXT, " +
                MONTH+ " TEXT, " +
                MONTH_TXT+ " TEXT, " +
                YEAR+ " TEXT, " +
                DATE_OBJ+ " DATE, " +
                PICKUP_LOC+ " TEXT, " +
                DROP_LOC+ " TEXT, " +
                DESC+ " TEXT, " +
                TOTAL_KM+ " INTEGER, " +
                PER_KM+ " INTEGER, " +
                TOLL_CHARGES+ " INTEGER, "+
                TOTAL_FAR+ " INTEGER) ";

        db.execSQL(table);

        String vehicleTable= "CREATE TABLE " +V_TABLE+ "(" +
                V_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                VEHICLE_IMG+ " TEXT, " +
                V_NUMBER+ " TEXT, " +
                DRIVER_NAME+ " TEXT, " +
                KM_RUNNING+ " TEXT, " +
                V_MAKE+ " TEXT, " +
                V_MODEL+ " TEXT, " +
                V_YEAR+ " DATE ) ";

        db.execSQL(vehicleTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


// Data Table
    public void InsetData(DB_Model db_model){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VEHICLE,db_model.getVEHICLE());
        values.put(DATE,db_model.getDATE());
        values.put(DAY,db_model.getDAY());
        values.put(MONTH,db_model.getMONTH());
        values.put(MONTH_TXT,db_model.getMONTH_TXT());
        values.put(YEAR,db_model.getYEAR());
        values.put(DATE_OBJ,db_model.getDATE_OBJ());
        values.put(PICKUP_LOC,db_model.getPICKUP_LOC());
        values.put(DROP_LOC,db_model.getDROP_LOC());
        values.put(DESC,db_model.getDESC());
        values.put(TOTAL_KM,db_model.getTOTAL_KM());
        values.put(PER_KM,db_model.getPER_KM());
        values.put(TOLL_CHARGES,db_model.getTOLL_CHARGES());
        values.put(TOTAL_FAR,db_model.getTOTAL_FAR());

        DB.insert(BILL_TABLE,null,values);
    }

    public ArrayList<DB_Model> getEveryOne(String Year,String Month){
        SQLiteDatabase DB=this.getReadableDatabase();
        String filterQuery="SELECT * FROM "+BILL_TABLE+ " WHERE "+ YEAR + "=? AND " +MONTH+ "=? ";
        String[] selectionArgs={Year,Month};
        Cursor c=DB.rawQuery(filterQuery,selectionArgs);
        ArrayList<DB_Model> data=new ArrayList<>();

        if(c.moveToFirst()){
            do {
                int id=c.getInt(0);
                String vehicle=c.getString(1);
                String date=c.getString(2);
                String day=c.getString(3);
                String month=c.getString(4);
                String monthTxt=c.getString(5);
                String year=c.getString(6);
                String dateObj=c.getString(7);
                String pickupLoc=c.getString(8);
                String dropLoc=c.getString(9);
                String description=c.getString(10);
                int totalKm=c.getInt(11);
                int perKm=c.getInt(12);
                int tollCharges=c.getInt(13);
                int totalFar=c.getInt(14);

                data.add(new DB_Model(id,vehicle,date,day,month,monthTxt,year,dateObj,pickupLoc,dropLoc,description,totalKm,perKm,tollCharges,totalFar));
            }while (c.moveToNext());
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
                           String description,
                           int totalKm,
                           int perKm,
                           String tollCharges,
                           String totalFar){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VEHICLE_IMG,vehicleImg);
        values.put(VEHICLE,vehicle);
        values.put(DATE,date);
        values.put(DAY,day);
        values.put(MONTH,month);
        values.put(MONTH_TXT,monthTxt);
        values.put(YEAR,year);
        values.put(DATE_OBJ,dateObj);
        values.put(PICKUP_LOC,pickupLoc);
        values.put(DROP_LOC,dropLoc);
        values.put(DESC,description);
        values.put(TOTAL_KM,totalKm);
        values.put(PER_KM,perKm);
        values.put(TOLL_CHARGES,tollCharges);
        values.put(TOTAL_FAR,totalFar);

        DB.update(BILL_TABLE,values,COLUMN_ID+ "=" + id,null);

    }

    public void deleteData(int id){
        SQLiteDatabase DB=this.getWritableDatabase();
        String query=" DELETE FROM "+BILL_TABLE+ " WHERE "+ COLUMN_ID + "=" +id;
        DB.execSQL(query);
        DB.close();
    }

    public String getVehicleModel(String vNumber){
        SQLiteDatabase DB=this.getReadableDatabase();
        String query = "SELECT * FROM " + V_TABLE + " WHERE " + V_NUMBER + "= ? ";
        String[] selectArgs={vNumber};
        Cursor cursor = DB.rawQuery(query, selectArgs);

        String vehicleModel = null;
        if (cursor != null && cursor.moveToFirst()) {
            vehicleModel = cursor.getString(cursor.getColumnIndexOrThrow(V_MODEL));
            cursor.close();
        }
        return vehicleModel;
    }


// Vehicle Table
    public void InsertVehicle(Vehicle_Model vehicleModel){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VEHICLE_IMG,vehicleModel.getVEHICLE_IMG());
        values.put(V_NUMBER,vehicleModel.getV_NUMBER());
        values.put(DRIVER_NAME,vehicleModel.getDRIVER_NAME());
        values.put(KM_RUNNING,vehicleModel.getKM_RUNNING());
        values.put(V_MAKE,vehicleModel.getV_MAKE());
        values.put(V_MODEL,vehicleModel.getV_MODEL());
        values.put(V_YEAR,vehicleModel.getV_YEAR());

        DB.insert(V_TABLE,null,values);
    }

    public ArrayList<Vehicle_Model> getEveryVehicle(){
        SQLiteDatabase DB=this.getReadableDatabase();
        String filterQuery="SELECT * FROM "+V_TABLE ;
        Cursor c=DB.rawQuery(filterQuery,null);
        ArrayList<Vehicle_Model> data=new ArrayList<>();

        if(c.moveToFirst()){
            do {
                int id=c.getInt(0);
                String vehicleImg=c.getString(1);
                String vNumber=c.getString(2);
                String driverName=c.getString(3);
                String kmRunning=c.getString(4);
                String vMake=c.getString(5);
                String vModel=c.getString(6);
                String vYear=c.getString(7);

                data.add(new Vehicle_Model(id,vehicleImg,vNumber,driverName,kmRunning,vMake,vModel,vYear));
            }while (c.moveToNext());
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
                               String vYear){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VEHICLE_IMG,vehicleImg);
        values.put(V_NUMBER,vNumber);
        values.put(DRIVER_NAME,driverName);
        values.put(KM_RUNNING,kmRunning);
        values.put(V_MAKE,vModel);
        values.put(V_MODEL,vMake);
        values.put(V_YEAR,vYear);

        DB.update(V_TABLE,values,COLUMN_ID+ "=" + id,null);

    }

    public void deleteVehicle(int id){
        SQLiteDatabase DB=this.getWritableDatabase();
        String query=" DELETE FROM "+V_TABLE + " WHERE " +COLUMN_ID + "=" +id;
        DB.execSQL(query);
        DB.close();
    }

}
