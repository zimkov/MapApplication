package com.example.retrofitproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitproject.DataClasses.Comment
import com.example.retrofitproject.DataClasses.MapObject
import com.example.retrofitproject.DataClasses.SocialMapObject
import com.example.retrofitproject.DataClasses.User
import com.example.retrofitproject.Product.ProductApi
import com.example.retrofitproject.adapter.CommentAdapter
import com.example.retrofitproject.adapter.MapObjectAdapter
import com.example.retrofitproject.ui.theme.RetrofitProjectTheme
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
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

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    @SuppressLint("SuspiciousIndentation")
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView
    private val startPoint = GeoPoint( 51.5406, 46.0086)
    private lateinit var pointList: ArrayList<MapObject>
    private lateinit var locationOverlay: MyLocationNewOverlay
    private val modalBottomSheet = ModalBottomSheet()

    lateinit var user : User
    private lateinit var adapter: MapObjectAdapter
    private lateinit var  drawerLayout: DrawerLayout



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.main_screen)
        drawerLayout = findViewById(R.id.drawer_layout)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)

        }


        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        val username = navigationView.getHeaderView(0).findViewById<TextView>(R.id.userName)
        val email = navigationView.getHeaderView(0).findViewById<TextView>(R.id.email)
        user = intent.extras?.get("user") as User
        username.text = user.name
        email.text = user.email


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

        CoroutineScope(Dispatchers.IO).launch {
            val socialMapObject = productApi.getSocialMapObject()
            pointList = ArrayList<MapObject>()
            runOnUiThread {
                socialMapObject.forEach {
                    pointList.add(MapObject(it.id, it.display_name, GeoPoint(it.x, it.y), 4f, it.adress, "Просто", it.availability))
                }

                pointList.forEach{
                    setMarker(it.id, it.geoPoint, it.display_name, it.address, it.rating, it.availability)
                }

            }

        }

        //var mapObjectList = ArrayList<MapObject>()

        //поиск
        adapter = MapObjectAdapter()
        findViewById<RecyclerView>(R.id.searchList).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.searchList).adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    var list: List<SocialMapObject>? = emptyList()
                    if(newText != null && newText != ""){
                        list = productApi.searchSocialMapObject(newText)
                    }else{
                        list = emptyList()
                    }

                    runOnUiThread {
                        adapter.submitList(list)
                    }

                }
                return true
            }
        })





        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        locationOverlay.enableMyLocation()
        map.overlays.add(this.locationOverlay)


        findViewById<ImageButton>(R.id.locationButton).setOnClickListener {
            mapController.setCenter(locationOverlay.myLocation)
            mapController.setZoom(20)
            Toast.makeText(this,"Текущее местоположение", Toast.LENGTH_SHORT).show()
        }



    }
    //Боковое меню
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_settings -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_info -> startActivity(Intent(this, InfoActivity::class.java))
            R.id.nav_share -> Toast.makeText(this,"Поделиться", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> {
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Выход из аккаунта", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }





    var lengthRoad = 0
    var durationRoad = 0

    //Ставит маркер на введенные координаты, присваивает имя и другие данные
    private fun setMarker(id: Int, geoPoint: GeoPoint, name: String, adress: String, rating: Float, availability: String){
        var marker = Marker(map)
        marker.position = geoPoint
        marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.marker_default)
        marker.title = name
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        //Появление карточки при нажати на объект (Обработка нажатия на маркер)
        marker.setOnMarkerClickListener { marker, mapView ->
            buildRoad(geoPoint)

            lateinit var commentList : ArrayList<Comment>
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

            CoroutineScope(Dispatchers.IO).launch {
                val comments = productApi.getComments(id)
                commentList = ArrayList()
                runOnUiThread {
                    //Toast.makeText(this@MainActivity, comments[0].Text+"cdf", Toast.LENGTH_SHORT).show()
                    comments.forEach {
                        commentList.add(Comment(it.id, it.text, it.rate, it.userId, it.user, it.mapObjectId, it.mapObject))

                    }
                    modalBottomSheet.commentList = commentList

                }

            }

            modalBottomSheet.user = user
            modalBottomSheet.mapObjectId = id
            modalBottomSheet.name = name
            modalBottomSheet.rating = rating
            modalBottomSheet.availability = availability
            modalBottomSheet.geoPoint = geoPoint
            modalBottomSheet.address = adress
            modalBottomSheet.durationRoad = durationRoad
            modalBottomSheet.lengthRoad = lengthRoad
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
            return@setOnMarkerClickListener true
        }
        map.overlays.add(marker)
        map.invalidate()
    }


    fun buildRoad(endPoint: GeoPoint){
        map.overlays.removeAll { it is Polyline }
        CoroutineScope(Dispatchers.IO).launch{
            val roadManager = OSRMRoadManager(this@MainActivity, System.getProperty("http.agent"))
            roadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT)
            val wayPoints = arrayListOf<GeoPoint>(locationOverlay?.myLocation  ?:startPoint, endPoint)
            val road = roadManager.getRoad(wayPoints)
            val roadOverlay = RoadManager.buildRoadOverlay(road, Color.rgb(77,129,213), 20F)
            withContext(Dispatchers.Main){
                lengthRoad = (road.mLength * 1000).toInt()
                durationRoad = road.mDuration.toInt()
                map.overlays.add(0, roadOverlay)
                map.invalidate()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
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
            val sslContext =
                SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
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