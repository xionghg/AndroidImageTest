package com.xhg.test.image.pictures;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhg.test.image.R;
import com.xhg.test.image.data.Picture;
import com.xhg.test.image.utils.Log;

import java.util.List;

/**
 * @author xionghg
 * @created 2017-07-11.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder> {
    private static final String TAG = "MainAdapter";

    private List<Picture> mPictures;

    private OnItemClickListener mOnItemClickListener;

    public PicturesAdapter(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void replaceData(List<Picture> data) {
        this.mPictures = data;
        Log.d(TAG, "updateData: size=" + mPictures.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPictures == null ? 0 : mPictures.size();
    }

    public void addItem(Picture picture) {
        int position = getItemCount();
        addItem(position, picture);
    }

    public void addItem(int position, Picture picture) {
        mPictures.add(position, picture);
        notifyItemInserted(position);
    }

    public void changeItem(int position, Picture picture) {
        Log.d(TAG, "changeItem: position=" + position);
        mPictures.set(position, picture);
        notifyItemChanged(position);
//        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mPictures.remove(position);
        notifyItemRemoved(position);
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final PicturesViewHolder holder, final int position) {
//        final BitmapGenerator colorHolder = new BitmapGenerator(504, 504);
//        colorHolder.setStrategy(mPictures.get(position).getStrategy());
//        colorHolder.setCallback(new BitmapGenerator.SimpleCallback() {
//            @Override
//            public void onBitmapCreated() {
//                holder.imageView.setImageBitmap(colorHolder.createBitmap());
//            }
//        }).startInParallel();
        Log.v(TAG, "onBindViewHolder: position=" + position);
        Bitmap bitmap = mPictures.get(position).getBitmap();
        if (bitmap != null) {
            Log.v(TAG, "onBindViewHolder: bitmap not null");
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.empty_image_150dp);
        }
        holder.textView.setText(mPictures.get(position).getStrategy().getDescription());
        holder.imageView.setOnClickListener(v ->
                mOnItemClickListener.onItemClick(mPictures.get(position))
        );
    }

    @Override
    public PicturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item, parent, false);
        return new PicturesViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(Picture picture);
    }

    static class PicturesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public PicturesViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            textView = view.findViewById(R.id.text_picture_description);
        }
    }
}
