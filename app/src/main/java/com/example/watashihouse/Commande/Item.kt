package com.example.watashihouse.Commande

data class Item(
    val color: String,
    val description: String,
    val id: Int,
    val image1: String,
    val image2: String,
    val image3: String,
    val image4: Any,
    val name: String,
    val note: Double,
    val price: Int,
    val stock: Int
)