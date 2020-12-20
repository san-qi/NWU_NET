package top.sanqii.nwunet.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    var text = MutableLiveData<String>().apply {
        value = "请点击右下角完成校园网身份录入~"
    }
}