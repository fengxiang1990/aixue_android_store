package com.wenba.aixuestore.data.source

import com.wenba.aixuestore.data.source.remote.RemoteDataSource
import com.wenba.aixuestore.network.BaseResponse
import io.reactivex.Flowable
import org.json.JSONObject

class AppDataRepostory(remoteDataSource: RemoteDataSource) : AppDataSource {

    private val mRemoteDataSource: RemoteDataSource = checkNotNull(remoteDataSource)

    override fun loadAppInfos(ukey: String, _api_key: String,page : Int): Flowable<BaseResponse<BaseAppInfo>>? {
        return mRemoteDataSource.loadAppInfos(ukey, _api_key,page)
    }

    override fun loadAppDetail(aKey: String, _api_key: String): Flowable<JSONObject>? {
        return mRemoteDataSource.loadAppDetail(aKey, _api_key)
    }

}