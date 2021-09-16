package com.zjh.livedatabus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zjh.livedatabus.databinding.ActivityOneBinding

/**
 *  desc :
 *  @author zjh
 *  on 2021/9/15
 */
class OneActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOneBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            LiveDataBus.instance.event("stringEvent").post("发送字符串事件")
            LiveDataBus.instance.event(LiveEvents::class.java).stringEvent().post("这是一个字符串")
            LiveDataBus.instance.event(LiveEvents::class.java).integerEvent().post(123)
            LiveDataBus.instance.event(LiveEvents::class.java).userEvent().post(User("小明", 18))
        }
    }

}