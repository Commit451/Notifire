package com.commit451.notifire.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textPayload = findViewById<TextView>(R.id.text_payload)
        val buttonPrint = findViewById<View>(R.id.button_print)
        buttonPrint.setOnClickListener {
            Log.d("notifire", "Token: ${FirebaseInstanceId.getInstance().token}")
        }

        var string = ""
        if (intent.extras != null) {
            for (key in intent.extras.keySet()) {
                string += "Key: $key Value: ${intent.extras.getString(key)}\n"
            }
        }
        textPayload.text = string

    }
}
