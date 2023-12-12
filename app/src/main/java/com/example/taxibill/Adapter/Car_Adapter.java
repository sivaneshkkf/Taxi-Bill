package com.example.taxibill.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.taxibill.DB.Vehicle_Model;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.RecyclerAddCarBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Car_Adapter extends RecyclerView.Adapter<Car_Adapter.AppViewHolder>{

    Activity activity;
    List<Vehicle_Model> list;
    OnItemViewClickListener onItemViewClickListener;
    public static int index=0;

    public Car_Adapter(Activity activity, List<Vehicle_Model> list, OnItemViewClickListener onItemViewClickListener) {
        this.activity = activity;
        this.list = list;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerAddCarBinding binding = RecyclerAddCarBinding.inflate(inflater, parent, false);
        return new Car_Adapter.AppViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        RecyclerAddCarBinding binding = holder.binding;

        Vehicle_Model vehicleModel=list.get(position);


            String img=vehicleModel.getVEHICLE_IMG();
            Glide.with(activity.getApplicationContext()).load(img).into(binding.img);
            binding.txt.setText(vehicleModel.getV_MODEL());


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
        RecyclerAddCarBinding binding;
        public AppViewHolder(@NonNull View itemView, RecyclerAddCarBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
