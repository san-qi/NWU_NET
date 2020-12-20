package top.sanqii.nwunet.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import top.sanqii.nwunet.R
import top.sanqii.nwunet.customFab

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner){ textView.text = it }

        customFab(activity){
            postEmail()
        }

        return root
    }

    private fun postEmail(){
        val uri = Uri.parse("mailto:sanqii@sina.com")
        val intent = Intent(Intent.ACTION_SENDTO, uri);
        // 主题
        intent.putExtra(Intent.EXTRA_SUBJECT, "NWU NET")
        // 正文
        intent.putExtra(Intent.EXTRA_TEXT, "在此感谢您提出的意见或建议")
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"))
    }
}