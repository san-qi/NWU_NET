package top.sanqii.nwunet.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "若有对该APP的改进建议" +
                "\n或者该APP执行时出现问题" +
                "\n请联系邮箱: sanqii@sina.com" +
                "\n戳右下角即可发送信息反馈给作者"
    }
    val text: LiveData<String> = _text
}