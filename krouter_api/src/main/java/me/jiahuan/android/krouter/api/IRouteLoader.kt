package me.jiahuan.android.krouter.api

import me.jiahuan.android.krouter.annotation.RouteMeta

interface IRouteLoader {
    fun loadInto(map: MutableMap<String, RouteMeta>)
}