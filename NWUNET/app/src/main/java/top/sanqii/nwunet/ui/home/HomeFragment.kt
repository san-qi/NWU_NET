package top.sanqii.nwunet.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import top.sanqii.nwunet.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })

        // 判断用户是否已经录入学号信息
        activity?.getSharedPreferences("privateData", Context.MODE_PRIVATE)?.apply {
            if (getInt("uid", -1) != -1) {
                homeViewModel.text.value = "点击右下角进行认证"
                // 设定fab动作
                customFab(activity) {
                    Snackbar.make(it, "认证信息已发送", Snackbar.LENGTH_INDEFINITE)
                            .setAction("详情") {
                                getNetworkState(context)?.let {
                                    // 长时间没有消息弹出如何解决
                                    postConfirm(
                                            getInt("uid", 2018),
                                            getString("networkPassword", "").toString()
                                    )
                                }?: toastShow("网络未连接")
                            }.show()
                }
            }
            else{
                customFab(activity){
                    Snackbar.make(it, "请先录入你的学号和校园网认证密码", Snackbar.LENGTH_LONG)
                            .setAction("前往") {
                                findNavController().navigate(R.id.nav_netCertification)
                            }.show()
                }
            }
        }

        return root
    }

}
