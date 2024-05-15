package com.example.retrofitproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.retrofitproject.DataClasses.MapObject
import com.example.retrofitproject.Product.ProductApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.osmdroid.util.GeoPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginText = findViewById<TextView>(R.id.loginText)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

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


        loginButton.setOnClickListener {


            CoroutineScope(Dispatchers.IO).launch {
                val user = productApi.getUserByEmail(username.text.toString())

                if(user==null){
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Такой пользователь незарегистрирован",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    runOnUiThread {
                        loginText.text = user.name
                        if(password.text.toString() == user.password){
                            finish()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)

                        }
                    }
                }


            }


        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}