package com.xhg.test.image.pictures

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import com.xhg.test.image.R
import com.xhg.test.image.data.Picture
import com.xhg.test.image.picturedetail.PictureDetailActivity
import com.xhg.test.image.settings.SettingsActivity
import com.xhg.test.image.utils.Log
import java.util.*


/**
 * @author xionghg
 * @created 17-7-20.
 */

class PicturesFragment : Fragment(), PicturesContract.View {

    private lateinit var presenter: PicturesContract.Presenter
    private lateinit var recyclerAdapter: PicturesAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var picturesView: RelativeLayout
    private var toButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        recyclerAdapter = PicturesAdapter()
        recyclerAdapter.onItemClickListener = { presenter.openPictureDetails(it) }
    }

    override fun onStart() {
        super.onStart()
        presenter.registerRepositoryCallBack(true)
    }

    override fun onStop() {
        super.onStop()
        presenter.registerRepositoryCallBack(false)
        presenter.cancelCurrentOperation()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setPresenter(presenter: PicturesContract.Presenter) {
        this.presenter = Objects.requireNonNull<PicturesContract.Presenter>(presenter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.result(requestCode, resultCode)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.pictures_frag, container, false)

        picturesView = root.findViewById<View>(R.id.picturesRL) as RelativeLayout

        // Set up progress indicator
        refreshLayout = root.findViewById<View>(R.id.refresh_layout) as SwipeRefreshLayout
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity!!, R.color.colorPrimary),
                ContextCompat.getColor(activity!!, R.color.colorAccent),
                ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))

        // Set the scrolling view in the custom SwipeRefreshLayout.
        // swipeRefreshLayout.setScrollUpChild(listView);
        toButton = root.findViewById<View>(R.id.to_button) as Button
        toButton!!.setOnClickListener {
            val intent = Intent(activity, PictureDetailActivity::class.java)
            intent.putExtra("strategy_index", 4)
            startActivity(intent)
        }

        recyclerView = root.findViewById<RecyclerView>(R.id.recycle_view)
        val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        //设置布局管理器
        recyclerView.layoutManager = layoutManager
        //设置Adapter
        recyclerView.adapter = recyclerAdapter
        //设置增加或删除条目的动画
        recyclerView.itemAnimator = DefaultItemAnimator()
        refreshLayout.setOnRefreshListener { presenter.loadPictures(false) }

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.e(TAG, "onOptionsItemSelected: ")
        when (item!!.itemId) {
            R.id.menu_help -> showMessage("coming soon")
            R.id.menu_settings -> startActivity(Intent(activity, SettingsActivity::class.java))
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.pictures_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        refreshLayout.post { refreshLayout.isRefreshing = active }
    }

    override fun showPictures(Pictures: MutableList<Picture>) {
        recyclerAdapter.replaceData(Pictures)
        picturesView.visibility = View.VISIBLE
    }

    override fun showNoPictures() {
        showMessage("No pictures")
    }

    override fun showPictureUpdate(index: Int, picture: Picture) {
        recyclerAdapter.changeItem(index, picture)
    }

    override fun showPictureDetailsUi(PictureId: Int) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        val intent = Intent(context, PictureDetailActivity::class.java)
        intent.putExtra(PictureDetailActivity.EXTRA_PICTURE_ID, PictureId)
        startActivity(intent)
    }

    override fun showLoadingPicturesError() {
        showMessage("loading pictures error")
    }

    private fun showMessage(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "PicturesFragment"

        // Requires empty public constructor
        fun newInstance(): PicturesFragment {
            return PicturesFragment()
        }
    }

}
