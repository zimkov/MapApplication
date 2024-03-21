package com.example.retrofitproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.osmdroid.util.GeoPoint

class ModalBottomSheet : BottomSheetDialogFragment() {

    var name: String = ""
    lateinit var geoPoint: GeoPoint
    override fun getTheme() = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var ds = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)
        ds.findViewById<TextView>(R.id.createText).text = name
        ds.findViewById<TextView>(R.id.textView1).text = geoPoint.toString()
        return ds
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    fun setData(geoPoint: GeoPoint, name: String){
    }

}