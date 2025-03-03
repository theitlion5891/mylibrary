package com.fantafeat.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batball11.adapter.TopicCatAdapter
import com.fantafeat.Adapter.HelpdeskQuestionAdapter
import com.fantafeat.Model.QuestionModel
import com.fantafeat.Model.TopicCatModel
import com.fantafeat.Model.TopicModel
import com.fantafeat.R
import com.fantafeat.Session.BaseActivity
import com.fantafeat.util.ApiManager
import com.fantafeat.util.GetApiResult
import com.fantafeat.util.HttpRestClient
import com.fantafeat.util.LogUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class BrowseAllTopicActivity : BaseActivity() {

    private lateinit var toolbar_back: ImageView
    private lateinit var toolbar_title: TextView
    private lateinit var recyclerAllTopic: RecyclerView
    //private var topicList=ArrayList<TopicModel>()
    private var topicCatList= ArrayList<TopicCatModel>()
    private lateinit var topicAdapter: TopicCatAdapter
    private lateinit var adapterQuestion: HelpdeskQuestionAdapter
    private var listQuestion= ArrayList<QuestionModel>()
    private var dialog: Dialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_all_topic)
        toolbar_back=findViewById(R.id.toolbar_back)
        toolbar_title=findViewById(R.id.toolbar_title)
        recyclerAllTopic=findViewById(R.id.recyclerAllTopic)

        toolbar_title.text = "All Topic"

        toolbar_back.setOnClickListener { finish() }

        recyclerAllTopic.layoutManager= LinearLayoutManager(mContext)

        topicAdapter= TopicCatAdapter(mContext,topicCatList,object :TopicCatAdapter.onItemClick{
            override fun onClick(model: TopicModel) {
                getFaqList(model)
            }
        })
        recyclerAllTopic.adapter=topicAdapter

        getAllTopic()
    }

    private fun getFaqList(model1: TopicModel) {
        try {

            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            param.put("topic_id",model1.id)
            LogUtil.e("resp", "getFaqList param : $param")

            HttpRestClient.postJSON(mContext,true,
                ApiManager.getCallbackTopicFaqList,param,object : GetApiResult {
                    override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                        LogUtil.e("resp", "getFaqList: $responseBody")

                        if (responseBody.optBoolean("status")){
                            val data=responseBody.optJSONArray("data")
                            listQuestion.clear()
                            for (i in 0 until data.length()){
                                val obj=data.optJSONObject(i)
                                val model=gson.fromJson<QuestionModel>(obj.toString(),QuestionModel::class.java)
                                listQuestion.add(model)
                            }
                            showFaqListSheet(model1)
                        }

                    }

                    override fun onFailureResult(responseBody: String?, code: Int) {

                    }

                })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun showFaqListSheet(model: TopicModel) {
        dialog= BottomSheetDialog(mContext)
        dialog!!.setContentView(R.layout.faq_list_sheet)

        val recyclerFaq=dialog!!.findViewById<RecyclerView>(R.id.recyclerFaq)
        val imgClose=dialog!!.findViewById<ImageView>(R.id.imgClose)
        val txtTitle=dialog!!.findViewById<TextView>(R.id.txtTitle)
        val nodata=dialog!!.findViewById<LinearLayout>(R.id.nodata)

        txtTitle!!.text = model.topicName

        recyclerFaq!!.layoutManager= LinearLayoutManager(mContext)
        val dividerItemDecoration = DividerItemDecoration(recyclerFaq.context, LinearLayoutManager.VERTICAL)
        recyclerFaq.addItemDecoration(dividerItemDecoration)

        LogUtil.e("resp", "getFaqList: ${listQuestion.size}")

        adapterQuestion= HelpdeskQuestionAdapter(mContext,listQuestion,object : HelpdeskQuestionAdapter.onItemClick{
            override fun onClick(model: QuestionModel) {
                dialog!!.dismiss()
                startActivity(
                    Intent(mContext,ChatActivity::class.java)
                        .putExtra("faq_id", model.id)
                        .putExtra("topic_id", model.topicId)
                        .putExtra("msg", model.faqQuestion)
                        .putExtra("isCreateTickets", true)
                )
            }
        })
        recyclerFaq.adapter=adapterQuestion

        if (listQuestion.size==0){
            nodata.visibility= View.VISIBLE
            recyclerFaq.visibility= View.GONE
        }else{
            nodata.visibility= View.GONE
            recyclerFaq.visibility= View.VISIBLE
        }

        imgClose!!.setOnClickListener { dialog!!.dismiss() }

        /*dialog!!.setOnDismissListener {
            topicCatList.forEachIndexed { index1, topicModel1 ->
                topicModel1.list.forEachIndexed { index, topicModel ->
                    topicModel.isSelected=false
                }
            }
            topicAdapter.notifyDataSetChanged()
        }*/

        val window = dialog!!.window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawableResource(android.R.color.transparent)

        dialog!!.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog?
            setupFullHeight(bottomSheetDialog!!)
        }

        dialog!!.show()
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
    /*    val bottomSheet: FrameLayout =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams: ViewGroup.LayoutParams = bottomSheet.getLayoutParams()
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.setLayoutParams(layoutParams)
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED)*/
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun getAllTopic() {
        try {

            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            LogUtil.e("resp", "getAllTopic param : $param")

            HttpRestClient.postJSON(mContext,true,
                ApiManager.getCallbackTopicList,param,object : GetApiResult {
                    override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                        LogUtil.e("resp", "getAllTopic: $responseBody")

                        if (responseBody.optBoolean("status")){
                            val data=responseBody.optJSONArray("data")
                            topicCatList.clear()
                            val map= HashMap<String,ArrayList<TopicModel>>()
                            for (i in 0 until data.length()){
                                val obj=data.optJSONObject(i)
                                val model=gson.fromJson<TopicModel>(obj.toString(),TopicModel::class.java)
                                if (map.containsKey(model.cat_name)){
                                    map[model.cat_name]!!.add(model)
                                }else{
                                    val list=ArrayList<TopicModel>()
                                    list.add(model)
                                    map[model.cat_name] = list
                                }
                                //topicList.add(model)
                            }

                            for ((key, value) in map) {
                                val model=TopicCatModel()
                                model.title=key
                                model.list=value
                                topicCatList.add(model)
                            }
                            topicCatList.reverse()

                            //LogUtil.e("resp","Map: $map ${map.size}")

                            topicAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailureResult(responseBody: String?, code: Int) {

                    }

                })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (dialog!=null && dialog!!.isShowing){
            dialog!!.dismiss()
        }
    }

    override fun initClick() {

    }
}