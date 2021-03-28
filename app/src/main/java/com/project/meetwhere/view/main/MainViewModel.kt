package com.project.meetwhere.view.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.meetwhere.model.Station

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // MutableLiveData
    private val liveList = MutableLiveData<ArrayList<Station>>()
    private var list = ArrayList<Station>()
    val _currentArrayList: LiveData<ArrayList<Station>>
        get() = liveList


    // MutableLiveData
    private val liveCount = MutableLiveData<Int>()
    val _currentCount: LiveData<Int>
        get() = liveCount


    init {
        liveCount.value = 0
    }

    fun add(station: Station) {
        // 만약 추가했던 역을 선택하면 추가하지 않는다
        var flag = true
        for (item in list) {
            // 만약 같으면
            if (item.name.equals(station.name)) {
                flag = false
                break
            }
        }

        // 중복된 역이 없으면
        if (flag) {
            // 추가한다
            list.add(station)
            liveList.value = list
        }

        // count
        liveCount.value = liveCount.value?.plus(1)
    }
}