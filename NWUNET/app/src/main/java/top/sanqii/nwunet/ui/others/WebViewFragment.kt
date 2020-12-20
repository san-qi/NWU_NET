package top.sanqii.nwunet.ui.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import top.sanqii.nwunet.R
import top.sanqii.nwunet.customFab
import top.sanqii.nwunet.toastShow

class WebViewFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_webview, container, false)
        val myWebView: WebView = root.findViewById(R.id.webView)
        myWebView.settings.javaScriptEnabled = true
        myWebView.webViewClient = WebViewClient()
        myWebView.loadUrl("http://10.16.0.12:8081/login")

        customFab(activity){ toastShow("?QAQ?")}

        return root
    }
}