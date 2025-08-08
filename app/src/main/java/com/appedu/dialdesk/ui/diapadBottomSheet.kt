package com.appedu.dialdesk.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class diapadBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your existing layout
        return inflater.inflate(R.layout.dialog_custom_dailpad, container, false)
    }

    override fun onStart() {
        super.onStart()

        // Get the bottom sheet view's parent (the actual BottomSheet container)
        val view = getView()
        if (view == null) return

        val parent = view.getParent() as View // parent is the bottom sheet container
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<View?>(parent)

        // Expand the sheet when it opens (optional)
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED)

        // Optional: prevent dragging off (if desired)
         behavior.setDraggable(false); // requires material lib that supports this method
    }

    companion object {
        fun newInstance(): diapadBottomSheet {
            return diapadBottomSheet()
        }
    }
}
