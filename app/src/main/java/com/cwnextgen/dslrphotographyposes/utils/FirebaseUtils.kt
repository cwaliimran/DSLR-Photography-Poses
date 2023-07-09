package com.cwnextgen.dslrphotographyposes.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


fun firestore(): FirebaseFirestore {
    return FirebaseFirestore.getInstance()
}

fun auth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}

fun storage(): FirebaseStorage {
    return FirebaseStorage.getInstance()
}


fun generateUUID(): String {
    val uuid = UUID.randomUUID()
    return uuid.toString()
}