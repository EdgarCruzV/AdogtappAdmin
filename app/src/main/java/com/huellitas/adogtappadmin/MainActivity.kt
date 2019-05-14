package com.huellitas.adogtappadmin


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.huellitas.adogtappadmin.MenuFragments.*
import com.huellitas.adogtappadmin.MenuFragments.BajaExtras.BajaExtras
import com.huellitas.adogtappadmin.MenuFragments.BajaPaseadores.BajaPaseadores
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritos
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTienda
import kotlinx.android.synthetic.main.nav_header.*
import java.util.*


class MainActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    lateinit var _db: DatabaseReference
    lateinit var uid: String

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        loadUserInformation()
        when (p0.getItemId()) {
            R.id.layout_perritos -> {
                drawer.closeDrawers()
                val adoptarFragment = Perritos.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Alta de Perritos", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_baja_perritos -> {
                drawer.closeDrawers()
                val adoptarFragment = BajaPerritos.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Baja de Perritos", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_tienda -> {
                drawer.closeDrawers()
                val adoptarFragment = Tienda.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Alta de Artículos de Tienda", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_baja_tienda -> {
                drawer.closeDrawers()
                val adoptarFragment = BajaTienda.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Baja de Artículos de Tienda", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_actualizacion_datos -> {
                drawer.closeDrawers()
                val adoptarFragment = ActualizacionDatos.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Actualización de Datos de la Cuenta", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_extras -> {
                drawer.closeDrawers()
                val adoptarFragment = Extras.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Alta de Artículos de Extras", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_baja_extras -> {
                drawer.closeDrawers()
                val adoptarFragment = BajaExtras.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Baja de Artículos de Extras", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_paseadores -> {
                drawer.closeDrawers()
                val adoptarFragment = Paseadores.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Alta de Paseadores", Toast.LENGTH_SHORT).show()
            }
            R.id.layout_baja_paseadores -> {
                drawer.closeDrawers()
                val adoptarFragment = BajaPaseadores.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();
                Toast.makeText(this, "Baja de Paseadores", Toast.LENGTH_SHORT).show()
            }

        }
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle: Bundle? = intent.extras
        val myArray: ArrayList<String>? = intent.getStringArrayListExtra("myArray")

        //Para el menu
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        var navigationView: NavigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        checkUserLogged()
        loadUserInformation()
        val fragment = Perritos.newInstance();
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkUserLogged() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null || uid != "Zmm2TSfx8Xc8fVuZOins5sEsYX92") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun loadUserInformation() {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = mAuth.getCurrentUser()
        _db = FirebaseDatabase.getInstance().getReference("/users")

        if (user != null) {
            if (user!!.uid != null) {
                uid = user!!.uid
            }
            if (user!!.email != null) {
                //var displayName : String = user!!.getDisplayName()!!
                nav_view.getHeaderView(0).mail_text.text = user!!.email
            }
            val ref = FirebaseDatabase.getInstance().reference
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Code
                    val userName = dataSnapshot.child("users")
                        .child("Administrator").child("username").value.toString()
                    if (userName != "null") {
                        nav_view.getHeaderView(0).name_text.text = userName
                    }
                    val userPhotoUrl = dataSnapshot.child("users").child("Administrator")
                        .child("profileImageUrl").value.toString()
                    if (userPhotoUrl != "null") {
                        profile_pic.setBackgroundResource(0)
                        Glide.with(applicationContext)
                            .load(userPhotoUrl)
                            .into(nav_view.getHeaderView(0).profile_pic)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Code
                }
            })
        }

    }


}

data class User(
    var uid: String = "",
    val username: String = "",
    val image: String = ""
)


