package com.example.android10_11

import android.annotation.SuppressLint
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var cameraOpenId:Button
    private lateinit var browserOpenId:Button
    private lateinit var fileOpenId: Button
    lateinit var clickImageId:ImageView
    private val fileRequestCode= 456
    private val fileExplorerPermissionRequestCode = 7
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraOpenId=findViewById(R.id.camera_button)
        browserOpenId=findViewById(R.id.browser_button)
        fileOpenId=findViewById(R.id.file_button)
        clickImageId=findViewById(R.id.click_image)
        registerPermission()
        checkPermission()
        cameraOpenId.setOnClickListener {
            val cameraIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(cameraIntent, picture_id)
        }

        browserOpenId.setOnClickListener {
            val intentBrowser = Intent(this, Browser::class.java)
            startActivity(intentBrowser)
        }

        fileOpenId.setOnClickListener {
            openFileExplorer()
        }
    }

    private fun openFileExplorer() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, fileRequestCode)
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED->{

                    }

            else->{
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }


    }

    private fun registerPermission(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this, "Разрешения получены", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Запрет на разрешения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== picture_id){
            val photo=data!!.extras!!["data"] as Bitmap
            clickImageId.setImageBitmap(photo)
        }
        if(requestCode==fileRequestCode && resultCode== RESULT_OK){
            val uri: Uri? = data?.data
            if(uri != null){
                Toast.makeText(this, "Выбран файл:$uri", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object{
        private const val picture_id=123
    }
}