package me.jiahuan.android.krouter.annotation

enum class RouteType(val className: String) {
    UNKNOWN(""),
    ACTIVITY("android.app.Activity"),
    SERVICE("android.app.Service")
}