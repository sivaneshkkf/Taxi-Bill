package com.example.taxibill.Utils;


import android.Manifest;

public interface CommonConstants {

    String folderName = "/Savings";
    String[] CAM_STORAGE_PERM = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    String SHARED_PREF = "Savings";
    public static String SPACE = " ", COMMA = ",";
    public static String SHARED_PREF_NAME = "Savings";
    String doubleQuotes = "\"";

    String OTP = "OTP";
    public static String pipe = "|", NEXT_LINE = "\n", emptyString = "";

    public static String token="token",from="from", loginwithOTP="loginwithOTP", createAccount="createAccount",vendorID="vendorID",bottomSheetFrom="bottomSheetFrom",vendorCategory="vendorCategory";

    public static String vehicleNumber="vehicleNumber",
            vehicle="vehicle",
            date="date",
            day="day",
            month="month",
            year="year",
            dateObj="dateObj",
            pickupLoc="pickupLoc",
            dropLoc="dropLoc",
            desc="description",
            totalKm="totalKm",
            perKm="perKm",
            tollCharges="tollCharges",
            totalFar="totalFar",
            picDropLoc="picDropLoc",
            locVal="locVal",
            progressVal="progressVal",
            isTableCreated="isTableCreated";

String FOLDER_NAME="Savings";

}
