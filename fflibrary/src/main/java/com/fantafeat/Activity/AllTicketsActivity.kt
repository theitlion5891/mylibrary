package com.fantafeat.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fantafeat.Adapter.MyTicketsAdapter
import com.fantafeat.Model.MyTickets
import com.fantafeat.R
import com.fantafeat.Session.BaseActivity
import com.fantafeat.util.ApiManager
import com.fantafeat.util.GetApiResult
import com.fantafeat.util.HttpRestClient
import com.fantafeat.util.LogUtil
import org.json.JSONObject

class AllTicketsActivity : BaseActivity() {

    private lateinit var toolbar_back: ImageView
    private lateinit var toolbar_title: TextView
    private lateinit var txtNoData: TextView
    private lateinit var recyclerMyTickets: RecyclerView
    private lateinit var laySwipe: SwipeRefreshLayout
    private var listMyTickets=ArrayList<MyTickets>()
    private var adapterMyTickets: MyTicketsAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tickets)
        toolbar_back=findViewById(R.id.toolbar_back)
        toolbar_title=findViewById(R.id.toolbar_title)
        txtNoData=findViewById(R.id.txtNoData)
        recyclerMyTickets=findViewById(R.id.recyclerMyTickets)
        laySwipe=findViewById(R.id.laySwipe)

        toolbar_title.text = "My Tickets"

        recyclerMyTickets.layoutManager=
            LinearLayoutManager(mContext)
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

        laySwipe.setOnRefreshListener {
            getMyTicketList()
        }

        toolbar_back.setOnClickListener { finish() }


    }

    override fun onResume() {
        super.onResume()
        getMyTicketList()
    }

    private fun getMyTicketList() {
        try {
            listMyTickets.clear()

            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)//
            param.put("ticket_status","")//'Open','Inreview','Close'
            LogUtil.e("resp", "getMyTicketList param : $param")
            val isLoader=!laySwipe.isRefreshing
            HttpRestClient.postJSON(mContext,isLoader,
                ApiManager.getMyTicketList,param,object : GetApiResult {
                    override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                        LogUtil.e("resp", "getMyTicketList: $responseBody")
                        if (laySwipe.isRefreshing){
                            laySwipe.isRefreshing=false
                        }
                        if (responseBody.optBoolean("status")){
                            val data=responseBody.optJSONArray("data")

                            if (data!=null && data.length()>0){

                                for (i in 0 until data.length()){
                                    val obj=data.optJSONObject(i)
                                    val model=gson.fromJson<MyTickets>(obj.toString(),MyTickets::class.java)
                                    listMyTickets.add(model)
                                }
                                adapterMyTickets!!.notifyDataSetChanged()
                            }
                        }
                        if (listMyTickets.size>0){
                            txtNoData.visibility= View.GONE
                            recyclerMyTickets.visibility= View.VISIBLE
                        }else{
                            txtNoData.visibility= View.VISIBLE
                            recyclerMyTickets.visibility= View.GONE
                        }
                    }

                    override fun onFailureResult(responseBody: String?, code: Int) {
                        if (laySwipe.isRefreshing){
                            laySwipe.isRefreshing=false
                        }
                    }

                })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            if (laySwipe.isRefreshing){
                laySwipe.isRefreshing=false
            }
        }


    }

    override fun initClick() {

    }
}