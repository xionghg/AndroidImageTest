package com.xhg.test.image.pictures;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhg.test.image.strategies.ColorHolder;
import com.xhg.test.image.R;
import com.xhg.test.image.strategies.ColorStrategy;

import java.util.List;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-11.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.MyViewHolder> {

    private static final String TAG = "TestBitmap-MainAdapter";

    private List<ColorStrategy> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * 事件回调监听
     */
    private PicturesAdapter.OnItemClickListener mOnItemClickListener;

    public PicturesAdapter(Context context, List<ColorStrategy> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updateData(List<ColorStrategy> data) {
        this.mData = data;
        Log.d(TAG, "updateData: size=" + mData.size());
        notifyDataSetChanged();
    }

    public void addItem(int position, ColorStrategy colorStrategy) {
        mData.add(position, colorStrategy);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(PicturesAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ColorHolder colorHolder = new ColorHolder(504, 504);
        colorHolder.setStrategy(mData.get(position));
        colorHolder.setCallback(new ColorHolder.SimpleCallback() {
            @Override
            public void onColorsCreated() {
                holder.imageView.setImageBitmap(colorHolder.createBitmap());
            }
        }).startInParallel();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.imageView, position);
                }
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
        }
    }
}
