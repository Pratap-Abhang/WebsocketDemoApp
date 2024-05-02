package com.pratapabhang.websocketexample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pratapabhang.websocketexample.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


const val WEBSOCKET_KEY = "<Websocket Api Key from https://piehost.com/>"

class MainActivity : AppCompatActivity(), WebsocketInterface {


    private lateinit var binding : ActivityMainBinding
    private lateinit var webSocketListener : WebSocketListener
    private lateinit var okHttpClient : OkHttpClient
    private var webSocket: WebSocket? = null
    private var socketStatus = false
    private var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()


        binding.btnConnect.setOnClickListener {
            webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        }
        binding.btnDisconnect.setOnClickListener {
            webSocket?.close(1000, "Cancelled Manually!")
        }

        binding.btnSend.setOnClickListener {
            message = binding.editText.text.toString()
            webSocket?.send(message)
            this.addMessage(Pair(true, message))
            binding.editText.setText("")
        }

    }


    fun initView(){
        webSocketListener = WebSocketListener(this)
        okHttpClient = OkHttpClient()
    }

    private fun createRequest() : Request {
        val socketUrl = "wss://free.blr2.piesocket.com/v3/1?api_key=${WEBSOCKET_KEY}&notify_self=1"

        return Request.Builder()
            .url(socketUrl)
            .build()

    }

    override fun setStatus(status: Boolean) {
        socketStatus = status
    }

    override fun addMessage(data: Pair<Boolean, String>) {
        if(socketStatus){
            message = data.second
        }
        var mMsg = binding.txtData.text.toString()
        binding.txtData.setText("$mMsg \n$message")

    }
}