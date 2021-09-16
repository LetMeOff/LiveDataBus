package com.zjh.livedatabus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zjh.livedatabus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            LiveDataBus.instance.event(LiveEvents::class.java).stickyEvent().post("这是一个粘性事件")
        }

        binding.toOneBtn.setOnClickListener {
            startActivity(Intent(this, OneActivity::class.java))
        }
        binding.toTwoBtn.setOnClickListener {
            startActivity(Intent(this, TwoActivity::class.java))
        }

        initObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        //字符串事件监听
        LiveDataBus.instance.event("stringEvent").observer(this, object : ObserverWrapper<Any>() {
            override fun onChanged(value: Any?) {
                logD("接收到字符串事件 : $value")
            }
        })
        LiveDataBus.instance.event("stringEvent").observer(this, {
            logD("接收到字符串事件 : $it")
        })

        //自定义事件监听
        LiveDataBus.instance.event(LiveEvents::class.java).stringEvent()
            .observer(this, object : ObserverWrapper<String>() {
                override fun onChanged(value: String?) {
                    logD("接收到自定义事件 : $value")
                }
            })
        LiveDataBus.instance.event(LiveEvents::class.java).stringEvent().observer(this) {
            binding.stringText.text = it
        }
        LiveDataBus.instance.event(LiveEvents::class.java).integerEvent().observer(this) {
            binding.integerText.text = it.toString()
        }
        LiveDataBus.instance.event(LiveEvents::class.java).userEvent().observer(this) {
            binding.userText.text = "userName : ${it.name} , userAge : ${it.age}"
        }
    }

}