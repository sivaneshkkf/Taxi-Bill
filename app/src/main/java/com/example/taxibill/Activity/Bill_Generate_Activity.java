package com.example.taxibill.Activity;

import static com.example.taxibill.DB.Date_Formats.getCurrentDateFormat;
import static com.example.taxibill.DB.Date_Formats.monthMM;
import static com.example.taxibill.DB.Date_Formats.yearyyyy;
import static com.example.taxibill.Utils.App.gson;
import static com.github.mikephil.charting.animation.Easing.Linear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.text.ListFormatter;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.taxibill.Adapter.Pic_Drop_Adapter;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Date_Formats;
import com.example.taxibill.DB.Date_Model;
import com.example.taxibill.DB.PicDrop_Model;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.R;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.ActivityBillGenerateBinding;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.w3c.dom.DocumentType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill_Generate_Activity extends AppCompatActivity {
    ActivityBillGenerateBinding binding;
    Activity activity;
    DBhelper dBhelper;
    SQLiteDatabase db;
    String YEAR="",MONTH="";
    DB_Model db_model;
    Vehicle_Model vehicleModel;
    int columnId=0,pos=-1;
    Pic_Drop_Adapter picDropAdapter;
    List<PicDrop_Model> picDropModelList=new ArrayList<>();
    ArrayList<DB_Model> dbModelsList=new ArrayList<>();
    ArrayList<Vehicle_Model> vehicleModelArrayList=new ArrayList<>();
    private static final int REQUEST_WRITE_STORAGE = 112;
    String dirpath;
    LinearLayout linear;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBillGenerateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        setStatusBarTextLight();
        linear=binding.linScreen;

        dBhelper=new DBhelper(activity);
        db=dBhelper.getReadableDatabase();

        YEAR=getCurrentDateFormat(yearyyyy);
        MONTH=getCurrentDateFormat(monthMM);

        dbModelsList.clear();
        vehicleModelArrayList.clear();
        dbModelsList=dBhelper.getEveryOne(YEAR,MONTH);
        vehicleModelArrayList=dBhelper.getEveryVehicle();

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){
                columnId=bundle.getInt("id");
                pos=bundle.getInt("pos");
                getData(pos);
        }

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Size", ""+linear.getWidth()+" "+linear.getHeight());
                bitmap=LoadBitmap(linear,linear.getWidth(),linear.getHeight());

                checkPermission();
            }
        });

    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        WindowManager windowManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width=displayMetrics.widthPixels;
        float height=displayMetrics.heightPixels;
        int convertWidth=(int) width, converHeight = (int) height;

        PdfDocument document=new PdfDocument();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(convertWidth,converHeight,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);

        Canvas canvas=page.getCanvas();
        Paint paint=new Paint();
        canvas.drawPaint(paint);

        bitmap=Bitmap.createScaledBitmap(bitmap,convertWidth,converHeight,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

//  target pdf download
        String targetPdf="/sdcard/page.pdf";
        File file;
        file=new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        document.close();
        OpenPdf();
    }

    private void OpenPdf() {
        File file=new File("/sdcard/page.pdf");
        if(file.exists()){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri=Uri.fromFile(file);
            intent.setDataAndType(uri,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(activity, "Activity not found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with creating the PDF
                createPdf();
            } else {
                // Request permission
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        } else {
            // Permission is granted on devices running Android versions below M
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with creating the PDF
                createPdf();
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(getApplicationContext(), "Permission denied. Cannot create PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getData(int pos){
        db_model=dbModelsList.get(pos);
        String vNumber=db_model.getVEHICLE();
        binding.vNumber.setText(vNumber);

        for (int i=0; i<vehicleModelArrayList.size();i++){
            Vehicle_Model v_Model=vehicleModelArrayList.get(i);
            String vnumber=v_Model.getV_NUMBER();
            if(vnumber.equalsIgnoreCase(db_model.getVEHICLE())){
                vehicleModel=vehicleModelArrayList.get(i);
            }
        }

        binding.mobNumber.setText(vehicleModel.getMOBILE_NUMBER());
        binding.email.setText(vehicleModel.getEMAIL());

        String dateObj= db_model.getDATE_OBJ();
        try {
            Date date = Date_Formats.dateFormatDb.parse(dateObj);
            String dateText=Date_Formats.outputFormat.format(date);
            binding.date.setText(dateText);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        picDropModelList.clear();
        if(!db_model.getPICKUP_LOC().equalsIgnoreCase("")){
            String json = db_model.getPICKUP_LOC();
            Type listType = new TypeToken<List<PicDrop_Model>>(){}.getType();
            picDropModelList = gson.fromJson(json, listType);
            Log.i("picDropModelList", "picDropModelList: "+picDropModelList.toString());
        }

        picDropAdapter=new Pic_Drop_Adapter(activity, picDropModelList, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {

            }
        });
        binding.recyclerPicDrop.setAdapter(picDropAdapter);

        binding.km.setText(db_model.getTOTAL_KM()+" KM");
        binding.tollCharges.setText("₹ "+db_model.getTOLL_CHARGES());
        binding.totalFar.setText("₹"+db_model.getTOTAL_FAR());

    }

    private void setStatusBarTextLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.white));

            // Set the status bar text color to light
            View decor = window.getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


}