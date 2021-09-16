package com.zjh.livedatabus

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 *  desc : LiveData 包装类
 *  @author zjh
 *  on 2021/9/15
 */
interface LiveDataWrapper<T> {

    /**
     * 是否有观察者
     */
    fun haveObservers(): Boolean

    /**
     * 是否有活跃的观察者
     */
    fun hasActiveObservers(): Boolean

    /**
     * 发送数据
     */
    fun post(@NonNull value: T)

    /**
     * 观察（仅观察注册后发送的事件）
     */
    fun observer(@NonNull owner: LifecycleOwner, @NonNull observerWrapper: ObserverWrapper<T>)
    fun observer(@NonNull owner: LifecycleOwner , @NonNull observer: Observer<T>)

    /**
     * 观察粘性事件（注册观察者注册前发送的数据也能收到）
     */
    fun observerSticky(@NonNull owner: LifecycleOwner, @NonNull observerWrapper: ObserverWrapper<T>)
    fun observerSticky(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<T>)
}