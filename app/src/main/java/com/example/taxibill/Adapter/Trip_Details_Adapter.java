package com.example.taxibill.Adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taxibill.DB.DB_Model;
import com.example.taxibill.DB.DBhelper;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.RecyclerTripDetailsBinding;
import com.example.taxibill.databinding.RecyclerTripDetailsBinding;

import org.json.JSONException;

import java.util.List;

public class Trip_Details_Adapter extends RecyclerView.Adapter<Trip_Details_Adapter.AppViewHolder>{

    Activity activity;
    List<DB_Model> list;
    OnItemViewClickListener onItemViewClickListener;
    DBhelper dBhelper;
    SQLiteDatabase db;
    public static int index=0;

    public Trip_Details_Adapter(Activity activity, List<DB_Model> list, OnItemViewClickListener onItemViewClickListener) {
        this.activity = activity;
        this.list = list;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerTripDetailsBinding binding = RecyclerTripDetailsBinding.inflate(inflater, parent, false);
        return new Trip_Details_Adapter.AppViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        RecyclerTripDetailsBinding binding = holder.binding;

        DB_Model db_model=list.get(position);

            binding.date.setText(db_model.getDATE());
            binding.month.setText(db_model.getMONTH_TXT());
            binding.vehicle.setText(db_model.getVEHICLE());

            dBhelper=new DBhelper(activity);
            db=dBhelper.getReadableDatabase();
            String vNumber=db_model.getVEHICLE();
            String vModel=dBhelper.getVehicleModel(vNumber);
            binding.vModel.setText(vModel);
            //binding.picDropLoc.setText(db_model.getPICKUP_LOC()+" to "+db_model.getDROP_LOC());
            binding.totalFar.setText("₹ "+db_model.getTOTAL_FAR());
            binding.desc.setText(db_model.getDESC());


        // Set an OnClickListener to handle item clicks
        binding.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    onItemViewClickListener.onClick(v, position);
                    index=position;
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /*if (index==position){
            binding.lin.setBackgroundDrawable(activity.getApplicationContext().getResources().getDrawable(R.drawable.button_curve_blue_background));
            binding.txt.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.white));
        }else{
            binding.lin.setBackgroundDrawable(activity.getApplicationContext().getResources().getDrawable(R.drawable.button_curve_white_background));
            binding.txt.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.black));
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AppViewHolder extends RecyclerView.ViewHolder {
        RecyclerTripDetailsBinding binding;
        public AppViewHolder(@NonNull View itemView, RecyclerTripDetailsBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
