package me.jiahuan.android.krouter.api

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.util.Log
import me.jiahuan.android.krouter.annotation.Consts
import me.jiahuan.android.krouter.annotation.RouteMeta
import me.jiahuan.android.krouter.annotation.RouteType

class KRouter private constructor() {
    companion object {
        private const val TAG = "KRouter"

        private val mRouteMap = HashMap<String, RouteMeta>()

        fun create(path: String): KRouterContextBuilder {
            return KRouterContextBuilder(path)
        }

        fun initialize(vararg moduleName: String) {
            moduleName.forEach {
                val clazz = Class.forName("${Consts.PACKAGE}.${Consts.AUTO_GENERATOR_CLASS_PREFIX}$it")
                val obj = clazz.newInstance() as IRouteLoader
                obj.loadInto(mRouteMap)
            }
        }
    }


    class KRouterContextBuilder(private val mPath: String) {
        fun withContext(context: Context): KRouterBuilder {
            return KRouterBuilder(mPath, context)
        }
    }

    class KRouterBuilder(private val mPath: String, private val mContext: Context) {
        private var mFlag: Int? = null
        private val mBundle = Bundle()
        private var mServiceConnection: ServiceConnection? = null
        private var mBindWithStartService = false
        private var mIntentAction: String? = null

        fun withFlag(flag: Int): KRouterBuilder {
            mFlag = flag
            return this
        }

        fun withBundle(bundle: Bundle): KRouterBuilder {
            mBundle.putAll(bundle)
            return this
        }

        fun withString(key: String, value: String): KRouterBuilder {
            mBundle.putString(key, value)
            return this
        }

        fun withInt(key: String, value: Int): KRouterBuilder {
            mBundle.putInt(key, value)
            return this
        }

        fun withServiceConnection(conn: ServiceConnection, startService: Boolean = false): KRouterBuilder {
            mServiceConnection = conn
            mBindWithStartService = startService
            return this
        }

        fun withAction(action: String): KRouterBuilder {
            mIntentAction = action
            return this
        }

        fun request() {
            val routeMeta = mRouteMap[mPath]
            if (routeMeta != null) {
                val intent = Intent(mContext, routeMeta.clazz)
                intent.putExtras(mBundle)
                mIntentAction?.let {
                    intent.action = it
                }
                when (routeMeta.routeType) {
                    RouteType.ACTIVITY -> {
                        mFlag?.let { flag ->
                            intent.setFlags(flag)
                        }
                        mContext.startActivity(intent)
                    }
                    RouteType.RECEIVER -> {
                        mContext.sendBroadcast(intent)
                    }
                    RouteType.SERVICE -> {
                        if (mServiceConnection != null) {
                            mServiceConnection?.let {
                                mContext.bindService(intent, it, Context.BIND_AUTO_CREATE)
                                if (mBindWithStartService) {
                                    mContext.startService(intent)
                                }
                            }
                        } else {
                            mContext.startService(intent)
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