package com.project.meetwhere.view.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.meetwhere.R
import com.project.meetwhere.adapter.SelectAdapter
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {

    // View Model
    private lateinit var stationViewModel: SelectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        setSupportActionBar(findViewById(R.id.toolbar))

        stationViewModel = ViewModelProvider(this).get(SelectViewModel::class.java)

        // 뒤로가기 버튼 추가
        val toolbar = supportActionBar!!
        toolbar.setDisplayHomeAsUpEnabled(true)

        // 검색기능
        edit_search_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // 200ms의 지연 후 검색함
                Handler().postDelayed({
                    stationViewModel.search(edit_search_query.text.toString())
                }, 200)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        // observer
        stationViewModel._currentArrayList.observe(this, Observer {
            var adapter = SelectAdapter(it)

            // 클릭 이벤트
            adapter.itemClick = object : SelectAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val intent = Intent()
                    intent.putExtra("name", it[position].name)
                    intent.putExtra("line", it[position].line)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            recyclerview.adapter = adapter
        })
    }

    // 툴바 버튼 추가
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 뒤로가기 버튼 클릭시
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}