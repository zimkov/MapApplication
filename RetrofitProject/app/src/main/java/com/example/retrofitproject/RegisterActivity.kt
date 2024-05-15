package com.example.retrofitproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.retrofitproject.DataClasses.RegisterBody
import com.example.retrofitproject.DataClasses.User
import com.example.retrofitproject.Product.ProductApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val name = findViewById<EditText>(R.id.name)
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
               // val user = RegisterBody(name.text.toString(), 1, username.text.toString(), password.text.toString())
                productApi.addUser(name.text.toString(), 1, username.text.toString(), password.text.toString())

                val userCheck = productApi.getUserByEmail(username.text.toString())
                if(userCheck!=null){
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Регистрация прошла успешно",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        //startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    }
                }


            }


        }

        registerButton.setOnClickListener {
            finish()
        }

    }
}