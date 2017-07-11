package com.xhg.test.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhg.test.image.model.StrategyModel;
import com.xhg.test.image.strategies.ColorStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-04.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap-Main";


    private Button mToButton;
    private RecyclerView recyclerView;
    private List<Bitmap> mDatas;
    private MyRecyclerAdapter recycleAdapter;
    private StrategyModel mStrategyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        mToButton = (Button) findViewById(R.id.to_button);
        mToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BigImageActivity.class);
                intent.putExtra("strategy_index", 1);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recycleAdapter = new MyRecyclerAdapter(MainActivity.this, mDatas);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        mDatas = new ArrayList<Bitmap>();
        mStrategyModel = StrategyModel.getInstance();
        ColorStrategy[] ss = mStrategyModel.getStrategies();
        for (ColorStrategy s : ss) {
            final ColorHolder holder = new ColorHolder(150, 150);
            holder.setStrategy(s);
            holder.setCallback(new ColorHolder.Callback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgressUpdate(int progress) {

                }

                @Override
                public void onColorsCreated() {
                    mDatas.add(holder.createBitmap());
                    recycleAdapter.setData(mDatas);
                }
            });
            holder.startInParallel();
        }
    }


    static class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Bitmap> mDatas;
        private Context mContext;
        private LayoutInflater inflater;

        public MyRecyclerAdapter(Context context, List<Bitmap> data) {
            this.mContext = context;
            this.mDatas = data;
            inflater = LayoutInflater.from(mContext);
        }

        public void setData(List<Bitmap> data) {
            this.mDatas = data;
            Log.d(TAG, "setData: size=" + mDatas.size());
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        //填充onCreateViewHolder方法返回的holder中的控件
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.imageView.setImageBitmap(mDatas.get(position));
        }

        //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.recycle_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
        }
    }
}
