package com.example.taxibill.Adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxibill.DB.PicDrop_Model;
import com.example.taxibill.Utils.OnItemViewClickListener;
import com.example.taxibill.databinding.RecyclerPicdropBinding;
import com.example.taxibill.databinding.RecyclerPicdropBinding;

import org.json.JSONException;

import java.util.List;

public class Pic_Drop_Adapter extends RecyclerView.Adapter<Pic_Drop_Adapter.AppViewHolder>{

    Activity activity;
    List<PicDrop_Model> list;
    OnItemViewClickListener onItemViewClickListener;
    public static int index=0;

    public Pic_Drop_Adapter(Activity activity, List<PicDrop_Model> list, OnItemViewClickListener onItemViewClickListener) {
        this.activity = activity;
        this.list = list;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerPicdropBinding binding = RecyclerPicdropBinding.inflate(inflater, parent, false);
        return new Pic_Drop_Adapter.AppViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        RecyclerPicdropBinding binding = holder.binding;

        PicDrop_Model picDropModel=list.get(position);

        binding.num.setText(picDropModel.getNum());
        binding.picLoc.setText(picDropModel.getPicLoc());
        binding.dropLoc.setText(picDropModel.getDropLoc());

        binding.picLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                picDropModel.setPicLoc(s.toString());
                try {
                    onItemViewClickListener.onClick(holder.itemView, position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.dropLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                picDropModel.setDropLoc(s.toString());
                try {
                    onItemViewClickListener.onClick(holder.itemView, position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AppViewHolder extends RecyclerView.ViewHolder {
        RecyclerPicdropBinding binding;
        public AppViewHolder(@NonNull View itemView, RecyclerPicdropBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
