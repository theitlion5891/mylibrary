package com.fantafeat.Activity

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fantafeat.Adapter.CallbackChatAdapter
import com.fantafeat.Model.ChatModel
import com.fantafeat.Model.MyTickets
import com.fantafeat.Model.UserModel
import com.fantafeat.R
import com.fantafeat.Session.BaseActivity
import com.fantafeat.Session.BaseFragment
import com.fantafeat.Session.MyApp
import com.fantafeat.util.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class ChatActivity : BaseActivity() {

    private var topic_id:String=""
    private var msg:String=""
    private var faq_id:String=""
    private var topic_name:String=""
    private var trans_id:String=""
    private var reason:String=""
    private var selectedPath:String=""
    private var isCreateTickets:Boolean=false
    private var ticket_model: MyTickets?=null
    private lateinit var toolbar_back: ImageView
    private lateinit var btnSend: ImageView
    private lateinit var imgRatted: TextView
    private lateinit var toolbar_title: TextView
    private lateinit var txtLblNoti: TextView
    private lateinit var txtSubmit: TextView
    private lateinit var txtRatted: TextView
    private lateinit var recyclerChat: RecyclerView
    private lateinit var layFooter: LinearLayout
    private lateinit var layRate: LinearLayout
    private lateinit var layRateAfter: LinearLayout
    private lateinit var layBot:LinearLayout

    private lateinit var txtBotQue:TextView
    private lateinit var btnYes:LinearLayout
    private lateinit var btnNo:LinearLayout

    private lateinit var layAwful: LinearLayout
    private lateinit var layBad: LinearLayout
    private lateinit var layOk: LinearLayout
    private lateinit var layGood: LinearLayout
    private lateinit var layGreat: LinearLayout
    private lateinit var layNoti: LinearLayout

    private lateinit var edtMsg: EditText
    private var list:ArrayList<ChatModel> ?= null
    private lateinit var adapter: CallbackChatAdapter
    private var ratting=0
    private var selectedChatModel: ChatModel?=null

    var handler = Handler()
    var runnable: Runnable? = null
    var delay = 10 * 1000
    private var isTimer=false
    private var isMediaUploading=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (intent.hasExtra("ticket_model")) {
            ticket_model = intent.getSerializableExtra("ticket_model") as MyTickets
            topic_id=ticket_model!!.topicId
            msg=ticket_model!!.topicName
        }
        else{
            if (intent.hasExtra("topic_id"))
                topic_id=intent.getStringExtra("topic_id")!!
            if (intent.hasExtra("msg"))
                msg=intent.getStringExtra("msg")!!
        }

        if (intent.hasExtra("faq_id"))
            faq_id=intent.getStringExtra("faq_id")!!

        if (intent.hasExtra("topic_name"))
            topic_name=intent.getStringExtra("topic_name")!!
        if (intent.hasExtra("trans_id"))
            trans_id=intent.getStringExtra("trans_id")!!

        isCreateTickets=intent.getBooleanExtra("isCreateTickets",false)

        toolbar_back=findViewById(R.id.toolbar_back)
        toolbar_title=findViewById(R.id.toolbar_title)
        if (ticket_model!=null)
            toolbar_title.text = "#"+ticket_model!!.ticketId
        else
            toolbar_title.text = "Chat"

        recyclerChat=findViewById(R.id.recyclerChat)
        edtMsg=findViewById(R.id.edtMsg)
        btnSend=findViewById(R.id.btnSend)
        layFooter=findViewById(R.id.layFooter)
        layRate=findViewById(R.id.layRate)
        layNoti=findViewById(R.id.layNoti)

        layAwful=findViewById(R.id.layAwful)
        layBad=findViewById(R.id.layBad)
        layOk=findViewById(R.id.layOk)
        layGood=findViewById(R.id.layGood)
        layGreat=findViewById(R.id.layGreat)
        txtSubmit=findViewById(R.id.txtSubmit)
        layRateAfter=findViewById(R.id.layRateAfter)
        txtRatted=findViewById(R.id.txtRatted)
        imgRatted=findViewById(R.id.imgRatted)
        txtLblNoti=findViewById(R.id.txtLblNoti)

        layBot=findViewById(R.id.layBot)
        txtBotQue=findViewById(R.id.txtBotQue)
        btnNo=findViewById(R.id.btnNo)
        btnYes=findViewById(R.id.btnYes)

        list= ArrayList()

        var status="open"
        if (ticket_model!=null){
            status=ticket_model!!.ticketStatus
        }

        recyclerChat.layoutManager=LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,true)
        adapter= CallbackChatAdapter(mContext, list!!,object :CallbackChatAdapter.onClickAction{
            override fun onUploadMedia(model: ChatModel) {
                selectedChatModel=model;
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/* video/*"
                imageLauncher.launch(pickIntent)
                //uploadMedia(model)
            }

            override fun onShowMedia(model: ChatModel) {
                //CustomUtil.loadImageWithGlide(mContext,player_img, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.user_image);
                showFullImageDialog(model)
            }
        },status)
        recyclerChat.adapter=adapter

        btnSend.setOnClickListener {
            val msg1=edtMsg.text.toString().trim()

            if (TextUtils.isEmpty(msg1)){
                CustomUtil.showTopSneakWarning(mContext,"Enter Message")
                return@setOnClickListener
            }
            msg=msg1
            createTickets()

            edtMsg.setText("")
            edtMsg.clearFocus()
            hideKeyboard(this@ChatActivity)
        }

        toolbar_back.setOnClickListener { finish() }

        checkStatus()

        if (isCreateTickets){
            createTickets()
        }
        else{
            getTicketsMsg(true)
            if (!ticket_model!!.ticketStatus.equals("close",true)){
                isTimer=true
                handler.postDelayed(Runnable { //do something
                    LogUtil.e("resp","Running: ")
                    getTicketsMsg(false)
                    handler.postDelayed(runnable!!, delay.toLong())
                }.also { runnable = it }, delay.toLong())

            }

        }

        layAwful.setOnClickListener {
            layAwful.alpha=1f
            layBad.alpha=0.2f
            layOk.alpha=0.2f
            layGood.alpha=0.2f
            layGreat.alpha=0.2f
            ratting=1
            submitRatting()
        }

        layBad.setOnClickListener {
            layBad.alpha=1f
            layAwful.alpha=0.2f
            layOk.alpha=0.2f
            layGood.alpha=0.2f
            layGreat.alpha=0.2f
            ratting=2
            submitRatting()
        }

        layOk.setOnClickListener {
            layOk.alpha=1f
            layAwful.alpha=0.2f
            layBad.alpha=0.2f
            layGood.alpha=0.2f
            layGreat.alpha=0.2f
            ratting=3
            submitRatting()
        }

        layGood.setOnClickListener {
            layGood.alpha=1f
            layAwful.alpha=0.2f
            layBad.alpha=0.2f
            layOk.alpha=0.2f
            layGreat.alpha=0.2f
            ratting=4
            submitRatting()
        }

        layGreat.setOnClickListener {
            layGreat.alpha=1f
            layAwful.alpha=0.2f
            layBad.alpha=0.2f
            layOk.alpha=0.2f
            layGood.alpha=0.2f
            ratting=5
            submitRatting()
        }

        txtSubmit.setOnClickListener {
            if (ratting==0){
                CustomUtil.showTopSneakWarning(this,"Choose ratting first")
                return@setOnClickListener
            }
            submitRatting()
        }

        btnYes.setOnClickListener {

            if (ticket_model!=null){
                if (ticket_model!!.is_kyc_bank_reject.equals("yes",true)){
                    msg="Yes, I want to change my bank details."
                    reason="kyc_bank_reject_yes"
                }
                else  if (ticket_model!!.is_team_edit.equals("yes",true)){
                    msg="Yes, I want to change my team name."
                    reason="team_edit_yes"
                }
                else{
                    msg="Yes"
                    reason="close_ticket"
                }
            }
            createTickets()
        }

        btnNo.setOnClickListener {

            if (ticket_model!=null){
                if (ticket_model!!.is_kyc_bank_reject.equals("yes",true)){
                    msg="No"
                    reason="kyc_bank_reject_no"
                }
                else  if (ticket_model!!.is_team_edit.equals("yes",true)){
                    msg="No"
                    reason="team_edit_no"
                }
                else{
                    msg="No"
                    reason="open_ticket"
                }
            }
            createTickets()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (ticket_model!=null && !ticket_model!!.ticketStatus.equals("close",true) &&
            !ticket_model!!.ticketStatus.equals("Bot_Open",true))
            handler.removeCallbacks(runnable!!)
    }

    override fun initClick() {

    }

    private fun showFullImageDialog(model: ChatModel) {
        val dialog= Dialog(this,R.style.DialogTheme)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_image_video)

        // dialog.setCancelable(false)

        val back_btn = dialog.findViewById<ImageView>(R.id.back_btn)
        val full_img = dialog.findViewById<ImageView>(R.id.full_img)
        val videoView = dialog.findViewById<VideoView>(R.id.videoView)

        val image=model.documentsName
        val imageBase=MyApp.imageBase + MyApp.callback_support

        LogUtil.e("resp","image: $image \n imageBase: $imageBase")


        if (!TextUtils.isEmpty(image.trim { it <= ' ' })) {
            val arr=image.split(".")
            if (arr.size>=2){
                if (arr[1].equals("jpg",true) ||
                    arr[1].equals("jpeg",true) ||
                    arr[1].equals("png",true)){
                    full_img.visibility= View.VISIBLE
                    videoView.visibility=View.GONE
                    if (!URLUtil.isNetworkUrl(image.trim { it <= ' ' })) {
                        if (URLUtil.isNetworkUrl(imageBase + image)) {
                            Glide.with(mContext)
                                .load(imageBase + image)
                                .placeholder(R.drawable.user_image)
                                .error(R.drawable.user_image)
                                .into(full_img)
                        }
                    } else {
                        Glide.with(mContext)
                            .load(image)
                            .placeholder(R.drawable.user_image)
                            .error(R.drawable.user_image)
                            .into(full_img)
                    }
                }
                else if (arr[1].equals("mp4",true)){
                    full_img.visibility=View.GONE
                    videoView.visibility=View.VISIBLE
                    videoView.seekTo(0)
                    // videoView.setBackgroundColor(resources.getColor(R.color.black))
                    val url: String =imageBase+image
                    val cacheUrl: String = url//cachingUrl(url)!!
                    LogUtil.e("resp", "cacheUrl: $cacheUrl")
                    videoView.setVideoURI(Uri.parse(cacheUrl))
                    videoView.setMediaController(null)

                    videoView.setOnPreparedListener { mediaPlayer: MediaPlayer? ->
                        videoView.start()
                    }

                }
            }

        }

        back_btn.setOnClickListener { dialog.dismiss() }

        dialog.setOnDismissListener {
            if (videoView.visibility==View.VISIBLE){
                if (videoView != null && videoView.isPlaying) {
                    videoView.pause()
                }
            }
        }

        //dialog.getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        /* val params: ViewGroup.LayoutParams = dialog.getWindow()!!.getAttributes()
         params.width = WindowManager.LayoutParams.MATCH_PARENT
         params.height = WindowManager.LayoutParams.MATCH_PARENT*/

        dialog.show()
    }

    private fun checkStatus(){
        if (ticket_model!=null){
            if ( ticket_model!!.ticketStatus.equals("close",true)){
                if (TextUtils.isEmpty(ticket_model!!.userRating)){
                    layFooter.visibility=View.GONE
                    layRateAfter.visibility=View.GONE
                    layBot.visibility=View.GONE
                    if (!ticket_model!!.userId.equals(preferences.userModel.id)){
                        layRate.visibility=View.GONE
                    }else{
                        layRate.visibility=View.VISIBLE
                    }
                }else{
                    layFooter.visibility=View.GONE
                    layRate.visibility=View.GONE
                    layRateAfter.visibility=View.VISIBLE
                    layBot.visibility=View.GONE

                    displayRatted(ticket_model!!.userRating)
                }

                txtLblNoti.text="This ticket is marked as closed"
            }
            else if (ticket_model!!.ticketStatus.equals("open",true)){
                layFooter.visibility=View.GONE
                layRateAfter.visibility=View.GONE
                layRate.visibility=View.GONE
                layBot.visibility=View.GONE

                txtLblNoti.text="Our team will be assigned at the earliest"
            }
            else if (ticket_model!!.ticketStatus.equals("Bot_Open",true)){
                layFooter.visibility=View.GONE
                layRateAfter.visibility=View.GONE
                layRate.visibility=View.GONE
                layBot.visibility=View.VISIBLE

                txtLblNoti.text="Our team will be assigned at the earliest"
            }
            else{
                layFooter.visibility=View.VISIBLE
                layRate.visibility=View.GONE
                layRateAfter.visibility=View.GONE
                layBot.visibility=View.GONE

                txtLblNoti.text="Our team is looking into your matter"
            }
        }

    }
  /*  fun cachingUrl(urlPath: String?): String? {
        return MyApp.getProxy(mContext).getProxyUrl(urlPath, true)
    }*/

    var imageLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val uri = data.data
                try {
                    LogUtil.e(
                        "filse",
                        "uri: $uri"
                    )
                    // val selectedImageUri = data.data

                    val user_image_file = FileUtil.from(mContext, uri)
                    //selectedPath = user_image_file.absolutePath
                    // profile_img.setImageURI(uri);
                    val fileSizeInBytes: Long = user_image_file.length()
                    val fileSizeInKB = fileSizeInBytes / 1024
                    val fileSizeInMB = fileSizeInKB / 1024
                    LogUtil.e(
                        "filse",
                        "fileSizeInMB: $fileSizeInMB   fileSizeInKB:$fileSizeInKB"
                    )
                    if (fileSizeInMB < ConstantUtil.MAX_MEDIA_SIZE_MB) {
                        //  sendImage();
                        uploadMedia(selectedChatModel!!,user_image_file)
                        //uploadMedia1(selectedChatModel!!,user_image_file)
                    } else {
                        selectedChatModel = null
                        CustomUtil.showTopSneakWarning(mContext, "Choose Max ${ConstantUtil.MAX_MEDIA_SIZE_MB} MB file size")
                    }

                    //  callApi();
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getMimeType(path: String): String {
        val extension: String = MimeTypeMap.getFileExtensionFromUrl(path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
    }

    private fun uploadMedia(model: ChatModel,file:File) {
        try {
            isMediaUploading=true
            val params = HashMap<String, String>()
            params["user_id"] = preferences.userModel.id
            params["id"] = model.id
            if (ticket_model!=null){
                params["chatId"] = ticket_model!!.id
            }

            HttpRestClient.postDataFileWithVideo(this,true,
                ApiManager.upload_user_callbackDoc,params,file,"user_doc",object :GetApiResult{
                override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                    LogUtil.e("resp", "uploadMedia: $responseBody")
                    isMediaUploading=false
                    if (responseBody.optBoolean("status")){
                        getTicketsMsg(false)
                    }else{
                        CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"))
                    }
                }

                override fun onFailureResult(responseBody: String?, code: Int) {
                    isMediaUploading=false
                }
            })
        }catch (e :java.lang.Exception){
            e.printStackTrace()
        }

    }

    private fun submitRatting() {
        try {
            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            param.put("rating_msg","")
            param.put("user_rating",ratting)
            if (ticket_model!=null){
                param.put("ticket_id",ticket_model!!.id)
            }

            LogUtil.e("resp", "createTickets param : $param")
            HttpRestClient.postJSON(mContext,true,
                ApiManager.updateUserRating,param,object : GetApiResult {
                    override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                        LogUtil.e("resp", "createTickets: $responseBody")

                        if (responseBody.optBoolean("status")){
                            //finish()
                            layFooter.visibility=View.GONE
                            layRate.visibility=View.GONE
                            layRateAfter.visibility=View.VISIBLE
                            displayRatted(ratting.toString())
                        }
                    }

                    override fun onFailureResult(responseBody: String?, code: Int) {

                    }

                })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun createTickets() {
        try {
            val param= JSONObject()
            param.put("user_id",preferences.userModel.id)
            param.put("msg",msg)
            param.put("topic_id",topic_id)
            if (ticket_model!=null){
                param.put("clbk_id",ticket_model!!.id)
            }
            if (!TextUtils.isEmpty(faq_id)) {
                param.put("topic_faq_id", faq_id)
            }
            if (!TextUtils.isEmpty(topic_name)){
                param.put("topic_name",topic_name)
            }
            if (!TextUtils.isEmpty(trans_id)){
                param.put("trans_id",trans_id)
            }
            if (!TextUtils.isEmpty(reason))
                param.put("reason",reason)

            LogUtil.e("resp", "createTickets param : $param")
            HttpRestClient.postJSON(mContext,true,
                ApiManager.createTicket,param,object : GetApiResult {
                    override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                        LogUtil.e("resp", "createTickets: $responseBody")

                        if (responseBody.optBoolean("status")){
                            val data=responseBody.optJSONObject("data")
                            ticket_model=gson.fromJson(data.toString(),MyTickets::class.java)
                            if (ticket_model!=null)
                                toolbar_title.text = "#"+ticket_model!!.ticketId

                            //checkStatus()
                            getTicketsMsg(false)

                            if (!isTimer){
                                if (!ticket_model!!.ticketStatus.equals("close",true) &&
                                    !ticket_model!!.ticketStatus.equals("Bot_Open",true)){
                                    isTimer=true
                                    handler.postDelayed(Runnable { //do something
                                        LogUtil.e("resp","Running: ")
                                        getTicketsMsg(false)
                                        handler.postDelayed(runnable!!, delay.toLong())
                                    }.also { runnable = it }, delay.toLong())
                                }
                            }

                        }else{
                            CustomUtil.showToast(mContext,responseBody.optString("msg"))
                            finish()
                        }

                    }

                    override fun onFailureResult(responseBody: String?, code: Int) {

                    }

                })
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun displayRatted(rat:String){
        var msg=""
        var img=""//R.drawable.awful_emoji
        when (rat) {
            "1" -> {
                msg="Awful | "
                img="\uD83D\uDE20"//R.drawable.awful_emoji
            }
            "2" -> {
                msg="Bad | "
                img="☹️"//R.drawable.bad_emoji
            }
            "3" -> {
                msg="Okay | "
                img="\uD83D\uDE10"//R.drawable.okay_emoji
            }
            "4" -> {
                msg="Good | "
                img="\uD83D\uDE0A"//R.drawable.good_emoji
            }
            else -> {
                msg="Great | "
                img="\uD83E\uDD29"//R.drawable.great_emoji
            }
        }

        txtRatted.text = msg
        imgRatted.text=img //setImageResource(img)
    }

    private fun getTicketsMsg(isLoader:Boolean) {
        if (!isMediaUploading) {
            try {
                val param = JSONObject()
                val userId = preferences.userModel.id
                param.put("user_id", userId)
                if (ticket_model != null) {
                    param.put("clbk_id", ticket_model!!.id)
                }

                LogUtil.e("resp", "getTicketsMsg param : $param")
                HttpRestClient.postJSON(mContext, isLoader,
                    ApiManager.getMyTicketListMessageList, param, object : GetApiResult {
                        override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                            LogUtil.e("resp", "getTicketsMsg: $responseBody")

                            if (responseBody.optBoolean("status")) {
                                val data = responseBody.optJSONArray("data")
                                list!!.clear()
                                for (i in 0 until data.length()) {
                                    val obj = data.optJSONObject(i)
                                    val model = gson.fromJson<ChatModel>(
                                        obj.toString(),
                                        ChatModel::class.java
                                    )
                                    model.isMe = model.userId == userId
                                    list!!.add(model)
                                }
                                if (responseBody.has("ticket_list")) {
                                    ticket_model = gson.fromJson(
                                        responseBody.optJSONObject("ticket_list").toString(),
                                        MyTickets::class.java
                                    )
                                    checkStatus()
                                    adapter.changeStatus(ticket_model!!.ticketStatus)

                                    if (ticket_model!!.is_kyc_bank_reject.equals("yes",true)){
                                        txtBotQue.setText("Do you want to change your Bank Details?")
                                        /*if (isGetUser)
                                            getUserDetails()*/
                                    }
                                    else if (ticket_model!!.is_team_edit.equals("yes",true)){
                                        txtBotQue.setText("Do you want to change your Team Name?")
                                        /*if (isGetUser)
                                            getUserDetails()*/
                                    }
                                    else{
                                        txtBotQue.setText("Is your issue now resolved?")
                                    }

                                } else
                                    adapter!!.notifyDataSetChanged()
                                // checkStatus()
                            }
                        }

                        override fun onFailureResult(responseBody: String?, code: Int) {

                        }

                    })
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getUserDetails() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user_id", preferences.userModel.id)
            jsonObject.put("token_no", preferences.userModel.tokenNo)
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        HttpRestClient.postJSON(
            mContext,
            true,
            ApiManager.AUTHV3GetUserDetails,
            jsonObject,
            object : GetApiResult {
                override fun onSuccessResult(responseBody: JSONObject, code: Int) {
                    LogUtil.e(BaseFragment.TAG, "USER onSuccessResult: $responseBody")
                    if (responseBody.optBoolean("status")) {
                        if (responseBody.optString("code") == "200") {
                            val data = responseBody.optJSONObject("data")
                            val userModel = gson.fromJson(data.toString(), UserModel::class.java)
                            preferences.userModel = userModel
                        }
                    }

                }

                override fun onFailureResult(responseBody: String, code: Int) {

                }
            })
    }
}