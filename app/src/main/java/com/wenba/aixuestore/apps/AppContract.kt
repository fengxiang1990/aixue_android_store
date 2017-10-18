package com.wenba.aixuestore.apps

import com.wenba.aixuestore.BasePresenter
import com.wenba.aixuestore.BaseView
import com.wenba.aixuestore.data.AppInfo
import com.wenba.aixuestore.data.AppInfoDetail

interface AppContract {

    interface View : BaseView<Pressenter> {

        fun showFilteringPopUpMenu()
        fun showRefresh(isRefresh: Boolean)
        fun loadComplete()
        fun showApps(appinfos: List<AppInfo>?)
        fun toAppDetail(appKey: String, appName: String)
        fun toInstall(aKey: String, appName: String)
    }

    interface DetailView : BaseView<Pressenter> {
        fun showAppDetail(appInfoDetail: AppInfoDetail?)
        fun toInstall(aKey: String, appName: String)
    }

    interface DetailPressenter : BasePresenter {
        fun loadAppDetail(aKey: String)
    }

    interface Pressenter : BasePresenter {

        fun loadAppInfos(page: Int = 1, filter: Filter = Filter.ALL)


    }
}