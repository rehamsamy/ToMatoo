//package com.example.khaled_sa2r.tomatoo.Main.Adapters;
//
//import android.media.Image;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.example.khaled_sa2r.tomatoo.R;
//
//import java.util.ArrayList;
//
//public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{
//
//    ArrayList<Hadeth> items;
//    AdapterView.OnItemClickListener onItemClickListener;
//
//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public CategoriesAdapter(ArrayList<Hadeth> items)
//    {
//        this.items=items;
//    }
//@NonNull
//@Override
//public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//    View view = LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.activity_main_design,parent,false);
//
//    return new ViewHolder(view);
//}
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        final Hadeth hadeth= items.get(position);
//        holder.name.setText(hadeth.getTitle());
//        if(onItemClickListener!=null)
//            holder.parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemClick(position,hadeth);
//                }
//            });
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//        ImageView image;
//        TextView name;
//        RatingBar rating;
//        View parent;
//        ViewHolder(View v){
//            super(v);
//            name= v.findViewById(R.id.name);
//            rating= v.findViewById(R.id.rating);
//            image= v.findViewById(R.id.image);
//
//            parent = v;
//        }
//
//    }
//
//    public interface OnItemClickListener{
//        void onItemClick(int pos,Hadeth hadeth);
//    }
//
//}
