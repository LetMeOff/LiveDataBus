package com.zjh.livedatabus

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 *  desc : LiveData 事件总线
 *  @author zjh
 *  on 2021/9/15
 */
class LiveDataBus {

    /**
     * LiveData集合
     */
    private val eventBus by lazy {
        mutableMapOf<String, LiveDataWrapper<*>>()
    }

    /**
     * 通过传入字符串直接创建LiveData event
     */
    fun event(event: String): LiveDataWrapper<Any> {
        return createEvent(event)
    }

    /**
     * 自定义事件后传入，再进行事件的发送和订阅操作
     * (不直接传入字符串，对事件进行约束，假如A传入`eventOne`发送事件，而B以`event0ne`来订阅事件，那么此事件就永远接收不到，并且会增加排查难度。
     * 而且当上游删除发送事件相关代码后，订阅方也无法感知到。因此用动态代理的方式来实现)
     */
    @Suppress("UNCHECKED_CAST")
    fun <E> event(clz: Class<E>): E {
        if (!clz.isInterface) {
            throw IllegalArgumentException("API declarations must be interfaces.")
        }
        if (clz.interfaces.isNotEmpty()) {
            throw IllegalArgumentException("API interfaces must not extend other interfaces.")
        }
        return Proxy.newProxyInstance(
            clz.classLoader,
            arrayOf(clz),
            InvocationHandler { _, method, _ ->
                return@InvocationHandler event(
                    // 事件名以集合类名_事件方法名定义
                    // 以此保证事件的唯一性
                    "${clz.canonicalName}_${method.name}"
                )
            }) as E
    }


    /**
     * 创建LiveData event
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> createEvent(event: String): LiveDataWrapper<T> {
        //获取或者创建一个LiveData实现类
        return eventBus.getOrPut(event, {
            ActiveLiveDataWrapper<T>()
        }) as LiveDataWrapper<T>
    }

    companion object {
        @JvmStatic
        val instance by lazy {
            LiveDataBus()
        }
    }

}