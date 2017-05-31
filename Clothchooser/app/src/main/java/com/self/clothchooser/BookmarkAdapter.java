package com.self.clothchooser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.self.clothchooser.database.ClothPair;
import com.self.clothchooser.database.Utility;

import java.util.ArrayList;

/**
 * Created by nishant on 15-05-2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.Holder> {
    private Context context;
    private ArrayList<ClothPair> clothPairArrayList;
    private LayoutInflater inflater;

    public BookmarkAdapter(Context context, ArrayList<ClothPair> clothPairArrayList) {
        this.context = context;
        this.clothPairArrayList = clothPairArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Toast.makeText(context, "" + clothPairArrayList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.model, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.model_imgshirt.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(position).getShirt())));
        holder.model_imgpant.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(position).getPant())));
        holder.layoutmodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookmarkSingleActivity.class);
                intent.putExtra("id", clothPairArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clothPairArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView model_imgshirt, model_imgpant;
        private LinearLayout layoutmodel;

        public Holder(View itemView) {
            super(itemView);
            layoutmodel = (LinearLayout) itemView.findViewById(R.id.modellayout);
            model_imgpant = (ImageView) itemView.findViewById(R.id.model_imgpant);
            model_imgshirt = (ImageView) itemView.findViewById(R.id.model_imgshirt);
        }
    }

    private Bitmap imageresize(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 450, 450, false);
        return bitmap;
    }
}
