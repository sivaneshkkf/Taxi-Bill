package com.example.taxibill.Fragment;


import static com.example.taxibill.DB.Date_Formats.dateFormatDb;
import static com.example.taxibill.DB.Date_Formats.dateFormatNativeDate;
import static com.example.taxibill.DB.Date_Formats.dayEEEE;
import static com.example.taxibill.DB.Date_Formats.getCurrentDateFormat;
import static com.example.taxibill.DB.Date_Formats.getDateFormat;

import static com.example.taxibill.DB.Date_Formats.monthMM;
import static com.example.taxibill.DB.Date_Formats.monthMMM;
import static com.example.taxibill.DB.Date_Formats.yearyyyy;
import static com.example.taxibill.Utils.App.gson;
import static com.example.taxibill.Utils.CommonConstants.dropLoc;
import static com.example.taxibill.Utils.CommonConstants.perKm;
import static com.example.taxibill.Utils.CommonConstants.pickupLoc;
import static com.example.taxibill.Utils.CommonConstants.tollCharges;
import static com.example.taxibill.Utils.CommonConstants.totalKm;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taxibill.Adapter.Pic_Drop_Loc_Adapter;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Date_Model;
import com.example.taxibill.DB.PicDrop_Model;
import com.example.taxibill.DB.Temp_Data_Model;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.MainActivity;
import com.example.taxibill.Myutils.ProgressBar_increase;
import com.example.taxibill.Myutils.ValueChangeListener;
import com.example.taxibill.R;
import com.example.taxibill.Utils.CommonConstants;
import com.example.taxibill.Utils.MyPrefs;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.FragmentAddBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.sql.Date;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Add_Fragment extends Fragment {

    FragmentAddBinding binding;
    ArrayList<DB_Model> allData=new ArrayList<>();
    DBhelper dBhelper;
    DB_Model db_model;
    SQLiteDatabase db;
    public static Temp_Data_Model tempDataModel=null;
    String YEAR="",MONTH="";
    List<String> vehicleList=new ArrayList<>();
    List<Vehicle_Model> list=new ArrayList<>();

    List<PicDrop_Model> picDropModelsList=new ArrayList<>();
    Pic_Drop_Loc_Adapter picDropLocAdapter;
    String json="";
    public static LinearLayout linBtn;

    int locVal=1;
    String vehicle="",date="",day="",month="",monthTxt,year="",dateObj="",pickupLoc="",dropLoc="",description="",totalKm="",perKm="",tollCharges="",totalFar="";
    int totalKmINT=0,perKmINT=0,tollChargesINT=0,totalFarINT=0;
    int progressVal=0;
    int val=0;
    public static ValueChangeListener myObject = new ValueChangeListener();
    int t_ColumnId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        linBtn=binding.linBtn;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dBhelper=new DBhelper(getActivity());
        db=dBhelper.getReadableDatabase();

        YEAR=getCurrentDateFormat(yearyyyy);
        MONTH=getCurrentDateFormat(monthMM);

        list=dBhelper.getEveryVehicle();

        for(int i=0;i<list.size();i++){
            Vehicle_Model vehicleModel=list.get(i);
            vehicleList.add(vehicleModel.getV_NUMBER());
        }

        ArrayAdapter spinnerAdapter= new ArrayAdapter<>(getActivity(),R.layout.spinner,vehicleList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner);
        binding.vehicleSpinner.setAdapter(spinnerAdapter);

        allData=dBhelper.getEveryOne(YEAR,MONTH);

        getSharedPrefValues();
        /*if(MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal)>0){
           //locVal=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal);
        }*/


        //Log.i("list_list", "list: "+picDropModelsList.get(0).getPicLoc());

        binding.vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle=vehicleList.get(position);
                tempDataModel.setT_VEHICLE(vehicle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.totalKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.totalKmError.setVisibility(View.INVISIBLE);
                MyPrefs.getInstance(getActivity()).putString(CommonConstants.totalKm,s.toString());
                if(s.length()>0){
                    val=Integer.parseInt(s.toString().trim());
                    totalKmINT=val;

                    tempDataModel.setT_TOTAL_KM(totalKmINT);
                    if(tempDataModel.isIS_TOTALKM()){
                        int oldvalue=progressVal;
                        progressVal+=valArray[3];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIS_TOTALKM(false);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }
                }else{
                    val=0;
                    totalKmINT=val;
                    tempDataModel.setT_TOTAL_KM(totalKmINT);
                    if(!tempDataModel.isIS_TOTALKM()){
                        int oldvalue=progressVal;
                        progressVal-=valArray[3];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIS_TOTALKM(true);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
                binding.totalFar.setText("₹"+String.valueOf(totalFarINT));
            }
        });

        binding.perKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.perKmError.setVisibility(View.INVISIBLE);
                MyPrefs.getInstance(getActivity()).putString(CommonConstants.perKm,s.toString());
                if(s.length()>0){
                    val=Integer.parseInt(s.toString().trim());
                    perKmINT=val;

                    tempDataModel.setT_PER_KM(perKmINT);
                    if(tempDataModel.isIS_PER_KM()){
                        int oldvalue=progressVal;
                        progressVal+=valArray[4];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIS_PER_KM(false);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }

                }else{
                    val=0;
                    perKmINT=val;

                    tempDataModel.setT_PER_KM(perKmINT);
                    if(!tempDataModel.isIS_PER_KM()){
                        int oldvalue=progressVal;
                        progressVal-=valArray[4];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIS_PER_KM(true);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
                binding.totalFar.setText("₹"+String.valueOf(totalFarINT));
            }
        });

        binding.tollCharges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tollChargesError.setVisibility(View.INVISIBLE);
                MyPrefs.getInstance(getActivity()).putString(CommonConstants.tollCharges,s.toString());
                if(s.length()>0){
                    val=Integer.parseInt(s.toString().trim());
                    tollChargesINT=val;

                    tempDataModel.setT_TOLL_CHARGES(tollChargesINT);
                    if(tempDataModel.isIs_TOLL_CHARGES()){
                        int oldvalue=progressVal;
                        progressVal+=valArray[5];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIs_TOLL_CHARGES(false);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }


                }else{
                    val=0;
                    tollChargesINT=val;

                    tempDataModel.setT_TOLL_CHARGES(tollChargesINT);
                    if(!tempDataModel.isIs_TOLL_CHARGES()){
                        int oldvalue=progressVal;
                        progressVal-=valArray[5];
                        tempDataModel.setT_PROGRESS_VALUE(progressVal);
                        tempDataModel.setIs_TOLL_CHARGES(true);
                        myObject.setMyValue(progressVal);
                        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
                binding.totalFar.setText("₹"+String.valueOf(totalFarINT));
            }
        });


        binding.linearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                int year1=c.get(Calendar.YEAR);
                int month1=c.get(Calendar.MONTH);
                int date1=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year2, int mon, int dayOfMonth) {

                       date=String.valueOf(dayOfMonth);
                       month=String.valueOf(mon+1);
                       year=String.valueOf(year1);
                       dateObj=getDateFormat(dateFormatDb,year2,mon,dayOfMonth);
                       monthTxt=getDateFormat(monthMMM,year2,mon,dayOfMonth);
                       day=getDateFormat(dayEEEE,year2,mon,dayOfMonth);

                       String dateTxt=getDateFormat(dateFormatNativeDate,year2,mon,dayOfMonth);
                       binding.date.setText(dateTxt);

                        Date_Model dateModel=new Date_Model(date,month,year,dateObj,monthTxt,day,dateTxt);

                        Gson gson = new Gson();
                        String jsonDate = gson.toJson(dateModel);

                        tempDataModel.setT_DATE_MODEL(jsonDate);
                        if(tempDataModel.isIS_DATEMODEL()){
                            int oldvalue=progressVal;
                            progressVal+=valArray[1];
                            tempDataModel.setT_PROGRESS_VALUE(progressVal);
                            tempDataModel.setIS_DATEMODEL(false);
                            myObject.setMyValue(progressVal);
                            increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                        }



                        Log.i("date_formats", "date: "+date);
                        Log.i("date_formats", "month: "+month);
                        Log.i("date_formats", "year: "+year);
                        Log.i("date_formats", "date: "+dateObj);
                        Log.i("date_formats", "date: "+monthTxt);

                    }
                },year1,month1,date1);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locVal+=1;
                MyPrefs.getInstance(getActivity()).putInt(CommonConstants.locVal,locVal);
                picDropModelsList.add(new PicDrop_Model(String.valueOf(locVal),"",""));
                picDropLocAdapter.notifyItemInserted(picDropModelsList.size());
            }
        });

       /* picDropLocAdapter=new Pic_Drop_Loc_Adapter(getActivity(), picDropModelsList, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {

                Gson gson = new Gson();
                String json = gson.toJson(picDropModelsList);

                MyPrefs.getInstance(getActivity()).putString(CommonConstants.picDropLoc,json);

                if(v.getId()==R.id.delete){
                    picDropModelsList.remove(i);
                    picDropLocAdapter.notifyItemRemoved(i);
                    *//*if(locVal>0){
                        locVal-=1;
                    }*//*



                    for(int j=0;j<picDropModelsList.size();j++){
                        picDropModelsList.get(j).setNum(String.valueOf(j+1));
                        picDropLocAdapter.notifyItemChanged(j);
                    }

                    locVal=picDropModelsList.size();
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.locVal,locVal);

                }
            }
        });*/
        picDropLocAdapter = new Pic_Drop_Loc_Adapter(getActivity(), picDropModelsList, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {

                Gson gson = new Gson();
                String json = gson.toJson(picDropModelsList);

                tempDataModel.setT_PIC_DROP_MODEL(json);
                pickupLoc=json;
                dropLoc=json;
                if(tempDataModel.isIS_PICDROPMODEL()){
                    int oldvalue=progressVal;
                    progressVal+=valArray[2];
                    tempDataModel.setT_PROGRESS_VALUE(progressVal);
                    tempDataModel.setIS_PICDROPMODEL(false);
                    myObject.setMyValue(progressVal);
                    increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
                }


                if (v.getId() == R.id.delete) {
                    if(picDropModelsList.size()>1) {
                        picDropModelsList.remove(i);
                        picDropLocAdapter.notifyItemRemoved(i);
                        locVal -= 1;

                        for (int j = 0; j < picDropModelsList.size(); j++) {
                            picDropModelsList.get(j).setNum(String.valueOf(j + 1));
                            picDropLocAdapter.notifyItemChanged(j);
                        }

                        locVal = picDropModelsList.size();
                        MyPrefs.getInstance(getActivity()).putInt(CommonConstants.locVal, locVal);
                    }
                }

                /*if(v.getId()==R.id.picLoc){
                    if(i==0){
                        if(!picDropModelsList.get(i).getPicLoc().equalsIgnoreCase("")){
                            progressVal+=(100/6)/2;
                            MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);
                        }else{
                            progressVal-=(100/6)/2;
                            MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);
                        }
                    }
                }
                if(v.getId()==R.id.dropLoc){
                    if(i==0){
                        if(!picDropModelsList.get(i).getDropLoc().equalsIgnoreCase("")){
                            progressVal+=(100/6)/2;
                            MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);
                        }else{
                            progressVal-=(100/6)/2;
                            MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);
                        }

                    }
                }*/
            }
        });
        binding.recycelrPicDropLoc.setAdapter(picDropLocAdapter);

        editTxt(binding.desc,binding.descError);

        if(vehicleList.size()>0){
            binding.vehicleSpinner.setSelection(0);
            vehicle=vehicleList.get(0);

            tempDataModel.setT_VEHICLE(vehicle);
            if(tempDataModel.isIS_VEHICLE()){
                int oldvalue=progressVal;
                progressVal+=valArray[0];
                tempDataModel.setT_PROGRESS_VALUE(progressVal);
                tempDataModel.setIS_VEHICLE(false);
                myObject.setMyValue(progressVal);
                increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
            }

        }

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalKm=binding.totalKm.getText().toString().trim();
                perKm=binding.perKm.getText().toString().trim();
                tollCharges=binding.tollCharges.getText().toString().trim();
                description=binding.desc.getText().toString().trim();

               if(date.equalsIgnoreCase("")){
                    binding.dateError.setText("Please Select A Date");
                    binding.dateError.setVisibility(View.VISIBLE);
                }/*else if(pickupLoc.equalsIgnoreCase("")){
                   binding.pickupDropLocError.setText("Please Enter Pickup Location");
                   binding.pickupDropLocError.setVisibility(View.VISIBLE);
                }*/else if(tempDataModel.isIS_PICDROPMODEL()){
                   binding.pickupDropLocError.setText("Please Enter Drop Location");
                   binding.pickupDropLocError.setVisibility(View.VISIBLE);
                }else if(totalKm.equalsIgnoreCase("")){
                   binding.totalKmError.setText("Please Enter Total KM");
                   binding.totalKmError.setVisibility(View.VISIBLE);
                }else{
                   db_model=new DB_Model(0, vehicle, date, day, month, monthTxt, year, dateObj, pickupLoc, dropLoc,description,totalKmINT, perKmINT, tollChargesINT, totalFarINT);
                   dBhelper.InsetData(db_model);
                   Intent intent=new Intent(getActivity(), MainActivity.class);
                   startActivity(intent);

                   clearDB();
                }

            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                clearDB();
            }
        });


        Glide.with(getActivity()).asGif().load(R.drawable.car_gif).into(binding.seekBarGif);


       /* binding.totalKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    *//*if(totalKmINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }*//*
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.perKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    *//*if(perKmINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }*//*
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.tollCharges.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                   *//* if(tollChargesINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }*//*
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        // Add a property change listener
        myObject.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ("myValue".equals(evt.getPropertyName())) {
                            System.out.println("myValue changed from " + evt.getOldValue() + " to " + evt.getNewValue());
                            // Your action here
                            int oldvalue=ValueChangeListener.myValue;

                            increaseProgressSmoothly(oldvalue,progressVal,binding.progressBar,2000);
                            //ProgressBar_increase.Increase(oldvalue,progressVal,binding.progressBar,20);

                            binding.percentage.setText(progressVal+"%");
                        }
                    }
                },1000);
            }
        });


        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String value = String.valueOf(progress);
                /*if (progress % 10 == 0) {
                    Range = progress;
                    binding.seekbarValue.setText(value);
                }*/
                int x = seekBar.getThumb().getBounds().left;

                //set the left value to textview x value
                binding.seekBarGif.setX(x);
                //binding.seekbarVal.setText(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

    }


    public static void increaseProgressSmoothly(int startValue, int endValue, ProgressBar progressBar, long duration) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", startValue, endValue);
        progressAnimator.setDuration(duration);
        progressAnimator.start();
    }
    public static void increaseSeekBarSmoothly(int startValue, int endValue, SeekBar seekBar, long duration) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(seekBar, "progress", startValue, endValue);
        progressAnimator.setDuration(duration);
        progressAnimator.start();
    }
   /* public void animate(View view, int percentage) {
        Context context = requireContext();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int translationX = (int) ((percentage / 100.0) * screenWidth);
        int minus = (int) ((percentage / 100.0) * 130);

        int p=binding.progressBar.getWidth();

        // Convert 40dp to pixels
        float pixelsToSubtract = getResources().getDisplayMetrics().density * minus;

        // Subtract 40dp from the calculated translationX
        translationX -= pixelsToSubtract;

        Log.i("heightDiff", "heightDiff: " + screenWidth);
        Log.i("heightDiff", "pixelsToSubtract: " + pixelsToSubtract);
        Log.i("heightDiff", "translationX: " + translationX);
        Log.i("heightDiff", "per: " + percentage);
        Log.i("heightDiff", "minus: " + minus);
        Log.i("heightDiff", "p: " + p);

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animation.setDuration(2000);
        animation.start();
    }*/


    public void editTxt(EditText editText, TextView txtview){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtview.setVisibility(View.INVISIBLE);
                tempDataModel.setT_DESC(s.toString().trim());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*public void getSharedPrefValues(){
        vehicle=MyPrefs.getInstance(getActivity()).getString(CommonConstants.vehicle);
        description=MyPrefs.getInstance(getActivity()).getString(CommonConstants.desc);

        totalKmINT=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.totalKm);
        totalKm=String.valueOf(totalKmINT);

        perKmINT=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.perKm);
        perKm=String.valueOf(perKmINT);

        tollChargesINT=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.tollCharges);
        tollCharges=String.valueOf(tollChargesINT);

        String jsonDate = MyPrefs.getInstance(getActivity()).getString(CommonConstants.date);
        if (!jsonDate.isEmpty()) {
            // Deserialize only if the string is not empty
            Date_Model dateModel = gson.fromJson(jsonDate, Date_Model.class);
            Log.i("dateModel_dateModel", "dateModel: " + dateModel);

            date=dateModel.getDate();
            month=dateModel.getMonth();
            year=dateModel.getYear();
            dateObj=dateModel.getDateObj();
            monthTxt=dateModel.getMonthTxt();
            day=dateModel.getDay();
            binding.date.setText(dateModel.getDateTxt());

        }

        Log.i("dateModel_dateModel", "vehicle: "+vehicle);
        Log.i("dateModel_dateModel", "description: "+description);
        Log.i("dateModel_dateModel", "totalKm: "+totalKm);
        Log.i("dateModel_dateModel", "totalKmINT: "+totalKmINT);
        Log.i("dateModel_dateModel", "perKm: "+perKm);
        Log.i("dateModel_dateModel", "perKmINT: "+perKmINT);
        Log.i("dateModel_dateModel", "tollCharges: "+tollCharges);
        Log.i("dateModel_dateModel", "tollChargesINT: "+tollChargesINT);
        Log.i("dateModel_dateModel", "picloc: "+picDropModelsList);

        if(!vehicle.equalsIgnoreCase("")){
            int pos=vehicleList.indexOf(vehicle);
            binding.vehicleSpinner.setSelection(pos);
        }

        if(!totalKm.equalsIgnoreCase("0") && !totalKm.equalsIgnoreCase("")){
            binding.totalKm.setText(totalKm);
        }
        if(!description.equalsIgnoreCase("")){
            binding.desc.setText(description);
        }
        if(!perKm.equalsIgnoreCase("0") && !perKm.equalsIgnoreCase("")){
            binding.perKm.setText(perKm);
        }
        if(!tollCharges.equalsIgnoreCase("0") && !tollCharges.equalsIgnoreCase("")){
            binding.tollCharges.setText(tollCharges);
        }

        totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
        binding.totalFar.setText("₹"+String.valueOf(totalFarINT));

    }*/

    public void getSharedPrefValues(){

        calculation();

        tempDataModel=dBhelper.getTempData();

        t_ColumnId= tempDataModel.getT_COLUMN_ID();

        vehicle=tempDataModel.getT_VEHICLE();
        description=tempDataModel.getT_DESC();

        progressVal=tempDataModel.getT_PROGRESS_VALUE();
        int oldvalue=ValueChangeListener.myValue;
        increaseProgressSmoothly(oldvalue,progressVal,binding.progressBar,2000);
        increaseSeekBarSmoothly(oldvalue,progressVal,binding.seekBar,2000);
        //ProgressBar_increase.Increase(0,progressVal,binding.progressBar,10);

        binding.percentage.setText(progressVal+"%");

        totalKmINT=tempDataModel.getT_TOTAL_KM();
        totalKm=String.valueOf(totalKmINT);

        perKmINT= tempDataModel.getT_PER_KM();
        perKm=String.valueOf(perKmINT);

        tollChargesINT= tempDataModel.getT_TOLL_CHARGES();
        tollCharges=String.valueOf(tollChargesINT);


        if(!tempDataModel.getT_PIC_DROP_MODEL().equalsIgnoreCase("")){
            json = tempDataModel.getT_PIC_DROP_MODEL();
            pickupLoc=json;
            dropLoc=json;
            Type listType = new TypeToken<List<PicDrop_Model>>(){}.getType();
            picDropModelsList = gson.fromJson(json, listType);
            locVal=picDropModelsList.size();
        }else{
            picDropModelsList.clear();
            locVal=1;
            picDropModelsList.add(new PicDrop_Model(String.valueOf(locVal),"",""));
        }

        String jsonDate = tempDataModel.getT_DATE_MODEL();
        if (!jsonDate.isEmpty()) {
            // Deserialize only if the string is not empty
            Date_Model dateModel = gson.fromJson(jsonDate, Date_Model.class);
            Log.i("dateModel_dateModel", "dateModel: " + dateModel);

            date=dateModel.getDate();
            month=dateModel.getMonth();
            year=dateModel.getYear();
            dateObj=dateModel.getDateObj();
            monthTxt=dateModel.getMonthTxt();
            day=dateModel.getDay();
            binding.date.setText(dateModel.getDateTxt());

        }

        Log.i("dateModel_dateModel", "vehicle: "+vehicle);
        Log.i("dateModel_dateModel", "description: "+description);
        Log.i("dateModel_dateModel", "totalKm: "+totalKm);
        Log.i("dateModel_dateModel", "totalKmINT: "+totalKmINT);
        Log.i("dateModel_dateModel", "perKm: "+perKm);
        Log.i("dateModel_dateModel", "perKmINT: "+perKmINT);
        Log.i("dateModel_dateModel", "tollCharges: "+tollCharges);
        Log.i("dateModel_dateModel", "tollChargesINT: "+tollChargesINT);
        Log.i("dateModel_dateModel", "picloc: "+picDropModelsList);

        if(!vehicle.equalsIgnoreCase("")){
            int pos=vehicleList.indexOf(vehicle);
            binding.vehicleSpinner.setSelection(pos);
        }

        if(!totalKm.equalsIgnoreCase("0") && !totalKm.equalsIgnoreCase("")){
            binding.totalKm.setText(totalKm);
        }
        if(!description.equalsIgnoreCase("")){
            binding.desc.setText(description);
        }
        if(!perKm.equalsIgnoreCase("0") && !perKm.equalsIgnoreCase("")){
            binding.perKm.setText(perKm);
        }
        if(!tollCharges.equalsIgnoreCase("0") && !tollCharges.equalsIgnoreCase("")){
            binding.tollCharges.setText(tollCharges);
        }

        totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
        binding.totalFar.setText("₹"+totalFarINT);

    }

   public void clearDB(){
        dBhelper.deleteTempData(tempDataModel.getT_COLUMN_ID());
        MyPrefs.getInstance(getActivity()).putBoolean(CommonConstants.isTableCreated,false);
        MyPrefs.getInstance(getActivity()).putInt(CommonConstants.locVal,0);
   }

   public void updateTempData(){
       /*Temp_Data_Model tempDataModel=new Temp_Data_Model(t_ColumnId,vehicle,true,"",true,"",true,"",0,true,0,true,0,true,0,true,0);*/
        dBhelper.updateAllTempData(tempDataModel,t_ColumnId);
   }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateTempData();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateTempData();
    }

    int total = 100;
    int parts = 6;
    int[] valArray = new int[parts];
    public void calculation(){

        int quotient = total / parts;
        int remainder = total % parts;

        System.out.println("Parts: " + quotient);
        System.out.println("Parts: " + remainder);

        // Distribute the parts

        for (int i = 0; i < parts; i++) {
            valArray[i] = quotient;
            if (remainder > 0) {
                valArray[i]++;
                remainder--;
            }
        }

        // Display the parts
        System.out.println("Parts: " + Arrays.toString(valArray));

    }

}