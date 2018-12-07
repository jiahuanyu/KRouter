package me.jiahuan.android.krouter.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import me.jiahuan.android.krouter.annotation.Consts
import me.jiahuan.android.krouter.annotation.RouteMeta
import me.jiahuan.android.krouter.annotation.RouteType

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
        private var mContext: Context? = null
        private var mFlag: Int? = null
        private var mBundle: Bundle? = null

        fun withContext(context: Context): KRouterBuilder {
            mContext = context
            return this
        }

        fun withFlag(flag: Int): KRouterBuilder {
            mFlag = flag
            return this
        }

        fun withBundle(bundle: Bundle): KRouterBuilder {
            mBundle = bundle
            return this
        }

        fun request() {
            val routeMeta = mRouteMap[mPath]
            if (routeMeta != null) {
                when (routeMeta.routeType) {
                    RouteType.ACTIVITY -> {
                        mContext?.let { context ->
                            val intent = Intent(context, routeMeta.clazz)
                            mFlag?.let { flag ->
                                intent.setFlags(flag)
                            }
                            mBundle?.let { bundle ->
                                intent.putExtras(bundle)
                            }
                            context.startActivity(intent)
                        }
                    }
                    RouteType.SERVICE -> {
                        mContext?.let { context ->
                            val intent = Intent(context, routeMeta.clazz)
                            mBundle?.let { bundle ->
                                intent.putExtras(bundle)
                            }
                            context.startService(intent)
                        }
                    }
                    else -> {
                        Log.d(TAG, "else")
                    }
                }
            }
        }
    }
}