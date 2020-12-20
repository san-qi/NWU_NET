package top.sanqii.nwunet.ui.netCertification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import top.sanqii.nwunet.*


// 校园网信息录入
class NetCertificationFragment : Fragment() {

    private lateinit var viewModel: NetCertificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(NetCertificationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_netcertification, container, false)
        val id: EditText = root.findViewById(R.id.formIdEdit)
        val password: EditText = root.findViewById(R.id.formPasswdEdit)
        val warning: EditText = root.findViewById(R.id.warningText)
        warning.isEnabled = false
        viewModel.uid.observe(viewLifecycleOwner){
            val value = it.toString().toInt()
            if (value != -1)
                id.setText(it.toString())
        }

        // 更改控件的状态
        root.findViewById<Switch>(R.id.editAble).apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    id.isEnabled = false
                    password.isEnabled = false
                }
                else{
                    id.isEnabled = true
                    password.isEnabled = true
                }
            }

            // 判断用户是否已经录入学号信息
            activity?.getSharedPreferences("privateData", Context.MODE_PRIVATE)
                    ?.getInt("uid", -1).run {
                        isChecked = this != -1
                        isEnabled = isChecked
                    }

            // 获取该fragment所属的activity中的fab浮动按钮，重定制该按钮的功能
            customFab(activity) {
                // 当读写开关已打开时
                if (isChecked){
                    toastShow("该界面正处于保护状态\n请将读写保护关闭")
                }
                // 保证输入值不为空
                else if (id.text.length>5 && password.text.isNotEmpty()){
                    // 将值记录到sharedPreferences中
                    context?.getSharedPreferences("privateData", Context.MODE_PRIVATE)?.edit()?.apply {
                        val uid = id.text.toString().toInt()
                        viewModel.uid.postValue(uid)
                        putInt("uid", uid)
                        putString("networkPassword", password.text.toString())
                        apply()
                    }
                    toastShow("校园网身份信息录入完毕")

                    isChecked = true
                    isEnabled = true

                    // 可以在返回主页时清空fragment的栈吗
                    findNavController().navigate(R.id.nav_home)
                }
                else{
                    toastShow("输入的学号或者密码不正确")
                }
            }
        }

        return root
    }
}
