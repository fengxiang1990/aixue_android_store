package com.wenba.aixuestore.apps

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.finalteam.loadingviewfinal.LoadMoreMode
import cn.finalteam.loadingviewfinal.RecyclerViewFinal
import cn.finalteam.loadingviewfinal.SwipeRefreshLayoutFinal
import com.bumptech.glide.Glide
import com.wenba.aixuestore.R
import com.wenba.aixuestore.data.AppInfo
import com.wenba.aixuestore.util.UrlMapping
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_apps.*

class AppsFragment : Fragment(), AppContract.View {

    val appInfos = ArrayList<AppInfo>()
    var adapter: AppAdapter? = null
    var pressenter: AppContract.Pressenter? = null
    var filter = Filter.MASTER
    var recycleView: RecyclerViewFinal? = null
    var swipeRefreshLayout: SwipeRefreshLayoutFinal? = null
    var page = 1
    var isLoadMore = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_apps, container, false)
        recycleView = view?.findViewById(R.id.recycleView) as RecyclerViewFinal
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayoutFinal
        recycleView?.overScrollMode = View.OVER_SCROLL_NEVER
        recycleView?.layoutManager = LinearLayoutManager(activity)
        recycleView?.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity)
                .color(Color.WHITE)
                .sizeResId(R.dimen.recycler_divider_height)
                .build())
        adapter = AppAdapter(appInfos)
        recycleView?.adapter = adapter
        swipeRefreshLayout?.isRefreshing = true
        swipeRefreshLayout?.setOnRefreshListener({
            page = 1
            isLoadMore = false
            pressenter?.loadAppInfos(page, filter)
        })
        recycleView?.setHasLoadMore(true)
        recycleView?.setLoadMoreMode(LoadMoreMode.SCROLL)
        recycleView?.setOnLoadMoreListener {
            page = page.inc()
            isLoadMore = true
            pressenter?.loadAppInfos(page, filter)
        }

        val tag = arguments["tag"] as String
        when (tag) {
            "master" -> filter = Filter.MASTER
            "pro" -> filter = Filter.PRO
            "all" -> filter = Filter.ALL
        }
        pressenter?.loadAppInfos(page, filter)
        return view
    }


    override fun toInstall(aKey: String, appName: String) {
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra("aKey", aKey)
        intent.putExtra("appName", appName)
        startActivity(intent)
    }


    override fun showRefresh(isRefresh: Boolean) {
        if (!isRefresh) {
            swipeRefreshLayout?.onRefreshComplete()
        }
    }

    override fun showApps(appinfos: List<AppInfo>?) {
        if (!isLoadMore) {
            this.appInfos.clear()
        }
        this.appInfos.addAll(appinfos!!)
        adapter?.notifyDataSetChanged()
    }

    override fun toAppDetail(appKey: String, appName: String) {
        val intent = Intent(activity, AppDetailActivity::class.java)
        intent.putExtra("aKey", appKey)
        intent.putExtra("appName", appName)
        startActivity(intent)
    }

    override fun setPresenter(presenter: AppContract.Pressenter) {
    }

    inner class AppAdapter : RecyclerView.Adapter<AppAdapter.AppViewHolder> {

        var apps: List<AppInfo>

        constructor(apps: List<AppInfo>) {
            this.apps = apps
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppViewHolder {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_app
                    , parent, false)
            return AppViewHolder(view)
        }

        override fun onBindViewHolder(holder: AppViewHolder?, position: Int) {
            val appInfo = apps[position]
            Log.e(tag, appInfo.toString())
            holder!!.text_index.text = (position + 1).toString()
            holder!!.text_name.text = appInfo.appName
            holder!!.text_version.text = appInfo.appVersion + "(build " + appInfo.appBuildVersion + ")"
            holder!!.text_update_time.text = appInfo.appCreated
            if (appInfo.appType == "1") {
                holder!!.text_platform.text = "IOS"
            } else if (appInfo.appType == "2") {
                holder!!.text_platform.text = "Android"
            }
            Glide.with(activity)
                    .load(UrlMapping.ICON_DOMAIN + appInfo.appIcon)
                    .placeholder(R.mipmap.icon_img_faild)
                    .into(holder!!.img_icon)

            holder.img_setup.setOnClickListener({
                toInstall(appInfo.appKey!!, appInfo.appName!!)
            })

            holder!!.itemLayout.setOnClickListener({
                toAppDetail(appInfo.appKey!!, appInfo.appName!!)
            })
        }

        override fun getItemCount(): Int {
            return apps.size
        }


        inner class AppViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var itemLayout = itemView!!.findViewById(R.id.itemLayout) as LinearLayout
            var text_index = itemView!!.findViewById(R.id.text_index) as TextView
            var img_icon = itemView!!.findViewById(R.id.img_icon) as ImageView
            var text_name = itemView!!.findViewById(R.id.text_name) as TextView
            var text_version = itemView!!.findViewById(R.id.text_version) as TextView
            var text_platform = itemView!!.findViewById(R.id.text_platform) as TextView
            var text_update_time = itemView!!.findViewById(R.id.text_update_time) as TextView
            var img_setup = itemView!!.findViewById(R.id.img_setup) as ImageView
        }
    }


    override fun showFilteringPopUpMenu() {
    }

    override fun loadComplete() {
        recycleView?.onLoadMoreComplete()
    }

    override fun showFaild(msg: String) {
        text_error.visibility = View.VISIBLE
        text_error.text = msg
    }

    override fun hideFaild() {
        text_error.visibility = View.GONE
    }
}