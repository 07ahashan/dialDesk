package com.appedu.dialdesk.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.appedu.dialdesk.R
import com.appedu.dialdesk.databinding.ActivityHomeScreenBinding


class HomeScreenActivity : AppCompatActivity() {

    private lateinit var _homeBinding :ActivityHomeScreenBinding
    private var titleText :AppCompatTextView ?= null
    private var phoneNumber:AppCompatTextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _homeBinding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(_homeBinding.root)

        initVariables()
        initListeners()

        titleText?.setText(R.string.dialdesk)
    }

    private fun initVariables(){
        titleText = findViewById(R.id.tvScreenTitle)
        phoneNumber = findViewById(R.id.tvNumber)
    }

    private fun initListeners(){
        _homeBinding.fabDial.setOnClickListener {
            showDialPadDialog()
        }
    }

    private fun makeCall(){
        val number = getTextFromTextView(phoneNumber!!)
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           makeCall()
        } else {
            Toast.makeText(this, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showDialPadDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_dialpad, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()
    }

    private fun getTextFromTextView(textView: AppCompatTextView): String {
        return textView.text.toString()
    }

}