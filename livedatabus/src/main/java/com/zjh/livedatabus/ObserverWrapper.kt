package com.zjh.livedatabus

import androidx.lifecycle.Observer

/**
 *  desc : 观察者包装类
 *  @author zjh
 *  on 2021/9/15
 */
abstract class ObserverWrapper<T> {
    // 每个观察者都记录自己序号，只有在进入观察状态之后产生的数据才通知到观察者
    var sequence = 0

    //观察者
    var observer: Observer<ValueWrapper<T>>? = null

    // 默认不是粘性事件，不会收到监听之前发送的事件
    var sticky = false

    /**
     * 发生了变化
     *
     * @param value 新的值
     */
    abstract fun onChanged(value: T?)
}