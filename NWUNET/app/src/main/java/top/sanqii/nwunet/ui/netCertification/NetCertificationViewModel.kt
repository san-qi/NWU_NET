package top.sanqii.nwunet.ui.netCertification

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class NetCertificationViewModel(application: Application): AndroidViewModel(application){
    val uid = MutableLiveData<Int>().apply {
        value = application.getSharedPreferences("privateData", Context.MODE_PRIVATE).getInt("uid", -1)
    }
}