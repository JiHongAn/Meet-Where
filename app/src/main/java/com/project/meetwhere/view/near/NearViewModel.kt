package com.project.meetwhere.view.near

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.project.meetwhere.R
import com.project.meetwhere.model.TravelItem
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class NearViewModel(application: Application) : AndroidViewModel(application) {

    // volley 객체
    private var requestQueue: RequestQueue? = null

    // 파싱 주소
    var url: String = ""

    // GeoCoder
    var geoCoder = Geocoder(application)

    // MutableLiveData
    private val liveList = MutableLiveData<ArrayList<TravelItem>>()
    private var list = ArrayList<TravelItem>()
    private var listSearch = ArrayList<TravelItem>()
    val _currentArrayList: LiveData<ArrayList<TravelItem>>
        get() = liveList


    init {
        // Volley 객체 생성
        requestQueue = Volley.newRequestQueue(application)

        url = application.getString(R.string.rest_url)
    }

    fun getArr(centerStation: String) {
        var lat = 0.0
        var lng = 0.0

        try {
            // 역 이름 -> 좌표로 변환
            var mResultLocation: List<Address> =
                geoCoder.getFromLocationName(centerStation + "역", 1)

            // 좌표
            lat = mResultLocation[0].latitude
            lng = mResultLocation[0].longitude
        } catch (e: IOException) {
        }

        // url 주소
        url += "/get-around.php?mapX=$lng&mapY=$lat"
        jsonParse(url)
    }

    // 여행지 데이터를 가져온다
    private fun jsonParse(url: String) {
        val request =
            JsonArrayRequest(Request.Method.GET, url, null, { response ->
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject: JSONObject = response.getJSONObject(i)

                        val title = jsonObject.getString("title")
                        val firstImage = jsonObject.getString("firstImage")
                        val mapX = jsonObject.getString("mapX")
                        val mapY = jsonObject.getString("mapY")
                        val contentId = jsonObject.getString("contentId")
                        val contentTypeId = jsonObject.getString("contentTypeId")
                        val address = jsonObject.getString("address")
                        val dist = jsonObject.getString("dist")

                        // Model 추가
                        var travelItem = TravelItem(
                            title,
                            firstImage,
                            mapX,
                            mapY,
                            contentId,
                            contentTypeId,
                            address,
                            dist
                        )

                        // arraylist에 model 추가
                        list.add(travelItem)
                    }

                    liveList.value = list
                } catch (e: JSONException) {
                    Log.e("e", e.printStackTrace().toString())
                }
            }, { error ->
                Log.e("e", error.printStackTrace().toString())
            })
        requestQueue?.add(request)
    }

    // 검색기능
    fun search(query: String) {
        // arraylist를 비운다
        listSearch.clear()

        for (item in list) {
            // 만약 국가 이름이나 국가 코드, 국가 번호가 같다면
            if (item.title.toLowerCase().contains(query)) {
                listSearch.add(item)
            }
        }

        liveList.value = listSearch
    }
}