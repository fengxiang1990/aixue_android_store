package com.wenba.aixuestore.network

import android.util.Log
import okhttp3.*

object OkHttpKotlinHelper {

    private val tag = "OkHttpKotlinHelper"

    private var client: OkHttpClient? = null

    @JvmStatic
    private fun getOkHttpClient(): OkHttpClient {
        if (client == null) {
            client = OkHttpClient.Builder()
                    .addNetworkInterceptor(LogInterceptor()).build()
        }
        return client!!
    }

    class LogInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain?): Response {
            val request = chain?.request()
            val response = chain?.proceed(request)
            Log.d(tag, "request->" + request.toString())
            Log.d(tag, "response->" + response.toString())
            return response!!
        }
    }

    @JvmStatic
    fun postFormSync(url: String, params: Map<String, Any>): Response {
        val keys = params.keys
        val formBody = FormBody.Builder()
        for (key in keys) {
            formBody.add(key, params[key].toString())
        }
        var request = Request.Builder()
                .url(url)
                .post(formBody.build())
                .build()
        return OkHttpKotlinHelper.getOkHttpClient().newCall(request).execute()
    }
}