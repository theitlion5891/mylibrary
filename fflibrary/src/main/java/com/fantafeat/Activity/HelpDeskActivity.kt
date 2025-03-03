package com.fantafeat.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.fantafeat.Adapter.HelpdeskQuestionAdapter
import com.fantafeat.Adapter.MyTicketsAdapter
import com.fantafeat.Model.MyTickets
import com.fantafeat.Model.QuestionModel
import com.fantafeat.R
import com.fantafeat.Session.BaseActivity
import com.fantafeat.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject

class HelpDeskActivity : BaseActivity() {

    private lateinit var toolbar_back: ImageView
    private lateinit var toolbar_title: TextView
    private lateinit var txtAllTickets: TextView
    private lateinit var txtNoData: TextView
    private lateinit var layAllToic: LinearLayout
    private lateinit var layViewPast: LinearLayout
    private lateinit var layMyTickets: LinearLayout
    private lateinit var btnWrite: LinearLayout
    private lateinit var recyclerMyTickets: RecyclerView
    private lateinit var recyclerQue: RecyclerView
    private var listMyTickets=ArrayList<MyTickets>()
    private var listQuestion=ArrayList<QuestionModel>()
    private var adapterMyTickets: MyTicketsAdapter?=null
    private var adapterQuestion: HelpdeskQuestionAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_desk)
        toolbar_back=findViewById(R.id.toolbar_back)
        toolbar_title=findViewById(R.id.toolbar_title)
        recyclerMyTickets=findViewById(R.id.recyclerMyTickets)
        recyclerQue=findViewById(R.id.recyclerQue)
        txtNoData=findViewById(R.id.txtNoData)
        txtAllTickets=findViewById(R.id.txtAllTickets)
        layAllToic=findViewById(R.id.layAllToic)
        layViewPast=findViewById(R.id.layViewPast)
        layMyTickets=findViewById(R.id.layMyTickets)
        btnWrite=findViewById(R.id.btnWrite)
        toolbar_title.text = "Helpdesk"

        recyclerMyTickets.addItemDecoration(CirclePagerIndicatorDecoration())
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerMyTickets)

        recyclerQue.layoutManager= LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false)

        val dividerItemDecoration = DividerItemDecoration(recyclerQue.context, LinearLayoutManager.VERTICAL)
        recyclerQue.addItemDecoration(dividerItemDecoration)

        recyclerMyTickets.layoutManager=
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        adapterMyTickets= MyTicketsAdapter(mContext,listMyTickets,object :MyTicketsAdapter.onItemClick{
            override fun onClick(model: MyTickets) {
                startActivity(
                    Intent(mContext,ChatActivity::class.java)
                    .putExtra("ticket_model", model)
                    .putExtra("isCreateTickets", false)
                )
            }
        })
        recyclerMyTickets.adapter=adapterMyTickets

        adapterQuestion=HelpdeskQuestionAdapter(mContext,listQuestion,object :HelpdeskQuestionAdapter.onItemClick{
            override fun onClick(model: QuestionModel) {
                showAnswerSheet(model)
                /*startActivity(Intent(mContext,ChatActivity::class.java)
                    .putExtra("topic_id", model.topicId)
                    .putExtra("msg", model.faqQuestion)
                    .putExtra("isCreateTickets", true)
                )*/
            }
        })
        recyclerQue.adapter=adapterQuestion

        toolbar_back.setOnClickListener { finish() }

        layAllToic.setOnClickListener {
            startActivity(Intent(this,BrowseAllTopicActivity::class.java))
        }

        btnWrite.setOnClickListener {
            /*startActivity(Intent(mContext,ChatActivity::class.java)
                .putExtra("isCreateTickets", false)
            )*/
            startActivity(Intent(this,BrowseAllTopicActivity::class.java))
        }

        txtAllTickets.setOnClickListener {
            startActivity(Intent(this,AllTicketsActivity::class.java))
        }

        layViewPast.setOnClickListener {
            startActivity(Intent(this,AllTicketsActivity::class.java))
        }



    }

    private fun showAnswerSheet(model: QuestionModel) {
        val dialog= BottomSheetDialog(this)
        dialog.setContentView(R.layout.faq_answer_sheet)

        val txtFaq=dialog.findViewById<TextView>(R.id.txtFaq)
        val txtAnswer=dialog.findViewById<TextView>(R.id.txtAnswer)

        txtFaq!!.text=model.faqQuestion
        txtAnswer!!.text=model.faqAnswer

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        getMyTicketList()
    }

    override fun initClick() {

    }

    private fun getMyTicketList() {
        try {
            listMyTickets.clear()

            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            param.put("ticket_status","")//'Open','Inreview','Close'
            param.put("limit","3")
            param.put("offset","0")
            LogUtil.e("resp", "getMyTicketList param : $param")
            HttpRestClient.postJSON(mContext,true, ApiManager.getMyTicketList,param,object :
                GetApiResult {
                override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                    LogUtil.e("resp", "getMyTicketList: $responseBody")

                    if (responseBody.optBoolean("status")){
                        val data=responseBody.optJSONArray("data")

                        if (data!=null && data.length()>0){

                            for (i in 0 until data.length()){
                                val obj=data.optJSONObject(i)
                                val model=gson.fromJson<MyTickets>(obj.toString(),MyTickets::class.java)
                                /*if (ConstantUtil.is_game_test)
                                    model.ticketStatus="Close"*/
                                listMyTickets.add(model)
                            }
                            adapterMyTickets!!.notifyDataSetChanged()
                        }
                    }
                    if (listMyTickets.size>0){
                        layMyTickets.visibility= View.VISIBLE
                    }else{
                        layMyTickets.visibility= View.GONE
                    }

                    getTopicList()
                }

                override fun onFailureResult(responseBody: String?, code: Int) {

                }

            })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


    }

    private fun getTopicList() {
        try {
            listQuestion.clear()
            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            LogUtil.e("resp", "getTopicList param : $param")
            HttpRestClient.postJSON(mContext,false,ApiManager.getCallbackTopicFaqListDeafultDisplay,param,object :GetApiResult{
                override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                    LogUtil.e("resp", "getTopicList: $responseBody")

                    if (responseBody.optBoolean("status")){
                        val data=responseBody.optJSONArray("data")
                        if (data!=null && data.length()>0){
                            for (i in 0 until data.length()){
                                val obj=data.optJSONObject(i)
                                val model=gson.fromJson<QuestionModel>(obj.toString(),QuestionModel::class.java)
                                listQuestion.add(model)
                            }
                            adapterQuestion!!.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailureResult(responseBody: String?, code: Int) {

                }

            })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }
}