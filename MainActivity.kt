package com.example.exoduswrapper

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url.toString()
                                val regex = Regex("""https://reports\.exodus-privacy\.eu\.org/reports/([\w\.]+)/latest""")
                                val match = regex.find(url)
                                if (match != null) {
                                    val packageId = match.groupValues[1]
                                    val intentUrl =
                                        "intent://details?id=$packageId#Intent;scheme=market;package=com.aurora.store.nightly;component=com.aurora.store.nightly/com.aurora.store.MainActivity;end;"
                                    try {
                                        val intent = Intent.parseUri(intentUrl, Intent.URI_INTENT_SCHEME)
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    return true
                                }
                                return false
                            }
                        }
                        loadUrl("https://reports.exodus-privacy.eu.org/en/reports/")
                    }
                }
            )
        }
    }
}
