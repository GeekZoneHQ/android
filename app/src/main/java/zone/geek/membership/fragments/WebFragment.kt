/**
 *   Copyright 2020 Geek Zone UK
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package zone.geek.membership.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_web.view.*
import zone.geek.membership.R
import zone.geek.membership.activity.GeekZoneActivity.Companion.EVENT_DETAILS_URI

class WebFragment : Fragment() {

    companion object {
        private lateinit var progressBar: ProgressBar
        lateinit var webView: WebView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_web, container, false)
        webView = rootView.web_view
        webView.loadUrl(arguments?.getString(EVENT_DETAILS_URI))
        val settings = webView.settings
        settings.builtInZoomControls = false
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = true
        webView.clearHistory()
        webView.clearCache(true)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.webViewClient = ViewClient()

        progressBar = rootView.progress_bar

        // Inflate the layout for this fragment
        return rootView
    }

    inner class ViewClient : WebViewClient() {

        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }

    }
}
