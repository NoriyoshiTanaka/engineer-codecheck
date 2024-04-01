package jp.co.yumemi.android.code_check.dataClasses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("avatar_url")
    val avatarUrl: String,
)