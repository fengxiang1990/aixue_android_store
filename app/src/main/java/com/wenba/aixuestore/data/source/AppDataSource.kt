package com.wenba.aixuestore.data.source

import com.wenba.aixuestore.network.BaseResponse
import io.reactivex.Flowable
import org.json.JSONObject

interface AppDataSource {

    fun loadAppInfos(ukey: String, _api_key: String, page: Int): Flowable<BaseResponse<BaseAppInfo>>?

    fun loadAppDetail(aKey: String, _api_key: String): Flowable<JSONObject>?
}