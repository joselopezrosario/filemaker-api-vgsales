package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoGamesListAdapter extends RecyclerView.Adapter<VideoGamesListAdapter.ViewHolder> {
    private Context mContext;
    private JSONArray mDataset;

    VideoGamesListAdapter(Context context, JSONArray myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewRank;
        TextView TextViewName;
        TextView TextViewPlatform;
        TextView TextViewYear;
        TextView TextViewGenre;
        TextView TextViewPublisher;
        //TextView TextViewGlobalSales;

        ViewHolder(View view) {
            super(view);
            TextViewRank = view.findViewById(R.id.vg_rank);
            TextViewName = view.findViewById(R.id.vg_name);
            TextViewPlatform = view.findViewById(R.id.vg_platform);
            TextViewYear = view.findViewById(R.id.vg_year);
            TextViewGenre = view.findViewById(R.id.vg_genre);
            TextViewPublisher = view.findViewById(R.id.vg_publisher);
            //TextViewGlobalSales = view.findViewById(R.id.vg_global_sales);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.content_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            JSONObject record = mDataset.getJSONObject(position).getJSONObject("fieldData");
            holder.TextViewRank.setText(record.getString(DataAPI.FIELD_RANK));
            holder.TextViewName.setText(record.getString(DataAPI.FIELD_NAME));
            holder.TextViewPlatform.setText(record.getString(DataAPI.FIELD_PLATFORM));
            holder.TextViewYear.setText(record.getString(DataAPI.FIELD_YEAR));
            holder.TextViewGenre.setText(record.getString(DataAPI.FIELD_GENRE));
            holder.TextViewPublisher.setText(record.getString(DataAPI.FIELD_PUBLISHER));
            //holder.TextViewGlobalSales.setText(record.getString(DataAPI.FIELD_GLOBAL_SALES));
        }catch(JSONException e){
            System.out.println("Parsing error: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}