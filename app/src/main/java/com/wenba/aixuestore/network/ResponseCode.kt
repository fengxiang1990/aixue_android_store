package com.wenba.aixuestore.network

/**
 * Created by wenba on 2017/10/19.
 */
enum class ResponseCode(val code: Int, val msg: String) {

    SUCCESS(0, "Successful getData"),
    NOT_HAVE_NETWORK(-1, "没有网络,请检查网络连接"),
    OTHERS(-2, "请求错误");


}