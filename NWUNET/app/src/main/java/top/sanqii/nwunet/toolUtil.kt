package top.sanqii.nwunet

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


// 切换当前显示的fragment
fun changeFragment(from: Fragment, to: Fragment){
    from.parentFragmentManager.beginTransaction().apply {
        replace(R.id.nav_host_fragment, to)
        addToBackStack(null)
    }.commit()
}

// 获取该fragment所属的activity中的fab浮动按钮，重定制该按钮的功能
fun customFab(activity: Activity?, func: (View) -> Unit, errBack: ()->Unit = {}) {
    val fab: FloatingActionButton? = activity?.findViewById(R.id.fab)
    fab?.setOnClickListener {
        func(it)
    } ?: errBack()
}
// 控件未找到时的无回调的简易版本
fun customFab(activity: Activity?, func: (View) -> Unit) {
    customFab(activity, func){}
}

// 获取网络状态, 若为空则返回null
@RequiresApi(Build.VERSION_CODES.M)
fun getNetworkState(context: Context?): Network? {
    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork
}

fun getTimestamp(): Long {
    return Calendar.getInstance().timeInMillis
}

// 在MainActivity中显示一条消息
fun toastShow(msg: String, duration: Int = Toast.LENGTH_LONG){
    Thread(){
        Looper.prepare()
        Toast.makeText(MainActivity.context, msg, duration).show()
        Looper.loop()
    }.start()
}

