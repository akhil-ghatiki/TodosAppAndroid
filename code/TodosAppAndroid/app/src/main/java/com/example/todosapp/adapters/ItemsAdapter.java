package com.example.todosapp.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.todosapp.R;
import com.example.todosapp.datamodel.Item;
import com.example.todosapp.properties.ApplicationProperties;
import java.io.IOException;
import java.util.List;

/**
 * Created by aghatiki on 12/27/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private Context context;
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {    // Viewholder for the recycler view
        public TextView m_title;
        public TextView m_done;
        public TextView m_pending;
        public ImageView m_itemImage;
        public RelativeLayout fg,bg;
        public MyViewHolder(View itemView) {
            super(itemView);
            m_title = itemView.findViewById(R.id.item_title);
            m_done = itemView.findViewById(R.id.item_status_done);
            m_pending = itemView.findViewById(R.id.item_status_pending);
            m_itemImage = itemView.findViewById(R.id.item_image);
            fg = itemView.findViewById(R.id.view_fg);
            bg = itemView.findViewById(R.id.view_bg);
        }
    }

    public ItemsAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Item item = itemList.get(position);
        holder.m_title.setText(item.getTitle());
        if(!item.getAttachment().isEmpty() || item.getAttachment() != null) {
            Uri imageUri = Uri.parse(item.getAttachment());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                holder.m_itemImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(item.isDone()){
            holder.m_done.setVisibility(View.VISIBLE);
            holder.m_pending.setVisibility(View.GONE);
        }
        else {
            holder.m_done.setVisibility(View.GONE);
            holder.m_pending.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /***
     * Changes the status from pending to done
     * @param position to detect the direction of swipe
     * @param category denotes which category
     */
    public void doneItem(int position,int category){
        ApplicationProperties.getINSTANCE().getCategoryList().get(category).getItems().get(position).setStatus(true);
        notifyItemChanged(position);
    }

    /***
     * Chnages the status from done to pending
     * @param position to detect the direction of swipe
     * @param category denotes which category
     */
    public void pendingItem(int position,int category){
        ApplicationProperties.getINSTANCE().getCategoryList().get(category).getItems().get(position).setStatus(false);
        notifyItemChanged(position);
    }
}
