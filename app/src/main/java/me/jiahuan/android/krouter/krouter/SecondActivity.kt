package me.jiahuan.android.krouter.krouter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import me.jiahuan.android.krouter.annotation.Route

@Route(path = "/main/second")
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_second)
        Toast.makeText(this, "${intent.getIntExtra("test", -1)}", Toast.LENGTH_LONG).show()
    }
}