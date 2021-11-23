package com.example.projectpb;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectpb.data.Result;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MasakanAdapter extends RecyclerView.Adapter<MasakanAdapter.ViewHolder> {
    private final ArrayList<Result>mData=new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Result> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MasakanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MasakanAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(mData.get(holder.getBindingAdapterPosition())));
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitleText;
        ImageView mMasakanImage;
        TextView mDurasi;
        TextView mKesulitan;
        String mKey;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mMasakanImage = itemView.findViewById(R.id.gbr_makanan);
            mDurasi = itemView.findViewById(R.id.durasi);
            mKesulitan=itemView.findViewById(R.id.kesulitan);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v -> getBindingAdapterPosition());
        }

        void bind(Result result){
            Glide.with(itemView.getContext())
                    .load(result.getThumb())
                    .apply(new RequestOptions().override(1920, 1920))
                    .into(mMasakanImage);
            mTitleText.setText(result.getTitle());
            String durasi="Durasi : "+result.getTimes();
            String kesulitan="Kesulitan : "+result.getDificulty();
            mDurasi.setText(durasi);
            mKesulitan.setText(kesulitan);
            mKey=result.getKey();
        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(Result data);
    }
}
