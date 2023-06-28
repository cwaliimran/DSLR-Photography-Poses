package com.cwnextgen.quranislamicwallpaper.activities.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = "BaseActivity"
    var bundle: Bundle? = null
    val gson = Gson()
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // GlobalClass.updateStatusBar(window)
        context = this
//        if (AppClass.getCurrentUser() == null){
//            Toast.makeText(this, "Please login", Toast.LENGTH_LONG).show()
//            // TODO: start activity login
//        }else{
//            currentUser = AppClass.getCurrentUser()
//        }
        onCreate()
        initAdapter()
        initObservers()
        clicks()
        apiAndArgs()

        //  networkObserver()
    }

    abstract fun onCreate()
    open fun initAdapter() {}
    open fun initObservers() {}
    abstract fun clicks()
    open fun apiAndArgs() {}
//    private fun networkObserver() {
//        val cld = LiveDataInternetConnections(AppClass.instance)
//        cld.observe(this) { isConnected ->
//            if (isConnected) {
//                Handler(Looper.getMainLooper()).postDelayed({
//
//                }, 3000)
//
//            }
//        }
//    }


    fun hide(view: View) {
        view.visibility = View.INVISIBLE
    }

    fun hideGone(view: View) {
        view.visibility = View.GONE
    }

    fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
//        if (AppClass.getCurrentUser() == null){
//            Toast.makeText(this, "Please login", Toast.LENGTH_LONG).show()
//            // TODO: start activity login
//        }else{
//            currentUser = AppClass.getCurrentUser()
//        }
    }
}