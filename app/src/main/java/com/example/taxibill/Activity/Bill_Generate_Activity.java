package com.example.taxibill.Activity;

import static com.example.taxibill.DB.Date_Formats.getCurrentDateFormat;
import static com.example.taxibill.DB.Date_Formats.monthMM;
import static com.example.taxibill.DB.Date_Formats.yearyyyy;
import static com.example.taxibill.Utils.App.gson;
import static com.github.mikephil.charting.animation.Easing.Linear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

                convertLayoutToPDFAndShare();
            }
        });

    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public void convertLayoutToPDFAndShare() {

        View layoutReceipt = binding.linScreen; // Get a reference to the layoutReceipt
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(layoutReceipt.getWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        layoutReceipt.measure(widthMeasureSpec, heightMeasureSpec);
        int height = layoutReceipt.getMeasuredHeight();

        Bitmap bitmap = Bitmap.createBitmap(layoutReceipt.getWidth(), height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layoutReceipt.draw(canvas);

        // Create a PDF document
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), height, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        // Save the PDF to a file
        File pdfFile = new File(activity.getExternalFilesDir(null), "receipt.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharePDFFile(pdfFile);


        // Share the PDF via WhatsApp
        //sharePDFViaWhatsApp(pdfFile);
    }

//    open pdf
    private void OpenPdf(File file) {
        String path = file.getPath();
        Log.i("file_path", "OpenPdf: " + path);

        if (!path.equalsIgnoreCase("")) {
            Uri contentUri = FileProvider.getUriForFile(
                    activity,
                    activity.getApplicationContext().getPackageName() + ".fileprovider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "Activity not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sharePDFFile(File pdfFile) {
        Uri contentUri = FileProvider.getUriForFile(
                this,
                this.getApplicationContext().getPackageName() + ".fileprovider",
                pdfFile);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(shareIntent, "Share PDF using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found to open PDF", Toast.LENGTH_SHORT).show();
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


}