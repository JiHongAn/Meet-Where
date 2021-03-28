package com.project.meetwhere.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.meetwhere.R
import com.project.meetwhere.adapter.StationAdapter
import com.project.meetwhere.model.Station
import com.project.meetwhere.view.select.SelectActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // View Model
    private lateinit var mainViewModel: MainViewModel

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // observer
        var list = ArrayList<Station>()
        mainViewModel._currentArrayList.observe(this, Observer {
            list = it
            var adapter = StationAdapter(it)
            recyclerview.adapter = adapter
        })

        // count
        mainViewModel._currentCount.observe(this, Observer {
            // 만약 arraylist의 개수가 0개가 아니라면
            if (it != 0) {
                // 숨긴다
                text_add.visibility = View.GONE
            }

            if (it >= 2) {
                layout_next.visibility = View.VISIBLE
            }
            count = it
        })
    }


    // 툴바 버튼 추가
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 추가 버튼 클릭시
            R.id.action_add -> {
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    // 추가할 역을 선택 했다면
                    var name = data!!.getStringExtra("name").toString()
                    var line = data!!.getStringExtra("line").toString()

                    // arraylist에 추가한다
                    mainViewModel.add(Station(name, line))
                }
            }
        }
    }

    //뒤로가기 연속 클릭 대기 시간
    var mBackWait: Long = 0
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish() //액티비티 종료
        }
    }
}