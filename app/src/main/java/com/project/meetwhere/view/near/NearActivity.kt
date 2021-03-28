package com.project.meetwhere.view.near

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.meetwhere.R
import com.project.meetwhere.adapter.TravelItemAdapter
import kotlinx.android.synthetic.main.activity_near.*

class NearActivity : AppCompatActivity() {

    private lateinit var nearViewModel: NearViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near)
        setSupportActionBar(findViewById(R.id.toolbar))

        // recyclerview 스크롤
        recyclerview.isNestedScrollingEnabled = false;

        nearViewModel = ViewModelProvider(this).get(NearViewModel::class.java)

        // 중간 역 보여주기
        var centerStation = ""
        if (intent.hasExtra("centerStation")) {
            centerStation = intent.getStringExtra("centerStation").toString()

            // title 지정
            text_center_station.text = centerStation
        } else {
            // 없으면 종료한다
            finish()
        }

        // liveData
        nearViewModel._currentArrayList.observe(this, Observer {
            progress.visibility = View.GONE
            var adapter = TravelItemAdapter(it)
            recyclerview.adapter = adapter
        })

        // 주소 변환 후 json 파싱
        nearViewModel.getArr(centerStation)

        // 검색기능
        edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // 200ms의 지연 후 검색함
                Handler().postDelayed({
                    progress.visibility = View.VISIBLE
                    nearViewModel.search(edit_search.text.toString())
                }, 200)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }
}