package com.cwnextgen.dslrphotographyposes.activities.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson

abstract class BaseFragment : Fragment() {

    private val TAG = "BaseFragment"
    var fcmToken = ""
    private var mLastClickTime: Long = 0
    var bundle: Bundle? = null
    val gson = Gson()
    var lastClickTime: Long = 0

//    var currentUser : ModelUser? = null

    protected val mContext: Context by lazy {
        requireActivity()
    }

    protected val fragmentActivity: FragmentActivity by lazy {
        requireActivity()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (AppClass.getCurrentUser() == null){
//            Toast.makeText(requireContext(), "Please login", Toast.LENGTH_LONG).show()
//            // TODO: start activity login
//        }else{
//            currentUser = AppClass.getCurrentUser()
//        }
        viewCreated()
        initAdapter()
        initObservers()
        clicks()
        apiAndArgs()

    }

    abstract fun viewCreated()
    abstract fun clicks()
    open fun initAdapter() {}
    open fun initObservers() {}
    open fun apiAndArgs() {}

    protected fun show(view: View) {
        view.visibility = VISIBLE
    }

    protected fun hideGone(view: View) {
        view.visibility = GONE
    }

    protected fun hideInvisible(view: View) {
        view.visibility = INVISIBLE
    }

    override fun onResume() {
        super.onResume()
//        if (AppClass.getCurrentUser() == null){
//           Toast.makeText(requireContext(), "Please login", Toast.LENGTH_LONG).show()
//            // TODO: start activity login
//        }else{
//            currentUser = AppClass.getCurrentUser()
//        }
    }
}