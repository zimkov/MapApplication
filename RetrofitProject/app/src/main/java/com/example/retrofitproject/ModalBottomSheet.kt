package com.example.retrofitproject

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.osmdroid.util.GeoPoint

class ModalBottomSheet : BottomSheetDialogFragment() {

    var name: String = ""
    lateinit var geoPoint: GeoPoint
    var address: String = ""
    lateinit var image: Image
    override fun getTheme() = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ds = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)

        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(SlideModel("https://www.sstu.ru/upload/iblock/493/1024-_1_.jpg"))
        imageList.add(SlideModel("https://www.sstu.ru/upload/iblock/44b/foto.jpg"))
        imageList.add(SlideModel("https://www.sstu.ru/upload/iblock/522/DJI_0049.jpg"))

        val imageSlider = ds.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)



        ds.findViewById<TextView>(R.id.createText).text = name
        ds.findViewById<TextView>(R.id.textView2).text = "Координаты: " + geoPoint.toString()
        ds.findViewById<TextView>(R.id.textView1).text = "Адрес: " + address
        return ds
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}