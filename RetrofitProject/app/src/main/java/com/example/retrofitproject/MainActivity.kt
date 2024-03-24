package com.example.retrofitproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.retrofitproject.DataClasses.MapObject
import com.example.retrofitproject.Product.ProductApi
import com.example.retrofitproject.ui.theme.RetrofitProjectTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : AppCompatActivity(){
    @SuppressLint("SuspiciousIndentation")
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView
    private val startPoint = GeoPoint( 51.5406, 46.0086)
    private var pointList: List<MapObject> = listOf()
    private lateinit var locationOverlay: MyLocationNewOverlay
    private val modalBottomSheet = ModalBottomSheet()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val home = findViewById<LinearLayout>(R.id.home)
        val info = findViewById<LinearLayout>(R.id.info)
        val exit = findViewById<LinearLayout>(R.id.exit)
        val settings = findViewById<LinearLayout>(R.id.settings)
        val share = findViewById<LinearLayout>(R.id.share)

        val imageView = findViewById<ImageView>(R.id.menu)



        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient;
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7017/").client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val productApi = retrofit.create(ProductApi::class.java)
            /*
            CoroutineScope(Dispatchers.IO).launch {
                val product = productApi.getUser()
                runOnUiThread {


                    setContent {
                        RetrofitProjectTheme {
                            // A surface container using the 'background' color from the theme
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                Greeting(product)
                            }
                        }
                    }
                }
            }
            */


        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(R.layout.main_screen)
        map = findViewById<MapView>(R.id.mapview)
        map.setTileSource(TileSourceFactory.MAPNIK)

        //Зум и старотовая точка
        val mapController = map.controller
        mapController.setZoom(15)
        mapController.setCenter(startPoint)
        //Включение жестов поворота
        val rotationGestureOverlay = RotationGestureOverlay(map)
        rotationGestureOverlay.isEnabled
        map.setMultiTouchControls(true)
        map.overlays.add(rotationGestureOverlay)

        initPointList()
        pointList.forEach{
            setMarker(it.geoPoint, it.display_name, it.address)
        }
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        locationOverlay.enableMyLocation()
        map.overlays.add(this.locationOverlay)


    }

    private fun initPointList() {
        var id = 0
        pointList = listOf(
            MapObject(id++, "ТЮЗ",GeoPoint(51.531498,46.0149078), address = "ул. Киселева, 1, Ю.П"),
            MapObject(id++, "Крытый рынок",GeoPoint(51.5318644,46.0220545), address = "Саратов, Саратовская обл., 410056"),
            MapObject(id++, "Лицей №3 им АС Пушкина",GeoPoint(51.5295652,46.0204048), address = "ул. Советская, 46")
        )
    }

    //Ставит маркер на введенные координаты, присваивает имя и другие данные
    private fun setMarker(geoPoint: GeoPoint, name: String, adress: String){
        var marker = Marker(map)
        marker.position = geoPoint
        marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.marker_default)
        marker.title = name
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        //Появление карточки при нажати на объект (Обработка нажатия на маркер)
        marker.setOnMarkerClickListener { marker, mapView ->
            modalBottomSheet.name = name
            modalBottomSheet.geoPoint = geoPoint
            modalBottomSheet.address = adress
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
            //Toast.makeText(this, marker.title, Toast.LENGTH_SHORT).show()
            return@setOnMarkerClickListener true
        }
        map.overlays.add(marker)
        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        closeDrawer(drawerLayout = findViewById(R.id.drawerLayout))
    }


    fun openDrawer(drawerLayout: DrawerLayout){
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer(drawerLayout: DrawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }
}




object UnsafeOkHttpClient {
    val unsafeOkHttpClient: OkHttpClient
        get() = try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            // Install the all-trusting trust manager
            val sslContext =
                SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitProjectTheme {
        Greeting("Android")
    }
}