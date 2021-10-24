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

public class MasakanAdapter extends RecyclerView.Adapter<MasakanAdapter.ViewHolder> {
    private ArrayList<Result>mData=new ArrayList<>();
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitleText;
        ImageView mMasakanImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mMasakanImage = itemView.findViewById(R.id.gbr_makanan);
        }

        void bind(Result result){
            Glide.with(itemView.getContext())
                    .load(result.getThumb())
                    .apply(new RequestOptions().override(55, 55))
                    .into(mMasakanImage);
            mTitleText.setText(result.getTitle());
        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(Result data);
    }
}
