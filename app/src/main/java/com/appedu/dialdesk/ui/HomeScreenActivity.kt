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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.appedu.dialdesk.R
import com.appedu.dialdesk.databinding.ActivityHomeScreenBinding
import com.google.android.material.button.MaterialButton


class HomeScreenActivity : AppCompatActivity() {

    private lateinit var _homeBinding: ActivityHomeScreenBinding
    private var titleText: AppCompatTextView? = null
    private var phoneNumber: AppCompatTextView? = null
    private var lastDialedNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _homeBinding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(_homeBinding.root)

        initVariables()
        initListeners()

        titleText?.setText(R.string.dialdesk)
    }

    private fun initVariables() {
        titleText = findViewById(R.id.tvScreenTitle)
        phoneNumber = findViewById(R.id.tvNumber)
    }

    private fun initListeners() {
        _homeBinding.fabDial.setOnClickListener {
            showDialPadDialog()
        }

        _homeBinding.bottomNav.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, VideoCallActivity::class.java))
        }
    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lastDialedNumber?.let { makeCall(it) }
        } else {
            Toast.makeText(
                this@HomeScreenActivity,
                "Permission denied to make calls",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun showDialPadDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_dialpad, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val numberView = dialogView.findViewById<AppCompatTextView>(R.id.tvNumber)
        val btnCall = dialogView.findViewById<AppCompatImageButton>(R.id.btnCall)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numberButtons) {
            val btn = dialogView.findViewById<MaterialButton>(id)
            btn.setOnClickListener {
                numberView.text = numberView.text.toString() + btn.text.toString()
            }
        }

        btnCall.setOnClickListener {
            val number = numberView.text.toString()
            if (number.isNotBlank()) {
                lastDialedNumber = number
                dialog.dismiss()
                makeCall(number)
            } else {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


    private fun getTextFromTextView(textView: AppCompatTextView?): String {
        return textView?.text?.toString()?.trim().orEmpty()
    }

    private fun videoCall(){

    }

    private fun voiceCall(){

    }
}