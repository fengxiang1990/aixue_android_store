package com.wenba.aixuestore.apps

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.wenba.aixuestore.R
import com.wenba.aixuestore.data.source.AppDataRepostory
import com.wenba.aixuestore.data.source.remote.RemoteDataSource
import com.wenba.aixuestore.util.Config
import com.wenba.aixuestore.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.support.v4.withArguments
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    val LAST_TAB = "lastTabPosition"
    val LAST_ENV = "lastEnv"
    var titles: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        titles = arrayOf(getString(R.string.nav_master)
                , getString(R.string.nav_pro), getString(R.string.nav_all))

        val masterFragment = Fragment.instantiate(this, AppsFragment::class.java.name).withArguments(Pair("tag", "master")) as AppsFragment
        val proFragment = Fragment.instantiate(this, AppsFragment::class.java.name).withArguments(Pair("tag", "pro")) as AppsFragment
        val allFragment = Fragment.instantiate(this, AppsFragment::class.java.name).withArguments(Pair("tag", "all")) as AppsFragment
        val fragments = arrayListOf<Fragment>(masterFragment, proFragment, allFragment)

        val repository = AppDataRepostory(RemoteDataSource())
        masterFragment.pressenter = AppPressenter(repository, masterFragment)
        proFragment.pressenter = AppPressenter(repository, proFragment)
        allFragment.pressenter = AppPressenter(repository, allFragment)

        viewPager.adapter = MyPagerAdapter(fragments)
        tabLayout.addTab(tabLayout.newTab().setText(titles!![0]))
        tabLayout.addTab(tabLayout.newTab().setText(titles!![1]))
        tabLayout.addTab(tabLayout.newTab().setText(titles!![2]))
        tabLayout.setupWithViewPager(viewPager)
        val selection = SharedPreferenceUtil.getInt(this, LAST_TAB, 0)
        viewPager.currentItem = selection!!
        checkedId = SharedPreferenceUtil.getInt(this, LAST_ENV, R.id.env1)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferenceUtil.save(this, LAST_TAB, tabLayout.selectedTabPosition)
        SharedPreferenceUtil.save(this, LAST_ENV, checkedId)
    }

    inner class MyPagerAdapter : FragmentPagerAdapter {

        private val fragments: ArrayList<Fragment>

        constructor(fragments: ArrayList<Fragment>) : super(supportFragmentManager) {
            this.fragments = fragments
        }

        override fun getItemPosition(`object`: Any?): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles!![position]
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_apps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_filter -> showFilteringPopUpMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    var checkedId = R.id.env1

    private fun showFilteringPopUpMenu() {
        val popup = PopupMenu(this@MainActivity, this@MainActivity.findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_menus, popup.menu)
        popup.menu.findItem(checkedId).isChecked = true
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.env1 -> {
                    checkedId = R.id.env1
                    Config.uKey = Config.uKey1
                    Config._api_key = Config._api_key1
                    viewPager.adapter.notifyDataSetChanged()
                }
                R.id.env2 -> {
                    checkedId = R.id.env2
                    Config.uKey = Config.uKey2
                    Config._api_key = Config._api_key2
                    viewPager.adapter.notifyDataSetChanged()
                }
            }
            true
        }

        popup.show()
    }

}
