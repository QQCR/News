package com.example.news.ui.explore;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.models.Source;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.MyViewHolder> {

    private List<Source> sources;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ExploreAdapter(List<Source> sources, Context context){
        this.sources = sources;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.source_item, parent, false);
        return new ExploreAdapter.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Source model = sources.get(position);



        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());
        holder.category.setText(model.getCategory());
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public interface SourceAdapterListener {
        void onSourceButtonClicked(Source source);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name, category,desc;
        ImageView imageView;
        Button button;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.source_name);
            category = itemView.findViewById(R.id.source_category);
            desc = itemView.findViewById(R.id.source_desc);
            imageView = itemView.findViewById(R.id.source_image);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
