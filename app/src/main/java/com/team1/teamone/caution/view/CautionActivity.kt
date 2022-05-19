package com.team1.teamone.caution.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.caution.model.CautionApi
import com.team1.teamone.caution.model.CautionListResponse
import com.team1.teamone.caution.model.CautionResponse
import com.team1.teamone.caution.presenter.CautionAdapter
import com.team1.teamone.databinding.ActivityCautionBinding
import com.team1.teamone.util.network.BoolResponse
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CautionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCautionBinding
    private val api = RetrofitClient.create(CautionApi::class.java, RetrofitClient.getAuth())
    private val cautionDataList = mutableListOf<CautionResponse>()
    private lateinit var cautionAdapter: CautionAdapter

    // 액티비티 메인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caution)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_caution)

        // 최초로 들어오면 유의 리스트 띄우기
        drawCautionList()

        // 유의 전체 삭제
        binding.btnCautionRemoveAll.setOnClickListener {
            deleteAllCautions() // 전체삭제 확인하는 다이얼로그 출력
        }
    }

    private fun drawCautionList() {
        api.getAllCautions().enqueue(object : Callback<CautionListResponse> {
            override fun onResponse(
                call: Call<CautionListResponse>,
                response: Response<CautionListResponse>
            ) {
                // 성공
                cautionDataList.clear()
                response.body()?.cautions?.let { it -> cautionDataList.addAll(it) }
                Log.d("유의 통신 성공", cautionDataList.toString())
                cautionAdapter = CautionAdapter(cautionDataList)
                binding.rvCautionList.adapter = cautionAdapter
                binding.rvCautionList.layoutManager =
                    LinearLayoutManager(this@CautionActivity, LinearLayoutManager.VERTICAL, false)

                // 유의 버튼 눌렀을때 유의 해제
                cautionAdapter.setItemClickListener(object : CautionAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        deleteCaution(position)
                    }
                })
            }

            override fun onFailure(call: Call<CautionListResponse>, t: Throwable) {
                // 실패
                Log.d("유의 통신 실패", "유의 전체 조회 실패")
            }
        })
    }

    private fun deleteCaution(position: Int) {
        cautionDataList[position].cautionedMember.memberId?.let {
            api.deleteCaution(it).enqueue(object : Callback<BoolResponse> {
                override fun onResponse(
                    call: Call<BoolResponse>,
                    response: Response<BoolResponse>
                ) {
                    // 성공
                    Toast.makeText(
                        this@CautionActivity,
                        "사용자 유의를 해제했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    drawCautionList()
                }

                override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                    // 실패
                    Log.d("유의 통신 실패", "유의 하나 삭제 실패")
                }
            })
        }
    }

    private fun deleteAllCautions() {
        AlertDialog.Builder(this)
            .setTitle("유의 리스트 전체 삭제")
            .setMessage("현재 유의 표시한 사용자들을 전체 삭제하시겠습니까?")
            .setPositiveButton("예") { dialog, id ->
                deleteRequestToServer() // 서버로 북마크 삭제 요청
            }
            .setNegativeButton("아니오") { dialog, id ->

            }
            .show()
    }

    private fun deleteRequestToServer() {
        api.deleteAllCautions().enqueue(object : Callback<BoolResponse>{
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                // 성공
                Toast.makeText(this@CautionActivity, "유의 사용자 전체 삭제 완료", Toast.LENGTH_SHORT).show()
                // 리스트 다시 보여주기
                drawCautionList()
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("유의 통신 실패", "유의 전체 삭제 실패")
            }
        })
    }
}