package com.xhg.test.image.pictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xhg.test.image.R;
import com.xhg.test.image.data.Picture;
import com.xhg.test.image.picturedetail.PictureDetailActivity;
import com.xhg.test.image.settings.SettingsActivity;

import java.util.List;
import java.util.Objects;


/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesFragment extends Fragment implements PicturesContract.View {

    private static final String TAG = "PicturesFragment";

    private PicturesContract.Presenter mPresenter;
    private PicturesAdapter mRecyclerAdapter;

    private Button mToButton;
    private RecyclerView mRecyclerView;

    private RelativeLayout mPicturesView;

    public PicturesFragment() {
        // Requires empty public constructor
    }

    public static PicturesFragment newInstance() {
        return new PicturesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerAdapter = new PicturesAdapter(mItemListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.registerRepositoryCallBack(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.registerRepositoryCallBack(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull PicturesContract.Presenter presenter) {
        mPresenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pictures_frag, container, false);

        mPicturesView = (RelativeLayout) root.findViewById(R.id.picturesRL);

        // Set up progress indicator
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        // Set the scrolling view in the custom SwipeRefreshLayout.
        // swipeRefreshLayout.setScrollUpChild(listView);
        mToButton = (Button) root.findViewById(R.id.to_button);
        mToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PictureDetailActivity.class);
                intent.putExtra("strategy_index", 4);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        mRecyclerView.setAdapter(mRecyclerAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadPictures(false);
            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case R.id.menu_help:
                showMessage("coming soon");
                break;
            case R.id.menu_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pictures_fragment_menu, menu);
    }

    /**
     * Listener for clicks on Pictures in the ListView.
     */
    PicturesAdapter.OnItemClickListener mItemListener = new PicturesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Picture picture) {
            mPresenter.openPictureDetails(picture);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showEmptyPictures(List<Picture> Pictures) {
        mRecyclerAdapter.replaceData(Pictures);
        mPicturesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoPictures() {
        showMessage("No pictures");
    }

    @Override
    public void showPictureUpdate(int index, Picture picture) {
        mRecyclerAdapter.changeItem(index, picture);
    }

    @Override
    public void showPictureDetailsUi(int PictureId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), PictureDetailActivity.class);
        intent.putExtra(PictureDetailActivity.EXTRA_PICTURE_ID, PictureId);
        startActivity(intent);
    }

    @Override
    public void showLoadingPicturesError() {
        showMessage("loading pictures error");
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

}
