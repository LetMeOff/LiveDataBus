package com.zjh.livedatabus

import android.os.Looper
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *  desc : LiveData实现类
 *  @author zjh
 *  on 2021/9/15
 */
class ActiveLiveDataWrapper<T> : LiveDataWrapper<T> {

    /**
     * 事件序号
     */
    private var mSequence = 0

    /**
     * LiveData
     */
    private val mutableLiveData = MutableLiveData<ValueWrapper<T>>()

    /**
     * 是否有观察者
     */
    override fun haveObservers(): Boolean {
        return mutableLiveData.hasObservers()
    }

    /**
     * 是否有活跃的观察者
     */
    override fun hasActiveObservers(): Boolean {
        return mutableLiveData.hasActiveObservers()
    }

    /**
     * 发送数据
     */
    override fun post(@NonNull value: T) {
        if (isMainThread()) {
            mutableLiveData.value = ValueWrapper(value, mSequence)
        } else {
            mutableLiveData.postValue(ValueWrapper(value, mSequence))
        }
    }

    /**
     * 观察粘性数据
     */
    override fun observerSticky(owner: LifecycleOwner, observerWrapper: ObserverWrapper<T>) {
        observerWrapper.sticky = true
        observer(owner, observerWrapper)
    }

    override fun observerSticky(owner: LifecycleOwner, observer: Observer<T>) {
        //包装参数
        val observerWrapper: ObserverWrapper<T> = object : ObserverWrapper<T>() {
            override fun onChanged(value: T?) {
                value?.let { observer.onChanged(value) }
            }
        }
        observerWrapper.sticky = true
        observer(owner, observerWrapper)
    }

    /**
     * 观察数据
     */
    override fun observer(owner: LifecycleOwner, observerWrapper: ObserverWrapper<T>) {
        observerWrapper.sequence = if (observerWrapper.sticky) {
            -1
        } else {
            mSequence++
        }
        mutableLiveData.observe(owner, filterObserver(observerWrapper))
    }

    override fun observer(owner: LifecycleOwner, observer: Observer<T>) {
        //包装参数
        val observerWrapper: ObserverWrapper<T> = object : ObserverWrapper<T>() {
            override fun onChanged(value: T?) {
                value?.let { observer.onChanged(value) }
            }
        }
        observer(owner, observerWrapper)
    }

    /**
     * 从包装类中筛选出原始观察者
     */
    private fun filterObserver(observerWrapper: ObserverWrapper<T>): Observer<ValueWrapper<T>> {
        if (observerWrapper.observer != null) {
            //防止同一个事件被监听多次，导致接收到多次事件
            return observerWrapper.observer!!
        }

        //创建一个observer
        val observer = Observer<ValueWrapper<T>> {
            //回调
            if (it.sequence > observerWrapper.sequence) {
                // 产生的事件序号要大于观察者序号才被通知事件变化
                observerWrapper.onChanged(it.value)
            }
        }
        //添加到已有的observer
        observerWrapper.observer = observer
        return observer
    }

    /**
     * 是否主线程
     */
    private fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

}