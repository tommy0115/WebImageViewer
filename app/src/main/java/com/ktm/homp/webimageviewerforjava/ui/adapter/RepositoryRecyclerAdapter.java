package com.ktm.homp.webimageviewerforjava.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.ktm.homp.webimageviewerforjava.R;
import com.ktm.homp.webimageviewerforjava.model.Repository;
import com.ktm.homp.webimageviewerforjava.option.type.ViewSortType;
import com.ktm.homp.webimageviewerforjava.ui.activities.MainActivity;
import com.ktm.homp.webimageviewerforjava.ui.event.OnRepositoryItemClickListener;

import java.util.List;

public class RepositoryRecyclerAdapter extends RecyclerView.Adapter<RepositoryRecyclerAdapter.ImageVH>{

    List<Repository> repositories;
    RequestManager requestManager;
    OnRepositoryItemClickListener onRepositoryItemClickListener;
    int layoutSortType;

    public RepositoryRecyclerAdapter(List<Repository> repositories, RequestManager requestManager, int layoutSortType) {
        super();
        this.repositories = repositories;
        this.requestManager = requestManager;
        this.layoutSortType = layoutSortType;
    }

    public void setViewSortType(int layoutSortType){
        this.layoutSortType = layoutSortType;
    }

    @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if(viewType == ViewSortType.VIEW_SORT_COLUMN){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_item_column, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_item_list, parent, false);
        }

        return new ImageVH(v);
    }

    @Override
    public int getItemViewType(int position) {
        return layoutSortType;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, final int position) {
        final Repository repository = repositories.get(position);
        requestManager.load(repository.getThumbnailUrl()).into(holder.thumbImage);
        holder.textName.setText(repository.getName());
        holder.textDescription.setText(repository.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRepositoryItemClickListener != null){
                    onRepositoryItemClickListener.onItemClick(repository);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(repositories == null){
            return 0;
        }
        return repositories.size();
    }

    public void setOnRepositoryItemClickListener(OnRepositoryItemClickListener onRepositoryItemClickListener){
        this.onRepositoryItemClickListener = onRepositoryItemClickListener;
    }

    class ImageVH extends RecyclerView.ViewHolder{

        ImageView thumbImage;
        TextView textName;
        TextView textDescription;

        public ImageVH(View itemView) {
            super(itemView);

            thumbImage = itemView.findViewById(R.id.thumbImage);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
        }

    }

}
