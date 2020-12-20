package top.sanqii.nwunet

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit


private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
private val client = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
        .readTimeout(10, TimeUnit.SECONDS) // 读取超时时间
        .build()

// 下面三个函数能重构出一个通用函数吗
fun post(url: String, callback: Callback, json: String, headers: Headers? = null) {
    val request: Request.Builder = Request.Builder()
    val body: RequestBody = json.toRequestBody(JSON)
    headers?.let {
        request.headers(it)
    }
    request.apply {
        url(url)
        post(body)
    }
    client.newCall(request.build()).enqueue(callback)
}

fun get(url: String, callback: Callback, headers: Headers?=null) {
    val request: Request.Builder = Request.Builder()
    headers?.let {
        request.headers(it)
    }
    request.apply {
        url(url)
        get()
    }
    client.newCall(request.build()).enqueue(callback)
}

fun delete(url: String, callback: Callback, headers: Headers?=null) {
    val request: Request.Builder = Request.Builder()
    headers?.let {
        request.headers(it)
    }
    request.apply {
        url(url)
        delete()
    }
    client.newCall(request.build()).enqueue(callback)
}
