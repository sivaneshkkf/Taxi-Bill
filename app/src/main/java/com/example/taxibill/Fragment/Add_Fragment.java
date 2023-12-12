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

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taxibill.Adapter.Pic_Drop_Loc_Adapter;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Date_Model;
import com.example.taxibill.DB.PicDrop_Model;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.MainActivity;
import com.example.taxibill.R;
import com.example.taxibill.Utils.CommonConstants;
import com.example.taxibill.Utils.MyPrefs;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.FragmentAddBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
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

        if(!MyPrefs.getInstance(getActivity()).getString(CommonConstants.picDropLoc).equalsIgnoreCase("")){
            json = MyPrefs.getInstance(getActivity()).getString(CommonConstants.picDropLoc);
            Type listType = new TypeToken<List<PicDrop_Model>>(){}.getType();
            picDropModelsList = gson.fromJson(json, listType);
            locVal=picDropModelsList.size();
        }else{
            picDropModelsList.add(new PicDrop_Model(String.valueOf(locVal),"",""));
        }

        getSharedPrefValues();
        /*if(MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal)>0){
           //locVal=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal);
        }*/


        //Log.i("list_list", "list: "+picDropModelsList.get(0).getPicLoc());

        binding.vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle=vehicleList.get(position);

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
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.totalKm,totalKmINT);
                }else{
                    val=0;
                    totalKmINT=val;
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.totalKm,totalKmINT);
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
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.perKm,perKmINT);
                }else{
                    val=0;
                    perKmINT=val;
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.perKm,perKmINT);
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
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.tollCharges,tollChargesINT);

                }else{
                    val=0;
                    tollChargesINT=val;
                    MyPrefs.getInstance(getActivity()).putInt(CommonConstants.tollCharges,tollChargesINT);
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

                        MyPrefs.getInstance(getActivity()).putString(CommonConstants.date, jsonDate);
                        progressVal+=100/6;
                        MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);

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

                MyPrefs.getInstance(getActivity()).putString(CommonConstants.picDropLoc, json);

                progressVal+=100/6;
                MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);

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

                if(v.getId()==R.id.picLoc){
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
                }
            }
        });
        binding.recycelrPicDropLoc.setAdapter(picDropLocAdapter);

        editTxt(binding.desc,CommonConstants.desc,binding.descError);

        if(vehicleList.size()>0){
            binding.vehicleSpinner.setSelection(0);
            vehicle=vehicleList.get(0);
            MyPrefs.getInstance(getActivity()).putString(CommonConstants.vehicle,vehicle);
            progressVal+=100/6;
            MyPrefs.getInstance(getActivity()).putInt(CommonConstants.progressVal,progressVal);
        }

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalKm=binding.totalKm.getText().toString().trim();
                perKm=binding.perKm.getText().toString().trim();
                tollCharges=binding.tollCharges.getText().toString().trim();
                description=binding.desc.getText().toString().trim();

                pickupLoc=json;
                dropLoc=json;
               if(date.equalsIgnoreCase("")){
                    binding.dateError.setText("Please Select A Date");
                    binding.dateError.setVisibility(View.VISIBLE);
                }/*else if(pickupLoc.equalsIgnoreCase("")){
                   binding.pickupDropLocError.setText("Please Enter Pickup Location");
                   binding.pickupDropLocError.setVisibility(View.VISIBLE);
                }*/else if(dropLoc.equalsIgnoreCase("")){
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

                   clearSharedPref();
                }

            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                clearSharedPref();
            }
        });


        Glide.with(getActivity()).asGif().load(R.drawable.cargif).into(binding.gif);


        binding.totalKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(totalKmINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.perKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(perKmINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.tollCharges.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(tollChargesINT!=0){
                        progressVal+=100/6;
                    }else{
                        progressVal-=100/6;
                    }
                    Toast.makeText(getActivity(), ""+progressVal, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void editTxt(EditText editText, String constant, TextView txtview){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtview.setVisibility(View.INVISIBLE);
                MyPrefs.getInstance(getActivity()).putString(constant,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    int val=0;
    public int editTxtINT(EditText editText, String constant, TextView txtview){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtview.setVisibility(View.INVISIBLE);
                MyPrefs.getInstance(getActivity()).putString(constant,s.toString());
                val=Integer.parseInt(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                totalFarINT=(totalKmINT*perKmINT)+tollChargesINT;
                binding.totalFar.setText(String.valueOf(totalFarINT));
            }
        });
        return val;
    }

    public void getSharedPrefValues(){
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

    }

   public void clearSharedPref(){
      MyPrefs.getInstance(getActivity()).putString(CommonConstants.vehicle,"");
      MyPrefs.getInstance(getActivity()).putString(CommonConstants.date,"");
      MyPrefs.getInstance(getActivity()).putString(CommonConstants.picDropLoc,"");
      MyPrefs.getInstance(getActivity()).putString(CommonConstants.dropLoc,"");
      MyPrefs.getInstance(getActivity()).putString(CommonConstants.desc,"");
      MyPrefs.getInstance(getActivity()).putInt(CommonConstants.totalKm,0);
      MyPrefs.getInstance(getActivity()).putInt(CommonConstants.perKm,0);
      MyPrefs.getInstance(getActivity()).putInt(CommonConstants.tollCharges,0);
   }

}