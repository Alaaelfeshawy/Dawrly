package com.hfad.dawrlygp.views.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.PostListBinding;
import com.hfad.dawrlygp.model.PostItems;
import java.util.List;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private static List<PostItems> postItems;

    private OnItemClickListener onItemClickListener;

    private int selectedPosition = -1;

    public PostAdapter(List<PostItems> postItems) {
        this.postItems = postItems;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.post_list, parent, false);
        return new ViewHolder(binding);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        final PostItems item = postItems.get(position);

        holder.binding.setPosts(item);

        if(selectedPosition==position){
           holder.itemView.setBackgroundColor(Color.parseColor("#cc0000"));
       }else {
           holder.itemView.setBackgroundColor(Color.parseColor("#303030"));
       }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();
                onItemClickListener.onClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private PostListBinding binding ;

        public ViewHolder(@NonNull PostListBinding binding) {
            super(binding.getRoot());

            this.binding = binding ;
        }


    }
}


