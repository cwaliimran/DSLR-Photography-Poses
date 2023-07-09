package com.cwnextgen.dslrphotographyposes.utils

interface OnItemClick {
    fun onClick(position: Int, type: String? = "", data: Any? = null) {}
}