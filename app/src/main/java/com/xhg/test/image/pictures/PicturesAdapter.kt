package com.xhg.test.image.pictures

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.xhg.test.image.R
import com.xhg.test.image.data.Picture
import com.xhg.test.image.utils.Log

/**
 * @author xionghg
 * @created 2017-07-11.
 */

class PicturesAdapter(private val mOnItemClickListener: OnItemClickListener) : RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder>() {

    private var mPictures: MutableList<Picture> = ArrayList<Picture>()

    fun replaceData(data: MutableList<Picture>) {
        this.mPictures = data
        Log.d(TAG, "updateData: size=" + mPictures.size)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mPictures.size
    }

    fun addItem(picture: Picture) {
        val position = itemCount
        addItem(position, picture)
    }

    fun addItem(position: Int, picture: Picture) {
        mPictures.add(position, picture)
        notifyItemInserted(position)
    }

    fun changeItem(position: Int, picture: Picture) {
        Log.d(TAG, "changeItem: position=$position")
        mPictures[position] = picture
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        mPictures.removeAt(position)
        notifyItemRemoved(position)
    }

    // 填充onCreateViewHolder方法返回的holder中的控件
    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        //        final BitmapGenerator colorHolder = new BitmapGenerator(504, 504);
        //        colorHolder.setStrategy(mPictures.get(position).getStrategy());
        //        colorHolder.setCallback(new BitmapGenerator.SimpleCallback() {
        //            @Override
        //            public void onBitmapCreated() {
        //                holder.imageView.setImageBitmap(colorHolder.createBitmap());
        //            }
        //        }).startInParallel();
        Log.v(TAG, "onBindViewHolder: position=$position")
        val bitmap = mPictures[position].bitmap
        if (bitmap != null) {
            Log.v(TAG, "onBindViewHolder: bitmap not null")
            holder.imageView.setImageBitmap(bitmap)
        } else {
            holder.imageView.setImageResource(R.drawable.empty_image_150dp)
        }
        holder.textView.text = mPictures[position].strategy.description
        holder.imageView.setOnClickListener { mOnItemClickListener.onItemClick(mPictures[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return PicturesViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(picture: Picture)
    }

    open class PicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val textView: TextView

        init {
            imageView = itemView.findViewById(R.id.image)
            textView = itemView.findViewById(R.id.text_picture_description)
        }
    }

    companion object {
        private val TAG = "MainAdapter"
    }
}
