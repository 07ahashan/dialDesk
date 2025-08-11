package com.appedu.dialdesk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appedu.dialdesk.R
import com.appedu.dialdesk.databinding.ActivityVideoCallBinding
import com.appedu.dialdesk.utils.Utility
import com.google.android.material.textfield.TextInputLayout
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.core.invite.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import com.zegocloud.uikit.service.express.IExpressEngineEventHandler
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import org.json.JSONObject
import timber.log.Timber
import kotlin.random.Random

class VideoCallActivity : AppCompatActivity() {

    private lateinit var _bindingVideoCall : ActivityVideoCallBinding
    private var userId: String ?= null
    private var userName : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindingVideoCall = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(_bindingVideoCall.root)

        val yourUserID = _bindingVideoCall.yourUserId
        userId = Random.nextInt(10000, 99999).toString()
        userName = "User_${userId}"
        yourUserID.text = getString(R.string.your_user_id, userId)
    }

    override fun onStart() {
        super.onStart()
        initVideoButton()
        initVoiceButton()
        initCallInviteService(Utility.appId, Utility.appSign, userId.toString(), userName.toString())
    }

    private fun initVideoButton() {
        val newVideoCall = findViewById<ZegoSendCallInvitationButton>(R.id.new_video_call)
        newVideoCall.setIsVideoCall(true)

        newVideoCall.resourceID = "zego_data"

        newVideoCall.setOnClickListener { view ->
            val inputLayout = findViewById<TextInputLayout>(R.id.target_user_id)
            val targetUserID = inputLayout.editText?.text.toString()
            val split = targetUserID.split(",")
            val users = ArrayList<ZegoUIKitUser>()
            for (userID in split) {
                println("userID=$userID")
                val userName = "User_${userID}"
                users.add(ZegoUIKitUser(userID, userName))
            }
            newVideoCall.setInvitees(users)
        }
    }

    private fun initVoiceButton() {
        val newVoiceCall = findViewById<ZegoSendCallInvitationButton>(R.id.new_voice_call)
        newVoiceCall.setIsVideoCall(false)

        newVoiceCall.resourceID = "zego_data"

        newVoiceCall.setOnClickListener { view ->
            val inputLayout = findViewById<TextInputLayout>(R.id.target_user_id)
            val targetUserID = inputLayout.editText?.text.toString()
            val split = targetUserID.split(",")
            val users = ArrayList<ZegoUIKitUser>()
            for (userID in split) {
                println("userID=$userID")
                val userName = "User_${userID}"
                users.add(ZegoUIKitUser(userID, userName))
            }
            newVoiceCall.setInvitees(users)
        }
    }

    private fun initCallInviteService(
        appID: Long, appSign: String, userID: String, userName: String
    ) {
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
            provider =
                ZegoUIKitPrebuiltCallConfigProvider { invitationData -> getConfig(invitationData) }
        }

        ZegoUIKitPrebuiltCallService.events.errorEventsListener =
            ErrorEventsListener { errorCode, message -> Timber.d("onError() called with: errorCode = [$errorCode], message = [$message]") }

        ZegoUIKitPrebuiltCallService.events.invitationEvents.pluginConnectListener =
            SignalPluginConnectListener { state, event, extendedData -> Timber.d("onSignalPluginConnectionStateChanged() called with: state = [$state], event = [$event], extendedData = [$extendedData]") }

        ZegoUIKitPrebuiltCallService.init(
            application,
            appID,
            appSign,
            userID,
            userName,
            callInvitationConfig
        )

        ZegoUIKitPrebuiltCallService.events.callEvents.callEndListener =
            CallEndListener { callEndReason, jsonObject -> Timber.d("onCallEnd() called with: callEndReason = [$callEndReason], jsonObject = [$jsonObject]") }

        ZegoUIKitPrebuiltCallService.events.callEvents.setExpressEngineEventHandler(object :
            IExpressEngineEventHandler() {
            override fun onRoomStateChanged(
                roomID: String,
                reason: ZegoRoomStateChangedReason,
                errorCode: Int,
                extendedData: JSONObject
            ) {
                Timber.d("onRoomStateChanged() called with: roomID = [$roomID], reason = [$reason], errorCode = [$errorCode], extendedData = [$extendedData]")
            }
        })
    }

    private fun getConfig(invitationData: ZegoCallInvitationData):
            ZegoUIKitPrebuiltCallConfig {
        val isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.value
        val isGroupCall = invitationData.invitees.size > 1
        return when {
            isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVideoCall()
            !isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVoiceCall()
            !isVideoCall -> ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall()
            else -> ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallService.endCall()
    }
}