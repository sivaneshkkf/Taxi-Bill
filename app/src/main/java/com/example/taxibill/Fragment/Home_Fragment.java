package com.example.taxibill.Fragment;

import static com.example.taxibill.DB.Date_Formats.getCurrentDateFormat;
import static com.example.taxibill.DB.Date_Formats.monthMM;
import static com.example.taxibill.DB.Date_Formats.yearyyyy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.taxibill.Activity.AddCar_Activity;
import com.example.taxibill.Activity.Bill_Generate_Activity;
import com.example.taxibill.Adapter.Car_Adapter;
import com.example.taxibill.Adapter.Trip_Details_Adapter;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.R;
import com.example.taxibill.Utils.CommonFunctions;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.FragmentAddBinding;
import com.example.taxibill.databinding.FragmentHomeBinding;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Home_Fragment extends Fragment {
    FragmentHomeBinding binding;
    DBhelper dBhelper;
    SQLiteDatabase db;
    Car_Adapter carAdapter;
    List<Vehicle_Model> list=new ArrayList<>();
    Trip_Details_Adapter tripDetailsAdapter;
    String YEAR="",MONTH="";
    List<DB_Model> tripDetailsList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        View v=binding.getRoot();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dBhelper=new DBhelper(getActivity());
        db=dBhelper.getReadableDatabase();

        YEAR=getCurrentDateFormat(yearyyyy);
        MONTH=getCurrentDateFormat(monthMM);

        list=dBhelper.getEveryVehicle();

        tripDetailsList=dBhelper.getEveryOne(YEAR,MONTH);
        Log.i("tripDetailsList", "tripDetailsList: "+tripDetailsList.size());
        Log.i("tripDetailsList", "YEAR: "+YEAR);
        Log.i("tripDetailsList", "MONTH: "+MONTH);


        carAdapter=new Car_Adapter(getActivity(), list, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {

            }
        });

        binding.recyclerCar.setAdapter(carAdapter);


        binding.addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddCar_Activity.class);
                startActivity(intent);
            }
        });

        tripDetailsAdapter=new Trip_Details_Adapter(getActivity(), tripDetailsList, new OnItemViewClickListener() {
            @Override
            public void onClick(View v, int i) throws JSONException {
                DB_Model db_model=tripDetailsList.get(i);
                int id= db_model.getCOLUMN_ID();
                if(v.getId()==R.id.lin){

                    Bundle bundle= CommonFunctions.getBundle(getActivity());
                    bundle.putInt("id",id);
                    bundle.putInt("pos",i);
                    Intent intent=new Intent(getActivity(), Bill_Generate_Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        binding.recyclerTripDetails.setAdapter(tripDetailsAdapter);


    }
}