package com.example.watashihouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShoppingCartUserResponse {
    @SerializedName("id")
    @Expose
    var id: Int?= null

    @SerializedName("price")
    @Expose
    var price: Int?= null

    @SerializedName("items")
    @Expose
    var items: List<Item>?= null

    class Item{

        @SerializedName("id")
        @Expose
        var id: Int?= null

        @SerializedName("name")
        @Expose
        var name: String?= null

        @SerializedName("description")
        @Expose
        var description: String?= null

        @SerializedName("image1")
        @Expose
        var image1: String?= null

        @SerializedName("image2")
        @Expose
        var image2: String?= null

        @SerializedName("image3")
        @Expose
        var image3: String?= null

        @SerializedName("image4")
        @Expose
        var image4: String?= null

        @SerializedName("color")
        @Expose
        var color: String?= null

        @SerializedName("price")
        @Expose
        var price: Int?= null

        @SerializedName("stock")
        @Expose
        var stock: Int?= null

    }
}