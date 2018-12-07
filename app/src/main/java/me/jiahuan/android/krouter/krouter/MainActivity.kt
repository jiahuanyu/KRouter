package me.jiahuan.android.krouter.krouter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_activity_main.*
import me.jiahuan.android.krouter.annotation.Route
import me.jiahuan.android.krouter.api.KRouter

@Route(path = "/main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_main)
        KRouter.initialize("app")
        id_activity_main_go_button.setOnClickListener {
            KRouter
                .create("/main/second")
                .withContext(this)
                .request()
        }
    }
}