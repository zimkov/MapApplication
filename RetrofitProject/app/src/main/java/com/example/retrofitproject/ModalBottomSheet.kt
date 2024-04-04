package com.example.retrofitproject

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
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
    var rating: Float = 0.0f
    lateinit var image: Image

    override fun getTheme() = R.style.AppBottomSheetDialogTheme
    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ds = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)

        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel("https://cdn2.tu-tu.ru/image/pagetree_node_data/1/a2a56dac4560eb9f3582303c4d3ff265/"))
        imageList.add(SlideModel("https://www.sstu.ru/upload/iblock/44b/foto.jpg"))
        imageList.add(SlideModel("https://www.sstu.ru/upload/iblock/522/DJI_0049.jpg"))

        val imageSlider = ds.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)


        ds.findViewById<Button>(R.id.button1).setOnClickListener {
            val intent = Intent(requireContext(), AddInfoActivity::class.java)
            intent.putExtra("item", name)
            startActivity(intent)
        }

        ds.findViewById<TextView>(R.id.createText).text = name
        ds.findViewById<TextView>(R.id.textView2).text = "Координаты: " + geoPoint.toString()
        ds.findViewById<TextView>(R.id.textView1).text = "Адрес: " + address
        ds.findViewById<RatingBar>(R.id.ratingBar).rating = rating
        ds.findViewById<RatingBar>(R.id.ratingBar).stepSize = .5f
        ds.findViewById<RatingBar>(R.id.ratingBar).invalidate()

        return ds
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}