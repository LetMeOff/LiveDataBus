package com.zjh.livedatabus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zjh.livedatabus.databinding.ActivityTwoBinding

/**
 *  desc :
 *  @author zjh
 *  on 2021/9/15
 */
class TwoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityTwoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        LiveDataBus.instance.event(LiveEvents::class.java).stickyEvent().observerSticky(this) {
            binding.stickyText.text = it
        }
    }

}