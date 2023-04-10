package com.thus.catsfacts.model

import com.squareup.moshi.Json

data class CatRandomFact(
    @field:Json(name = "createdAt")
    val createdAt: String = "",
    @field:Json(name = "deleted")
    val deleted: Boolean = false,
    @field:Json(name = "_id")
    val id: String = "",
    @field:Json(name = "status")
    val status: Status = Status(),
    @field:Json(name = "text")
    val text: String = "",
    @field:Json(name = "type")
    val type: String = "",
    @field:Json(name = "updatedAt")
    val updatedAt: String = "",
    @field:Json(name = "user")
    val user: String = "",
    @field:Json(name = "__v")
    val v1: Int = 0
)

data class Status(
    @field:Json(name = "sentCount")
    val sentCount: Int = 0,
    @field:Json(name = "verified")
    val verified: Any = Any()
)