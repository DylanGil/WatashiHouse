package com.example.watashihouse.API

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommandeRequest {
    @SerializedName("user")
    @Expose
    var user: CommandeRequestId? = null

    @SerializedName("items")
    @Expose
    var items: List<CommandeRequestId>? = null
}