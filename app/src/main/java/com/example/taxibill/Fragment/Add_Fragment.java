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

import com.example.taxibill.Adapter.Pic_Drop_Loc_Adapter;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
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
    String vehicle="",date="",day="",month="",monthTxt,year="",dateObj="",pickupLoc="",dropLoc="",totalKm="",perKm="",tollCharges="",totalFar="";
    int totalKmINT=0,perKmINT=0,tollChargesINT=0,totalFarINT=0;
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

        if(MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal)>0){
           //locVal=MyPrefs.getInstance(getActivity()).getInt(CommonConstants.locVal);
        }


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
                }else{
                    val=0;
                    totalKmINT=val;
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
                }else{
                    val=0;
                    perKmINT=val;
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
                }else{
                    val=0;
                    tollChargesINT=val;
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
            }
        });
        binding.recycelrPicDropLoc.setAdapter(picDropLocAdapter);


        if(vehicleList.size()>0){
            binding.vehicleSpinner.setSelection(0);
            vehicle=vehicleList.get(0);
        }

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalKm=binding.totalKm.getText().toString().trim();
                perKm=binding.perKm.getText().toString().trim();
                tollCharges=binding.tollCharges.getText().toString().trim();
                pickupLoc=json;
                dropLoc=json;
               if(date.equalsIgnoreCase("")){
                    binding.dateError.setText("Please Select A Date");
                    binding.dateError.setVisibility(View.VISIBLE);
                }else if(pickupLoc.equalsIgnoreCase("")){
                   binding.pickupDropLocError.setText("Please Enter Pickup Location");
                   binding.pickupDropLocError.setVisibility(View.VISIBLE);
                }else if(dropLoc.equalsIgnoreCase("")){
                   binding.pickupDropLocError.setText("Please Enter Drop Location");
                   binding.pickupDropLocError.setVisibility(View.VISIBLE);
                }else if(totalKm.equalsIgnoreCase("")){
                   binding.totalKmError.setText("Please Enter Total KM");
                   binding.totalKmError.setVisibility(View.VISIBLE);
                }else{
                   db_model=new DB_Model(0, vehicle, date, day, month, monthTxt, year, dateObj, pickupLoc, dropLoc,totalKmINT, perKmINT, tollChargesINT, totalFarINT);
                   dBhelper.InsetData(db_model);
                   Intent intent=new Intent(getActivity(), MainActivity.class);
                   startActivity(intent);
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

}