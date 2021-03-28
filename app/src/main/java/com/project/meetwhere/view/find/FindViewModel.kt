package com.project.meetwhere.view.find

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.meetwhere.helper.SetWeight
import com.project.meetwhere.model.Params
import kotlin.math.sqrt

class FindViewModel(application: Application) : AndroidViewModel(application) {

    // MutableLiveData
    private val liveString = MutableLiveData<String>()
    val _currentCount: LiveData<String>
        get() = liveString

    // Arraylist 배열
    private var arr = arrayOf(
        ArrayList<Params>(),
        ArrayList<Params>(),
        ArrayList<Params>(),
        ArrayList<Params>(),
        ArrayList<Params>()
    )

    init {
        liveString.value = ""
    }

    fun find(intentGap: String, activity: FindActivity) {
        var list = ArrayList<String>()
        var intentArr = intentGap.split("|")

        for (i in intentArr.indices) {
            list.add(intentArr[i])
        }

        // 중간 역을 저장하는 String
        var center = ""

        var last = 0
        var min = Double.MAX_VALUE
        var minAverage = Double.MAX_VALUE

        // 가중치를 설정한다
        var setWeight = SetWeight(activity)
        for ((i, item) in list.withIndex()) {
            arr[i] = setWeight.init(item.trim())
            last = i
        }
        last++

        // 역들 사이 간격 비교
        for (j in 0 until arr[0].size) {
            var sum = 0

            for (i in 0 until last) {
                sum += arr[i][j].weight
            }

            // 평균 구하기
            var average = (sum / last).toDouble()

            // 표준편차를 구한다
            var sd = 0.0
            for (i in 0 until last) {
                if (arr[i][j].weight == 0) {
                    sd = 0.0
                    break
                }

                var temp = arr[i][j].weight - average
                sd += temp * temp
            }

            if (sd == 0.0) {
                continue
            }

            sd /= last
            sd = sqrt(sd)

            // 중간 역을 정한다
            if (sd * sd < min && average < minAverage) {
                center = arr[0][j].name
                min = sd
                minAverage = average
            }
        }

        liveString.value = center
    }
}