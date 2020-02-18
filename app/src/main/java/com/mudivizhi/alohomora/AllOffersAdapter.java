package com.mudivizhi.alohomora;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllOffersAdapter extends RecyclerView.Adapter<AllOffersAdapter.Viewholder>{

    private List<AllOffersModel> allOffersModelList;

    public AllOffersAdapter(List<AllOffersModel> allOffersModelList) {
        this.allOffersModelList = allOffersModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alloffers_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.setData(allOffersModelList.get(position).getUrl(),allOffersModelList.get(position).getOfferPercent(),allOffersModelList.get(position).getDesc(),position);
    }

    @Override
    public int getItemCount() {
        return allOffersModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView offerPerc,offerDesc;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = (CircleImageView) itemView.findViewById(R.id.iv_offersLogo);
            offerPerc = (TextView) itemView.findViewById(R.id.tv_offerPercent);
            offerDesc = (TextView) itemView.findViewById(R.id.tv_offerDesc);
        }

        private void setData(String url, final String offerPercent, final String desc, final int position){
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.offerPerc.setText(offerPercent);
            this.offerDesc.setText(desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    Intent intent = new Intent(itemView.getContext(),SetsActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("position",position);
                    itemView.getContext().startActivity(intent);

                     */
                }
            });

        }
    }


}
