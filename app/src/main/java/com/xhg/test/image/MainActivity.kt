package com.xhg.test.image

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.xhg.test.image.pictures.PicturesActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private var testItems: MutableList<TestItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            // 设置图标
            it.setHomeAsUpIndicator(R.drawable.ic_menu_white_32dp)
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        getList()
        recyclerView.adapter = MainAdapter(testItems)
    }

    private fun getList() {
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
        testItems.add(TestItem("Image Test", PicturesActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> Toast.makeText(this, "Home function is coming soon", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal class TestItem(internal var name: String, internal var cls: Class<*>)

    internal class MainAdapter(var data: MutableList<TestItem>?) : RecyclerView.Adapter<MainHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            return MainHolder(parent)
        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            data?.let { holder.bindData(it[position], position) }
        }
    }

    internal class MainHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_main_test, viewGroup, false)) {

        private val textView: TextView = itemView.findViewById(R.id.test_item_name)

        fun bindData(testItem: TestItem, position: Int) {
            textView.text = testItem.name
            itemView.setOnClickListener { itemView.context.startActivity(Intent(itemView.context, testItem.cls)) }
        }
    }

}
