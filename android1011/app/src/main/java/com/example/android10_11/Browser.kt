package com.example.android10_11

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class Browser : AppCompatActivity() {
    private lateinit var webView : WebView
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        webView = findViewById(R.id.webView)
        registerPermission()
        checkPermission()
        webView.settings.javaScriptEnabled = true
        webView.webViewClient
        webView.loadUrl("http://www.sapr.tstu.ru")
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) ==
                    PackageManager.PERMISSION_GRANTED->{

                    }

            else->{
                permissionLauncher.launch(android.Manifest.permission.INTERNET)
            }
        }


    }

    private fun registerPermission(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this, "Интернет работает", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Интернет не работает", Toast.LENGTH_SHORT).show()
            }
        }
    }
}