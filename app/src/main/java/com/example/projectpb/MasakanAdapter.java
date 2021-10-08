package com.example.projectpb;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MasakanAdapter extends RecyclerView.Adapter<MasakanAdapter.ViewHolder> {
    private ArrayList<Masakan> mMasakanData;
    private Context mContext;

    MasakanAdapter(Context context, ArrayList<Masakan> masakanData){
        this.mMasakanData=masakanData;
        this.mContext=context;
    }

    @Override
    public MasakanAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MasakanAdapter.ViewHolder holder, int position) {
        Masakan currentMasakan=mMasakanData.get(position);
        holder.bindTo(currentMasakan);
    }

    @Override
    public int getItemCount(){
        return mMasakanData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mMasakanImage;


        ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mMasakanImage = itemView.findViewById(R.id.gbr_makanan);
            itemView.setOnClickListener(this);
        }

        void bindTo(Masakan currentMasakan){
            // Populate the textviews with data.
            mTitleText.setText(currentMasakan.getJudul());
            mInfoText.setText(currentMasakan.getInfo());
            Glide.with(mContext).load(currentMasakan.getImageResource()).into(mMasakanImage);
        }


        @Override
        public void onClick(View view) {
            Masakan currentMasakan = mMasakanData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra("Judul", currentMasakan.getJudul());
            detailIntent.putExtra("image_resource",
                    currentMasakan.getImageResource());
            mContext.startActivity(detailIntent);
        }
    }

}
