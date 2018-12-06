package me.jiahuan.android.krouter.annotation

data class RouteMeta(
    val routeType: RouteType = RouteType.UNKNOWN,
    val path: String = "",
    val clazz: Class<*> = Any::class.java
)