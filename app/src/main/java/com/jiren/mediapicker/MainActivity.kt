package com.jiren.mediapicker

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.jiren.mediapicker.adapters.TabAdapter


fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}

class MainActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPager = findViewById(R.id.viewPager)
        val pagerAdapter = TabAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter
        mPager.offscreenPageLimit = 4

        findViewById<RelativeLayout>(R.id.btnImages).setOnClickListener(View.OnClickListener {
            mPager.currentItem = 0
        })

        findViewById<RelativeLayout>(R.id.btnVideos).setOnClickListener(View.OnClickListener {
            mPager.currentItem = 1
        })

        findViewById<RelativeLayout>(R.id.btnMusic).setOnClickListener(View.OnClickListener {
            mPager.currentItem = 2
        })

        findViewById<RelativeLayout>(R.id.btnOthers).setOnClickListener(View.OnClickListener {
            mPager.currentItem = 3
        })
        setupPermissions()
    }

    /**
     * Metodo para pedir permisos necesarios para ller los archivos
     */
    private fun setupPermissions() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionCheck2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100
            )
        }

        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
    }

    /**
     * Recargo los tabs si el usuario acepta los permisos
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100, 101 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    val pagerAdapter = TabAdapter(supportFragmentManager)
                    mPager.adapter = pagerAdapter
                    mPager.offscreenPageLimit = 4
                } else {


                    Toast.makeText(
                        this@MainActivity,
                        "Permission denied to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

}