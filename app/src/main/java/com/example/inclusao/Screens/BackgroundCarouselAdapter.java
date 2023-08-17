package com.example.inclusao.Screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.example.inclusao.R;

import androidx.recyclerview.widget.RecyclerView;

public class BackgroundCarouselAdapter extends RecyclerView.Adapter<BackgroundCarouselAdapter.ViewHolder> {

    private int[] backgroundImages;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }



    public int getNextIndex(int currentIndex) {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= backgroundImages.length) {
            nextIndex = 0; // Volta para o primeiro item
        }
        return nextIndex;
    }




    public BackgroundCarouselAdapter(int[] backgroundImages) {
        this.backgroundImages = backgroundImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carousel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int backgroundImage = backgroundImages[position];
        holder.imageView.setImageResource(backgroundImage);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return backgroundImages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.backgroundImage);
        }
    }
}
