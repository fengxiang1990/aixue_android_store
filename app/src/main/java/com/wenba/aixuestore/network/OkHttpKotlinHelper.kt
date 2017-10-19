package com.wenba.aixuestore.network

import android.content.Context
import android.util.Log
import com.wenba.aixuestore.util.NetWorkUtils
import okhttp3.*
import org.json.JSONObject

object OkHttpKotlinHelper {

    private val tag = "OkHttpKotlinHelper"

    private var client: OkHttpClient? = null

    var context: Context? = null

    @JvmStatic
    fun init(context: Context) {
        OkHttpKotlinHelper.context = context
    }

    @JvmStatic
    private fun getOkHttpClient(): OkHttpClient {
        if (client == null) {
            client = OkHttpClient.Builder()
                    .retryOnConnectionFailure(false)
                    .addInterceptor(NetConnInterceptor())
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

    class NetConnInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain?): Response {
            Log.e(tag, "NetConnInterceptor in")
            val request = chain?.request()
            return if (!NetWorkUtils.checkNetWork(context)) {
                Log.e(tag, "NetConnInterceptor no network,return local response by customer")
                getDefaultResponse(request!!, NotHaveNetworkException())
            } else {
                Log.e(tag, "NetConnInterceptor  has network,proceed net request")
                chain?.proceed(request)!!
            }
        }
    }

    private fun getDefaultResponse(request: Request, e: Throwable): Response {
        val body = JSONObject()
        if (e is NotHaveNetworkException) {
            body.put("code", ResponseCode.NOT_HAVE_NETWORK.code)
            body.put("message", ResponseCode.NOT_HAVE_NETWORK.msg)
        } else {
            body.put("code", ResponseCode.OTHERS.code)
            body.put("message", ResponseCode.OTHERS.msg)
        }
        return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message(body["message"].toString())
                .body(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), body.toString()))
                .build()
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
        return try {
            OkHttpKotlinHelper.getOkHttpClient().newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            getDefaultResponse(request, e)
        }
    }
}