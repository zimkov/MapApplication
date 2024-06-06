package com.example.retrofitproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.retrofitproject.DataClasses.User
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {

    lateinit var user : User
    private lateinit var  drawerLayout: DrawerLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


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

        val username = navigationView.getHeaderView(0).findViewById<TextView>(R.id.userName)
        val email = navigationView.getHeaderView(0).findViewById<TextView>(R.id.email)
        user = intent.extras?.get("user") as User
        username.text = user.name
        email.text = user.email

        val profileName = findViewById<TextView>(R.id.profileName)
        val category = findViewById<TextView>(R.id.categoryUser)

        profileName.text = user.name
        category.text = "Категория инвалидности: " + user.type

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> finish()
            R.id.nav_settings -> Toast.makeText(this,"Настройки", Toast.LENGTH_SHORT).show()
            R.id.nav_info -> {
                finish()
                startActivity(Intent(this, InfoActivity::class.java))
            }
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



}