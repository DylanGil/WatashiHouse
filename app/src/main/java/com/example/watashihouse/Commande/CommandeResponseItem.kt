package com.example.watashihouse.Commande

data class CommandeResponseItem(
    val dateOfPurchase: String,
    val id: Int,
    val items: List<Item>,
    val number: String,
    val totalPrice: Int,
    val user: User
)