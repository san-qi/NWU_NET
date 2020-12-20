package top.sanqii.nwunet.ui.home

import android.R.string
import com.google.gson.Gson
import okhttp3.*
import okio.IOException
import org.json.JSONObject
import top.sanqii.nwunet.*
import java.lang.Exception
import java.util.*


//整个过程中所需的头部
val headers = Headers.Builder()

// ConfirmCallBack所需的数据格式
private data class ConfirmData(
        val deviceType: String,
        val redirectUrl: String,
        val type: String,
        val webAuthPassword: String,
        val webAuthUser: Int
)

private data class ConfirmResponse(
        val createdAt: Int,
        val error: Int,
        val session: Session?,
        val statusCode: Int,
        val token: String,
        val truncated: Boolean
)

private data class Session(
        val context: String,
        val id: String,
        val keepalive: Boolean,
        val keepalive_interval: Int,
        val network_changed: Boolean,
        val started_at: Int,
        val token_expires_in: Int
)

// DeviceListCallBack所需的数据格式
private data class DeviceList(
        val concurrency: Int,
        val sessions: List<DeviceSession>
)

private data class DeviceSession(
        val acct_session_id: String,
        val acct_start_time: String,
        val acct_unique_id: String,
        val assignment: String,
        val calling_station_id: String,
        val deviceType: String,
        val experienceEndTime: Int,
        val framed_ip_address: String,
        val nas_ip_address: String,
        val user_name: String
)


// 定义了Callback中的一些基本的逻辑, 使用时只需要重写process逻辑就可以了
private abstract class BaseCallBack: Callback {
    override fun onFailure(call: Call, e: IOException) {
        toastShow("网络发生波动\n请稍后再试")
    }

    override fun onResponse(call: Call, response: Response) {
        response.body?.run{
            process(this, response.code)
        }?:run {
            toastShow("认证失败")
        }
        response.close()
    }

    abstract fun process(responseBody: ResponseBody, code: Int)
}


private class ConfirmCallBack: BaseCallBack() {
    override fun process(responseBody: ResponseBody, code: Int){
        val networkInfo = Gson().fromJson(responseBody.string(), ConfirmResponse::class.java)
        when (networkInfo.statusCode){
            200 -> {
                toastShow("认证成功\n会话序列号: " + networkInfo.session?.id)
            }
            400 -> {
                headers.add("Authorization", networkInfo.token)
                get(getUrlAddress("/session/list"),
                        DeviceListCallBack(), headers.build())
            }
            else -> {
                toastShow("认证失败")
            }
        }
    }
}

private class DeviceListCallBack: BaseCallBack(){
    override fun process(responseBody: ResponseBody, code: Int){
        // 请求返回值不为200, 应该是用户密码错误导致
        if (code!=200){
            toastShow("校园网认证密码错误\n请重新录入你的校园网认证密码")
            return
        }
        val networkInfo = Gson().fromJson(responseBody.string(), DeviceList::class.java)
        if (networkInfo.sessions.isNotEmpty()){
            var acctId = ""
            var deviceType = ""
            for (session in networkInfo.sessions){
                when (session.deviceType.toLowerCase(Locale.ROOT)){
                    "android" -> {
                        acctId = session.acct_unique_id
                        deviceType = session.deviceType
                    }
                    "unknown" -> {
                        acctId = session.acct_unique_id
                        deviceType = session.deviceType
                        break
                    }
                    else -> continue
                }
            }
            // 如果当前已有两台设备, 则下线一台
            if (networkInfo.sessions.size==2){
                if(acctId==""){
                    // 这里需要加个界面供用户选择吗
                    toastShow("可能同时有两台PC设备\n已移除其中一台")
                    acctId =  networkInfo.sessions[0].acct_unique_id
                    delete(getUrlAddress("/session/acctUniqueId/$acctId"),
                            RemoveDeviceCallBack(), headers.build())
                }
                else{
                    toastShow("已经有两台设备存在\n其中一台${deviceType}设备被移除\n请重新点击认证按钮")
                    delete(getUrlAddress("/session/acctUniqueId/$acctId"),
                            RemoveDeviceCallBack(), headers.build())
                }
            }
            // 设备小于两台时应该时用户的密码错误导致
            else{
                toastShow("校园网认证密码错误\n请重新录入你的校园网认证密码")
            }
        }
        else{
            toastShow("校园网认证密码错误\n请重新录入你的校园网认证密码")
        }
    }
}

private class RemoveDeviceCallBack: BaseCallBack(){
    override fun process(responseBody: ResponseBody, code: Int) {
        print("")
    }
}


private fun getUrlAddress(paddingUrl: String): String {
    val baseUrl = "http://10.16.0.12:8081/portal/api/v2"
    return "$baseUrl$paddingUrl?noCache=${getTimestamp()}"
}

fun postConfirm(uid: Int, passWord: String) {
    val origUrl = "https://s3.cn-northwest-1.amazonaws.com.cn/captive-portal/connection-test.html?noCache=${getTimestamp()}"
    val redirectUrl = "https://10.16.0.12:8081/?usermac=${getMacAddress()}&userip=${getIpAddress()}&origurl=$origUrl&nasip=10.100.0.1"
    val aConfirm = ConfirmData("Android", redirectUrl, "login", passWord, uid)
    val jsonStr = Gson().toJson(aConfirm)
    val urlAddress = getUrlAddress("/online")

    post(urlAddress, ConfirmCallBack(), jsonStr)
}
