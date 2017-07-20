package com.xhg.test.image.pictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhg.test.image.R;
import com.xhg.test.image.data.Picture;
import com.xhg.test.image.picturedetail.PictureDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesFragment extends Fragment implements PicturesContract.View {

    private PicturesContract.Presenter mPresenter;
    private PicturesAdapter mAdapter;

//    private View mNoPicturesView;
//
//    private ImageView mNoPictureIcon;
//
//    private TextView mNoPictureMainView;
//
//    private TextView mNoPictureAddView;

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
        mAdapter = new PicturesAdapter(new ArrayList<Picture>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull PicturesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
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

        // Set up Pictures view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycle_view);
        recyclerView.setAdapter(mAdapter);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadPictures(false);
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                showMessage("coming soon");
                break;
            case R.id.menu_refresh:
                mPresenter.loadPictures(true);
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
    public void showEmptyPictures(int size) {

    }

    @Override
    public void showPictures(List<Picture> Pictures) {
        mAdapter.replaceData(Pictures);
        mPicturesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoPictures() {
        showMessage("No pictures");
    }

//    private void showNoPicturesViews(String mainText, int iconRes, boolean showAddView) {
//        mPicturesView.setVisibility(View.GONE);
//        mNoPicturesView.setVisibility(View.VISIBLE);
//
//        mNoPictureMainView.setText(mainText);
//        mNoPictureIcon.setImageDrawable(getResources().getDrawable(iconRes));
//        mNoPictureAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
//    }

    @Override
    public void showPictureDetailsUi(String PictureId) {
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
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
