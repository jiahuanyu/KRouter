package me.jiahuan.android.krouter.api

import android.content.Context
import android.content.Intent
import me.jiahuan.android.krouter.annotation.Consts
import me.jiahuan.android.krouter.annotation.RouteMeta

class KRouter private constructor() {
    companion object {
        private const val TAG = "KRouter"

        private val mRouteMap = HashMap<String, RouteMeta>()

        fun create(path: String): KRouterBuilder {
            return KRouterBuilder(path)
        }

        fun initialize(vararg moduleName: String) {
            moduleName.forEach {
                val clazz = Class.forName("${Consts.PACKAGE}.${Consts.AUTO_GENERATOR_CLASS_PREFIX}$it")
                val obj = clazz.newInstance() as IRouteLoader
                obj.loadInto(mRouteMap)
            }
        }
    }


    class KRouterBuilder(val mPath: String) {
        private lateinit var mContext: Context

        fun withContext(context: Context): KRouterBuilder {
            mContext = context
            return this
        }

        fun request() {
            val routeMeta = mRouteMap[mPath]
            if (routeMeta != null) {
                mContext.startActivity(Intent(mContext, routeMeta.clazz))
            }
        }
    }
}