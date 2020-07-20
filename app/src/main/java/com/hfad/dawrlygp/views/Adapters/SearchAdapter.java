package com.hfad.dawrlygp.views.Adapters;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.hfad.dawrlygp.databinding.SearchListBinding;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.views.PostDetails;
import com.hfad.dawrlygp.R;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.ViewHolder>
{
    private Context context;

    private List<Posts> items , filter ;

    private int categoryPosition = 0  , datePosition = 0;

    private String cityName = null ;

    private ArrayList<Posts> postsInfo = new ArrayList<>() ;

    public SearchAdapter(List items, Context context) {

        this.items = items;

        this.context = context;

        this.filter=items;

    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.search_list, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Posts posts = items.get(position);
        holder.binding.setPosts(posts);
        switch (items.get(position).getPostItemsId()){
            case 1:
                holder.binding.mainCategory.setText("Mobile");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName() +" "+items.get(position).getTextAgeModelName());

                break;
            case 2:
                holder.binding.mainCategory.setVisibility(View.GONE);
                holder.binding.nameSearchListEdit.setText("Keys");

                break;
            case 3:
                 holder.binding.mainCategory.setText("Sunglasses");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
            case 4:
                holder.binding.mainCategory.setText("Wallets");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
            case 5:
                holder.binding.mainCategory.setText("Watches");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
            case 6:
                holder.binding.mainCategory.setText("Bags");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
            case 7:
                holder.binding.mainCategory.setText("Books");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
            case 8:
                holder.binding.mainCategory.setText("Papers");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());

                break;
            case 9:
                holder.binding.mainCategory.setText("Human");
                holder.binding.nameSearchListEdit.setText(items.get(position).getTextBrandNameName());
                break;
        }


        if (items.get(position).getTypeId() == 1){
            holder.binding.timeSearchListEdit.setText("Found at "+items.get(position).getTime());
        }

        else {
            holder.binding.timeSearchListEdit.setText("Lost at "+items.get(position).getTime());
        }

        holder.binding.cardViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String PageId = "HomeActivity" ;

                Intent intent = new Intent(context, PostDetails.class);

                postsInfo.add(posts);

                intent.putParcelableArrayListExtra("LIST", postsInfo);

                intent.putExtra("PageId",PageId);

                context.startActivity(intent);

            }
        });
    }

    public void filter(String text) {
        if(text.isEmpty()){
            items = filter;
        } else{
            List<Posts> filteredList = new ArrayList<>();
            text = text.toLowerCase().trim();
            for(Posts item: filter){
                if(item.getTextBrandNameName().toLowerCase().contains(text)
                        || item.getLocation().toLowerCase().contains(text)
                        || item.getCity().toLowerCase().contains(text)
                ){
                    filteredList.add(item);
                }
            }
            items = filteredList ;
        }
        notifyDataSetChanged();
    }

    private void filterCategory(int position){

        categoryPosition = position;

        items = filter ;

        List<Posts> filteredList = new ArrayList<>();

        for(Posts item: items){

            if( item.getPostItemsId() == position)
            {
                filteredList.add(item);
            }
        }

        items = filteredList ;

        notifyDataSetChanged();

    }

    private void filterLocation(String city){

        items = filter ;

        List<Posts> filteredList = new ArrayList<>();

        for (Posts posts : items){
            if (posts.getCity().equals(city)){

                filteredList.add(posts);
            }
        }

        items = filteredList ;

        cityName = city ;

        notifyDataSetChanged();



    }

    private void filterDate(int datePosition1){

        Log.v("SearchAdapterPos","2 "+datePosition1);

        datePosition = datePosition1;

        items = filter ;

        List<Posts> filteredList = new ArrayList<>();

        for(Posts item: items){

            String[] arrOfStr =  item.getTime().split("/");

            if (Integer.valueOf(arrOfStr[0])==datePosition1){

                filteredList.add(item);
            }
        }

        items = filteredList ;

        notifyDataSetChanged();
    }

    public void filterByAll(){

        items = filter ;

        if (categoryPosition == 0 && cityName ==  null && datePosition == 0){
            return;
        }

        if (categoryPosition !=0 && cityName ==null && datePosition == 0){

            filterCategory(categoryPosition);
        }

        if (categoryPosition ==0 && cityName !=null && datePosition == 0){

            filterLocation(cityName);
        }

        if (categoryPosition ==0 && cityName ==null && datePosition != 0){

            filterDate(datePosition);
        }

        if (categoryPosition !=0 && cityName !=null){

            ArrayList<Posts> filterList = new ArrayList<>();
            for (Posts posts : items){
                if (posts.getCity().equals(cityName)
                        && posts.getPostItemsId() == categoryPosition){

                    filterList.add(posts);
                }
            }
            items = filterList ;

            notifyDataSetChanged();
        }

        if (categoryPosition !=0 && datePosition !=0){

            ArrayList<Posts> filterList = new ArrayList<>();
            for (Posts posts : items){

                String[] arrOfStr =  posts.getTime().split("/");

                int time = Integer.valueOf(arrOfStr[0]) ;

                if (time == datePosition && posts.getPostItemsId() == categoryPosition){

                    filterList.add(posts);
                }
            }
            items = filterList ;

            notifyDataSetChanged();
        }

        if (datePosition !=0 && cityName !=null){

            ArrayList<Posts> filterList = new ArrayList<>();
            for (Posts posts : items){
                String[] arrOfStr =  posts.getTime().split("/");

                int time = Integer.valueOf(arrOfStr[0]) ;
                if (posts.getCity().equals(cityName)
                        && time == datePosition){

                    filterList.add(posts);
                }
            }
            items = filterList ;

            notifyDataSetChanged();
        }

        if (categoryPosition !=0 &&datePosition !=0 && cityName !=null){

            ArrayList<Posts> filterList = new ArrayList<>();

            for (Posts posts : items){

                String[] arrOfStr =  posts.getTime().split("/");

                int time = Integer.valueOf(arrOfStr[0]) ;

                if (time==datePosition && posts.getCity().equals(cityName)

                        && posts.getPostItemsId() == categoryPosition){

                    filterList.add(posts);
                }
            }
            items = filterList ;

            notifyDataSetChanged();
        }

    }

    public void getCategoryPosition(int position){

        categoryPosition = position ;
    }

    public void getLocationCity(String city){

        cityName = city ;
    }

    public void getDatePosition(int position){

        datePosition = position ;
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SearchListBinding binding;

        public ViewHolder( SearchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



        }
    }



}
