package com.example.news.ui.collection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.news.R;
import com.example.news.database.SaveDao;
import com.example.news.models.Save;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder>{

    private List<Save> saves;
    private Context context;
    private CollectionAdapter.OnItemClickListener onItemClickListener;
    private CollectionAdapter.OnButtonClickListener onButtonClickListener;
    private SaveDao saveDao;

    public CollectionAdapter(List<Save> saves, Context context){
        this.saves = saves;
        this.context = context;
        saveDao = new SaveDao(context);
    }
    @NonNull
    @Override
    public CollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_news, parent, false);
        return new CollectionAdapter.MyViewHolder(view, onItemClickListener,onButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.MyViewHolder holders, int position) {
        final CollectionAdapter.MyViewHolder holder = holders;
        Save model = saves.get(position);

        holder.name.setText(model.getTitle());
        holder.source.setText(model.getSource());
        Glide.with(context)
                .load(model.getUrlToImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return saves.size();
    }

    public interface OnButtonClickListener {
        void onButtonClicked(View view, int position);
    }

    public void setOnButtonClickListener(CollectionAdapter.OnButtonClickListener onButtonClickListener){
        this.onButtonClickListener = onButtonClickListener;
    }

    public void setOnItemClickListener(CollectionAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, source;
        ImageView imageView;
        Button button;
        CollectionAdapter.OnItemClickListener onItemClickListener;
        CollectionAdapter.OnButtonClickListener onButtonClickListener;

        public MyViewHolder(View itemView, CollectionAdapter.OnItemClickListener onItemClickListener, final CollectionAdapter.OnButtonClickListener onButtonClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.news_title);
            source = itemView.findViewById(R.id.news_source);
            imageView = itemView.findViewById(R.id.news_image);
            button = itemView.findViewById(R.id.btn_delete);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(saveDao.deleteSave(saves.get(getAdapterPosition()).getId())){
//                        Toast.makeText(context, "delete successful!", Toast.LENGTH_SHORT).show();
//                    }
//                    notifyDataSetChanged();
//                }
//            });
            this.onItemClickListener = onItemClickListener;
            this.onButtonClickListener = onButtonClickListener;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     onButtonClickListener.onButtonClicked(view,getAdapterPosition());
                }
            });



        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
