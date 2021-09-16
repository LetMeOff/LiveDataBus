package com.zjh.livedatabus

import androidx.annotation.NonNull

/**
 *  desc : LiveData 参数包装类
 *  @author zjh
 *  on 2021/9/15
 */
class ValueWrapper<T>(
    //参数值
    @NonNull var value: T,
    // 每个被观察的事件数据都有一个序号，只有产生的事件数据在观察者加入之后才通知到观察者
    // 即事件数据序号要大于观察者序号
    var sequence: Int
)