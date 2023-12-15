package com.example.taxibill.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.MainActivity;
import com.example.taxibill.R;
import com.example.taxibill.Utils.DialogUtils;
import com.example.taxibill.databinding.ActivityAddCarBinding;
import com.example.taxibill.databinding.YearPickerDialogBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddCar_Activity extends AppCompatActivity {

    ActivityAddCarBinding binding;
    DBhelper dBhelper;
    AlertDialog yearPickerDialog;
    YearPickerDialogBinding yearPickerDialogBinding;
    SQLiteDatabase db;
    Activity activity;
    Vehicle_Model vehicleModel;
    String URI="";
    String vehicle="",driverName="",mobileNumber="",vModel="",vMake="",vYear="",email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        dBhelper=new DBhelper(activity);
        db=dBhelper.getReadableDatabase();

        yearPickerDialogBinding= YearPickerDialogBinding.inflate(getLayoutInflater());
        yearPickerDialog= DialogUtils.getCustomAlertDialog(activity,yearPickerDialogBinding.getRoot());
        yearPickerDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.shape_popup_view));

        binding.vehicleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(activity)
                        .crop()
                        .cropOval()
                        .maxResultSize(512,512,true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                launcher.launch(it);
                            }
                        }));
            }
        });

        binding.linvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);

                yearPickerDialog.show();

                yearPickerDialogBinding.pickerYear.setMinValue(2000);
                yearPickerDialogBinding.pickerYear.setMaxValue(year);

            }
        });

        yearPickerDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPickerDialog.dismiss();
            }
        });

        yearPickerDialogBinding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPickerDialog.dismiss();
                vYear=String.valueOf(yearPickerDialogBinding.pickerYear.getValue());
                binding.vYearTv.setText(vYear);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle=binding.vehicle.getText().toString().trim().toUpperCase();
                vMake=binding.vMake.getText().toString().trim();
                vModel=binding.vModel.getText().toString().trim();
                driverName=binding.driverName.getText().toString().trim();
                mobileNumber=binding.mobileNumber.getText().toString().trim();
                email=binding.mobileNumber.getText().toString().trim();
                vYear=binding.vYearTv.getText().toString().trim();

                if(URI.equalsIgnoreCase("")){
                    binding.vehicleImgError.setText("Please Select a Image");
                    binding.vehicleImgError.setVisibility(View.VISIBLE);
                }else if(vehicle.equalsIgnoreCase("")){
                    binding.vehicleError.setText("Please Enter Vehilce Number");
                    binding.vehicleError.setVisibility(View.VISIBLE);
                }else if(vMake.equalsIgnoreCase("")){
                    binding.vMakeError.setText("Please Enter Vehicle Make");
                    binding.vMakeError.setVisibility(View.VISIBLE);
                }else if(vModel.equalsIgnoreCase("")){
                    binding.vModelError.setText("Please Enter Vehicle Model");
                    binding.vModelError.setVisibility(View.VISIBLE);
                }else if(driverName.equalsIgnoreCase("")){
                    binding.driverNameError.setText("Please Enter Owner Name");
                    binding.driverNameError.setVisibility(View.VISIBLE);
                }else if(mobileNumber.equalsIgnoreCase("")){
                    binding.mobileNumberError.setText("Please Enter Mobile Number");
                    binding.mobileNumberError.setVisibility(View.VISIBLE);
                }else if(mobileNumber.length()<10){
                    binding.mobileNumberError.setText("Invalid Mobile Number");
                    binding.mobileNumberError.setVisibility(View.VISIBLE);
                }else if(vYear.equalsIgnoreCase("")){
                    binding.vYearError.setText("Please Enter Vehicle Year");
                    binding.vYearError.setVisibility(View.VISIBLE);
                }else{

                    vehicleModel=new Vehicle_Model(0,URI,vehicle,vMake,vModel,driverName,mobileNumber,email,vYear);
                    dBhelper.InsertVehicle(vehicleModel);
                    Intent intent=new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    URI=uri.toString();
                    Glide.with(activity.getApplicationContext()).load(uri).into(binding.vehicleImg);
                    binding.vehicleImgError.setVisibility(View.GONE);
                }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                    Toast.makeText(activity, "Image Error", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onBackPressed() {
        finish();
    }
}