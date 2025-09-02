package co.seam.seamcomponents.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.SeamTopBar

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SeamOtpView(
    otpUrl: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SeamTopBar(
                title = stringResource(R.string.otp_title),
                onNavigateBack = onNavigateBack
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            builtInZoomControls = true
                            displayZoomControls = false
                        }
                        webViewClient = WebViewClient()
                        loadUrl(otpUrl)
                    }
                },
                update = { webView ->
                    webView.loadUrl(otpUrl)
                },
            )
        }
    }
}
