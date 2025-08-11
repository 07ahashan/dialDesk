package com.appedu.dialdesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appedu.dialdesk.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialpadBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.dialog_custom_dialpad,
            container, false
        )
        return v
    }
}