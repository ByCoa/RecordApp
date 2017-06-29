package com.example.ecoa.recordapp.ViewsAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecoa.recordapp.Models.RecordObject;
import com.example.ecoa.recordapp.R;

import java.util.List;

/**
 * Created by ecoa on 6/26/2017.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<RecordObject> records;

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imgv_record;
        public TextView txtv_title;
        public TextView txtv_date;
        public TextView txtv_duration;

        public RecordViewHolder(View v) {
            super(v);
            imgv_record = (ImageView) v.findViewById(R.id.imgv_record);
            txtv_title = (TextView) v.findViewById(R.id.txtv_record_name);
            txtv_date = (TextView) v.findViewById(R.id.txtv_record_date);
            txtv_duration = (TextView) v.findViewById(R.id.txtv_record_duration);
        }
    }

    public RecordAdapter(List<RecordObject> items) {
        this.records = items;
    }

    public RecordObject getChild (int position){
        return records.get(position);
    }
    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_cardview, viewGroup, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder viewHolder, int i) {
        viewHolder.txtv_title.setText(records.get(i).getTitle());
        viewHolder.txtv_date.setText(records.get(i).getDate());
        viewHolder.txtv_duration.setText(records.get(i).getDuration());
    }

}
