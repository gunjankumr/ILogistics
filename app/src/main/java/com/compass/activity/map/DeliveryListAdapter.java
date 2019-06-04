package com.compass.activity.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.compass.ilogistics.R;
import com.compass.model.DeliveryData;

import java.util.List;

public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.MyViewHolder> {

    private List<DeliveryData> deliveryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDeliveryOrder;
        public TextView textViewShopName;
        public TextView textViewStartLocation;
        public TextView textViewDropLocation;
        public TextView textViewDistance;
        public TextView textViewArrivesBefore;

        public MyViewHolder(View view) {
            super(view);
            textViewDeliveryOrder = view.findViewById(R.id.text_view_item_order);
            textViewShopName = view.findViewById(R.id.text_view_shop_name);
            textViewStartLocation = view.findViewById(R.id.text_view_start_location);
            textViewDropLocation = view.findViewById(R.id.text_view_drop_location);
            textViewDistance = view.findViewById(R.id.text_view_distance);
            textViewArrivesBefore = view.findViewById(R.id.text_view_arrival_time);
        }
    }

    public DeliveryListAdapter(List<DeliveryData> deliveryList) {
        this.deliveryList = deliveryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DeliveryData deliveryData = deliveryList.get(position);

        holder.textViewDeliveryOrder.setText(String.valueOf(position + 1));
        holder.textViewShopName.setText(deliveryData.getShopLocation());
        holder.textViewStartLocation.setText(deliveryData.getStartLocation());
        holder.textViewDropLocation.setText(deliveryData.getDropLocation());
        holder.textViewDistance.setText(deliveryData.getDistance());
        holder.textViewArrivesBefore.setText(deliveryData.getArrivesBefore());
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }
}
