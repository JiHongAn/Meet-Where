package com.project.meetwhere.helper

import android.content.res.AssetManager
import com.project.meetwhere.model.Find
import com.project.meetwhere.model.FindStart
import com.project.meetwhere.model.Params
import com.project.meetwhere.view.find.FindActivity
import org.json.JSONObject

class SetWeight(application: FindActivity) {
    private val assetManager: AssetManager = application.resources.assets

    // Arraylist 배열
    var arr = arrayOf(
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>(),
        ArrayList<Find>()
    )

    // 가중치 주기
    var arrLine = ArrayList<FindStart>()
    var startName = ""
    fun init(nameString: String): ArrayList<Params> {
        for (i in 0 until 8) {
            arr[i].clear()
        }
        arrLine.clear()

        var name = nameString

        // 배열에 역 저장하기
        getLine()

        // 출발 위치 찾기
        getStart(name + "")

        // 이름
        startName = name
        for (item in arrLine) {
            var line = item.line
            var startInt = item.start

            // 가중치 주기
            arr[line - 1][startInt].weight = -1

            // 시작
            setWeight(line - 1, 0, startInt)
        }


        // 전송할 파라미터
        var arrSend = ArrayList<Params>()

        for (i in 0 until 9) {
            for (j in 0 until arr[i].size) {
                arrSend.add(Params(arr[i][j].name, arr[i][j].weight))
            }
        }

        return arrSend
    }

    // json 파일 배열에 넣기
    private fun getLine() {
        for (subway in 1 until 10) {
            // json 파일 가져오기
            val inputStream = assetManager.open("station_line_$subway.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val jObject = JSONObject(jsonString)
            val jArray = jObject.getJSONArray("station")

            for (i in 0 until jArray.length()) {
                val obj = jArray.getJSONObject(i)
                var name = ""
                var line = ""
                var exchange = ""

                try {
                    name = obj.getString("name")
                    line = obj.getString("line")
                    exchange = obj.getString("exchange")
                } catch (e: Exception) {
                }

                // arraylist에 추가
                arr[subway - 1].add(Find(name, line, exchange, 0))
            }
        }
    }

    // 출발 위치 찾기
    private fun getStart(name: String) {
        // 1호선부터 9호선까지
        for (subway in 0 until 9) {
            // 같은 이름의 역이 있으면
            for (i in 0 until arr[subway].size) {
                if (arr[subway][i].name == name) {
                    // 시작 위치 설정
                    arrLine.add(FindStart(arr[subway][i].line.toInt(), i))
                }
            }
        }
    }

    // 가중치 주기
    private fun setWeight(line: Int, count: Int, start: Int) {
        var countUp = count
        var countDown = count

        // 노선 위로
        for (i in start until arr[line].size) {
            // 가중치를 정해준다
            if (arr[line][i].weight > countUp || arr[line][i].weight == 0) {
                arr[line][i].weight = countUp
            } else {
            }

            // 만약 환승역이 있다면
            if (!arr[line][i].exchange.equals("")) {
                // 콤마 단위로 분리한다
                var arrExchange = arr[line][i].exchange.split(",")

                for (cursor in arrExchange.indices) {
                    var position = arrExchange[cursor].replace(" ", "").toInt() - 1

                    // 같은 역 이름을 찾는다
                    for (j in 0 until arr[position].size) {
                        if (arr[position][j].name.equals(arr[line][i].name)) {
                            if (arr[position][j].weight > countUp || arr[position][j].weight == 0) {
                                // 환승 시간을 고려한다
                                setWeight(position, countUp + 5, j)
                            }
                        }
                    }
                }
            }

            // ++
            countUp++
        }

        // 노선 아래로
        for (i in start downTo 0) {
            // 가중치를 정해준다
            if (arr[line][i].weight > countDown || arr[line][i].weight == 0) {
                arr[line][i].weight = countDown
            } else {
            }

            // 만약 환승역이 있다면
            if (!arr[line][i].exchange.equals("")) {
                // 콤마 단위로 분리한다
                var arrExchange = arr[line][i].exchange.split(",")

                for (cursor in arrExchange.indices) {
                    var position = arrExchange[cursor].replace(" ", "").toInt() - 1

                    // 같은 역 이름을 찾는다
                    for (j in 0 until arr[position].size) {
                        if (arr[position][j].name.equals(arr[line][i].name)) {
                            if (arr[position][j].weight > countDown || arr[position][j].weight == 0) {
                                // 환승 시간을 고려한다
                                setWeight(position, countDown + 5, j)
                            }
                        }
                    }
                }
            }

            // ++
            countDown++
        }
        return
    }
}