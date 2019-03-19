package com.example.rxjavatestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rxjavatestapp.network.responce.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<User> userList;
    private RecyclerInterface mRecyclerInterface;

    public RecyclerAdapter(Context mContext, RecyclerInterface recyclerInterface) {
        this.mContext = mContext;
        this.mRecyclerInterface = recyclerInterface;
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recyler_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final User user = userList.get(position);
        String url = user.getProfilePicUrl();
        if (url != null) {
            Glide.with(mContext)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.idIvProfilePicture);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_default_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.idIvProfilePicture);
        }
        holder.idTvUserName.setText(user.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerInterface.selectItem(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView idIvProfilePicture;
        private TextView idTvUserName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            idIvProfilePicture = itemView.findViewById(R.id.idIvProfilePicture);
            idTvUserName = itemView.findViewById(R.id.idTvUserName);
        }
    }

    public void updateUserList(ArrayList<User> userList) {
        this.userList.clear();
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void clearUserList() {
        this.userList.clear();
        notifyDataSetChanged();
    }
}
