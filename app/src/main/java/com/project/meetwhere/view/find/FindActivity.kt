package com.project.meetwhere.view.find

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.meetwhere.view.find.FindViewModel
import com.project.meetwhere.R
import com.project.meetwhere.view.near.NearActivity

class FindActivity : AppCompatActivity() {

    // View Model
    private lateinit var findViewModel: FindViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewModel = ViewModelProvider(this).get(FindViewModel::class.java)

        findViewModel._currentCount.observe(this, Observer {
            // 만약 중간역을 찾았으면
            if (!it.equals("")) {
                val intent = Intent(this, NearActivity::class.java)
                intent.putExtra("centerStation", it)
                startActivity(intent)
                finish()
            }
        })

        // intent가 있으면
        if (intent.hasExtra("List")) {
            var intentArr = intent.getStringExtra("List").toString()

            // 200ms의 지연 후 검색함
            Handler().postDelayed({
                findViewModel.find(intentArr, this)
            }, 200)
        } else {
            // 없으면 종료한다
            finish()
        }
    }
}