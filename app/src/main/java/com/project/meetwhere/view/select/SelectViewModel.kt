package com.project.meetwhere.view.select

import android.app.Application
import android.content.res.AssetManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.meetwhere.model.Station
import org.json.JSONObject

class SelectViewModel(application: Application) : AndroidViewModel(application) {

    // MutableLiveData
    private val liveList = MutableLiveData<ArrayList<Station>>()
    private var list = ArrayList<Station>()
    val _currentArrayList: LiveData<ArrayList<Station>>
        get() = liveList

    init {
        getData(application)
    }

    // 데이터를 가져온다
    private fun getData(application: Application) {
        // 지하철 역 데이터를 가져온다
        val assetManager: AssetManager = application.resources.assets
        val inputStream = assetManager.open("station.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val jObject = JSONObject(jsonString)
        val jArray = jObject.getJSONArray("station")

        for (i in 0 until jArray.length()) {
            val obj = jArray.getJSONObject(i)
            var name = ""
            var line = ""

            try {
                name = obj.getString("name")
                line = obj.getString("line")
            } catch (e: Exception) {
            }

            // arraylist에 추가
            list.add(Station(name, line))
        }

        // 이름순으로 정렬한다
        list.sortBy { it.name }
        liveList.value = list
    }

    // 검색기능
    fun search(query: String) {
        var listSearch = ArrayList<Station>()

        // 만약 문자열이 포함된다면
        for (item in list) {
            if (item.name.toLowerCase().contains(query.toLowerCase())) {
                listSearch.add(item)
            }
        }

        liveList.value = listSearch
    }
}