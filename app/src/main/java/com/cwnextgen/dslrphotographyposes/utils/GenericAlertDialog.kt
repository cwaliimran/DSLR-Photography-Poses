package com.cwnextgen.dslrphotographyposes.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.cwnextgen.dslrphotographyposes.R
import com.cwnextgen.dslrphotographyposes.databinding.DialogGenericBinding

fun Context.genericDialog(
    listener: AlertDialogListener,
    title: String,
    message: String,
    data: Any? = null,
    cancelable: Boolean? = false,
    hideNoBtn: Boolean? = false,
    yesBtnText: String? = getString(R.string.yes),
    noBtnText: String? = getString(R.string.no),
) {
    val dialog = Dialog(this)
    val layoutInflater = LayoutInflater.from(this)
    val binding = DialogGenericBinding.inflate(layoutInflater)
    binding.tvTitle.text = title
    binding.tvMessage.text = message
    binding.btnyes.text = yesBtnText.toString()
    binding.btnno.text = noBtnText.toString()
    if (hideNoBtn == true) {
        binding.btnno.visibility = View.GONE
        if (yesBtnText == getString(R.string.yes)) {
            binding.btnyes.text = getString(R.string.done)
        }
    }
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCancelable(cancelable!!)
    if (dialog.isShowing) return
    binding.btnno.setOnClickListener {
        dialog.dismiss()
        listener.onNoClick(data)
    }
    binding.btnyes.setOnClickListener {
        dialog.dismiss()
        listener.onYesClick(data)
    }
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.show()
}
