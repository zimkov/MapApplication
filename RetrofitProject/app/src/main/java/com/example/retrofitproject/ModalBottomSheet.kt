package com.example.retrofitproject

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.retrofitproject.DataClasses.Comment
import com.example.retrofitproject.DataClasses.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.osmdroid.util.GeoPoint
import com.example.retrofitproject.Product.ProductApi
import com.example.retrofitproject.adapter.CommentAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class ModalBottomSheet : BottomSheetDialogFragment() {

    lateinit var user: User
    var mapObjectId: Int = 0
    var commentList: List<Comment> = ArrayList<Comment>()
    var name: String = ""
    lateinit var geoPoint: GeoPoint
    var address: String = ""
    var rating: Float = 0.0f
    var durationRoad = 0
    var lengthRoad = 0
    lateinit var image: Image
    private lateinit var adapter: CommentAdapter

    override fun getTheme() = R.style.AppBottomSheetDialogTheme
    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = CommentAdapter()

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
        ds.findViewById<RecyclerView>(R.id.rcView).setHasFixedSize(true)
        ds.findViewById<RecyclerView>(R.id.rcView).layoutManager = LinearLayoutManager(this.context)
        ds.findViewById<RecyclerView>(R.id.rcView).adapter = adapter
        adapter.submitList(commentList)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient;
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://194.26.138.65:5000/").client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val productApi = retrofit.create(ProductApi::class.java)


        val commentText = ds.findViewById<EditText>(R.id.commentField)
        ds.findViewById<Button>(R.id.sendComment).setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                productApi.addComment(commentText.text.toString(), user.email ,mapObjectId)
                commentText.text.clear()
            }
        }



        ds.findViewById<TextView>(R.id.createText).text = name
        ds.findViewById<TextView>(R.id.textView2).text = "Расстояние: " + lengthRoad + "м Время в пути: " + durationRoad/60 +"мин"
        ds.findViewById<TextView>(R.id.textView1).text = "Адрес: " + address
        ds.findViewById<RatingBar>(R.id.ratingBar).rating = rating
        ds.findViewById<RatingBar>(R.id.ratingBar).stepSize = .5f
        ds.findViewById<RatingBar>(R.id.ratingBar).invalidate()





        ds.findViewById<Button>(R.id.buttonGo).setOnClickListener {
            this.dismiss()
        }

        return ds
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }



}