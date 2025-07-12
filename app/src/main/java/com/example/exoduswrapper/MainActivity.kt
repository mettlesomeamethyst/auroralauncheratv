package com.example.exoduswrapper

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://reports.exodus-privacy.eu.org/en/reports/"
    private val PACKAGE_REGEX = Regex("""https://reports\.exodus-privacy\.eu\.org/reports/([a-zA-Z0-9_.]+?)/latest""")

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()

                val match = PACKAGE_REGEX.find(url)
                if (match != null) {
                    val packageId = match.groupValues[1]
                    openAuroraStore(packageId)
                    return true
                }

                return false
            }
        }

        webView.loadUrl(BASE_URL)
    }

    private fun openAuroraStore(packageId: String) {
        val intentUri = Uri.parse("intent://details?id=$packageId#Intent;scheme=market;package=com.aurora.store.nightly;component=com.aurora.store.nightly/com.aurora.store.MainActivity;end;")
        try {
            val intent = Intent.parseUri(intentUri.toString(), Intent.URI_INTENT_SCHEME)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}