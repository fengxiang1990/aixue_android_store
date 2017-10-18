package com.wenba.aixuestore.data.source.remote

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.wenba.aixuestore.data.source.AppDataSource
import com.wenba.aixuestore.data.source.BaseAppInfo
import com.wenba.aixuestore.network.BaseResponse
import com.wenba.aixuestore.network.OkHttpKotlinHelper
import com.wenba.aixuestore.util.JsonWrapper
import com.wenba.aixuestore.util.UrlMapping
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class RemoteDataSource : AppDataSource {

    val tag = "RemoteDataSource"

    override fun loadAppDetail(aKey: String, _api_key: String): Flowable<JSONObject> {
        return Flowable.just(UrlMapping.APP_DETAIL)
                .subscribeOn(Schedulers.io())
                .flatMap({ url ->
                    val map = HashMap<String, Any>()
                    map.put("aKey", aKey)
                    map.put("_api_key", _api_key)
                    val response = OkHttpKotlinHelper.postFormSync(url, map)
                    val result = response.body()?.string()
                    Flowable.just(JSONObject(result))
                })
                .observeOn(AndroidSchedulers.mainThread())
    }


    override fun loadAppInfos(ukey: String, _api_key: String, page: Int): Flowable<BaseResponse<BaseAppInfo>> {
        return Flowable.just(UrlMapping.ListMyPublished)
                .subscribeOn(Schedulers.io())
                .flatMap({ url ->
                    val map = HashMap<String, Any>()
                    map.put("uKey", ukey)
                    map.put("_api_key", _api_key)
                    map.put("page", page)
                    val response = OkHttpKotlinHelper.postFormSync(url, map)
                    val result = response.body()?.string()
                    Log.e(tag, "result->" + result)
                    val jsonObject = JSONObject(result)
                    val code = jsonObject["code"]
                    if (code == 0) {
                        Flowable.just(JsonWrapper.parse<BaseResponse<BaseAppInfo>>(result, object : TypeToken<BaseResponse<BaseAppInfo>>() {}.type))
                    } else {
                        Flowable.just(BaseResponse())
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
    }

}