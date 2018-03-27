package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 第三方小米推送的Util
 * @author chenqian
 * @date 2018-03-20 16:47
 * 目前没有开通此模块
 */

@Component
public class MiUtil {

    //注册时获取的信息
    @Value("${my.android.secret}")
    private String android_app_secret;

    @Value("${my.ios.secret}")
    private String ios_app_secret;

    @Value("${my.android.package}")
    private String android_package_name;

    // 1=正式环境/0=测试
    @Value("${my.development.type}")
    private int developmentType;

    //机型
    public final static int TYPE_ANDROID = 0; //安卓
    public final static int TYPE_IOS = 1; //IOS

    /**
     * 判断环境状态，调用小米推送
     */
    public void reStartPush(int deviceType) {
        // 如果为测试环境
        if (developmentType == 0) {
            // 测试环境只提供对IOS支持，不支持Android
            Constants.useSandbox();
            if (deviceType == TYPE_ANDROID) {
                Constants.useOfficial();
            }
        } else {
            // 正式环境
            Constants.useOfficial();
        }
    }

    /**
     * 构建发送信息
     * @param title
     * @param content
     * @param payload
     * @param deviceType
     * @param timeToSend
     * @return Message
     */
    private Message buildMessage(String title, String content, JSONObject payload, int deviceType,
                                 long timeToSend) throws Exception {
        Message message = null;
        if (deviceType == TYPE_ANDROID) {
            message = buildMessage2Android(title, content, payload, timeToSend);
        } else if (deviceType == TYPE_IOS) {
            message = buildMessage2IOS(content, payload, timeToSend);
        }
        return message;
    }

    /**
     * 构建android推送信息
     * @param title
     * @param content
     * @param payload
     * @param timeToSend
     * @return
     */
    private Message buildMessage2Android(String title, String content, JSONObject payload, long timeToSend)
            throws Exception {
        Message message = new Message.Builder()
                .title(title) //标题
                .description(content) //设置在通知栏展示的通知的描述
                .payload(payload.toJSONString())
                .restrictedPackageName(android_package_name) // 设置包名
                .passThrough(MiConfigEnum.notificationMessage.getIndex()) // 消息使用通知栏方式
                .notifyType(MiConfigEnum.defaultVibrate.getIndex()) // 使用默认震动提示
                .notifyType(MiConfigEnum.defaultAll.getIndex()) // 使用默认呼吸灯
                .enableFlowControl(true) // 控制消息是否需要进行平缓发送
                .timeToSend(timeToSend) // 定时推送时间
                .build();
        return message;
    }

    /**
     * 构建ios推送信息
     * @param content
     * @param payload
     * @param timeToSend
     * @return
     */
    private Message buildMessage2IOS(String content, JSONObject payload, long timeToSend) throws Exception {
        Message message = new Message.IOSBuilder()
                .description(content) //通知描述
                .soundURL("") //自定义铃声
                .badge(1) // 数字角标
                .extra("payload", payload.toJSONString())
                .timeToSend(timeToSend).build();
        return message;
    }

    /**
     * 1.向所有设备发送推送信息
     * @param title 消息标题
     * @param content 消息描述
     * @param payload 消息内容
     * @param deviceType 环境类型 0/1
     * @param timeToSend 定时推送时间
     * @throws Exception
     */
    public JSONObject sendBroadcastAll(String title, String content, JSONObject payload, int deviceType,
                                       long timeToSend) throws Exception {
        reStartPush(deviceType);// 准备小米推送

        Sender sender = null;
        if (deviceType == TYPE_ANDROID) {
            sender = new Sender(android_app_secret); // 需要根据appSecret来发送
        } else if (deviceType == TYPE_IOS) {
            sender = new Sender(ios_app_secret); // 需要根据appSecret来发送
        }
        Message message = buildMessage(title, content, payload, deviceType, timeToSend);
        Result result = sender.broadcastAll(message, 0);// 推送消息给所有设备，不重试
        return handlerResult(result);
    }

    /**
     * 2.根据regId发送一条短信
     * @param title 消息标题
     * @param content 消息描述
     * @param payload 消息内容
     * @param regId
     * @param deviceType 环境类型 0/1
     * @param timeToSend 定时推送时间
     * @return JSONObject
     */
    public JSONObject sendMessageToRegId(String title, String content, JSONObject payload, String regId, int deviceType,
                                         long timeToSend) throws Exception {
        reStartPush(deviceType);// 准备小米推送

        Sender sender = null;
        if (deviceType == TYPE_ANDROID) {
            sender = new Sender(android_app_secret); // 需要根据appSecret来发送
        } else if (deviceType == TYPE_IOS) {
            sender = new Sender(ios_app_secret); // 需要根据appSecret来发送
        }
        Message message = buildMessage(title, content, payload, deviceType, timeToSend);
        Result result = sender.send(message, regId, 0); // 根据regID，发送消息到指定设备上，不重试。
        return handlerResult(result);
    }

    /**
     * 3.根据alias发送一条短信
     * @param title
     * @param content
     * @param payload
     * @param userMobile
     * @param deviceType
     * @param timeToSend
     * @return JSONObject
     */
    private JSONObject sendMessageToAlias(String title, String content, JSONObject payload, String userMobile,
                                          int deviceType, long timeToSend) throws Exception{
        reStartPush(deviceType);// 准备小米推送

        Sender sender = null;
        if (deviceType == TYPE_ANDROID) {
            sender = new Sender(android_app_secret); // 需要根据appSecret来发送
        } else if (deviceType == TYPE_IOS) {
            sender = new Sender(ios_app_secret); // 需要根据appSecret来发送
        }
        Message message = buildMessage(title, content, payload, deviceType, timeToSend);
        Result result = sender.sendToAlias(message, userMobile, 0); // 根据alias，发送消息到指定设备上，不重试。
        return handlerResult(result);
    }

    //统一处理返回数据格式
    private JSONObject handlerResult(Result result){
        JSONObject dataResult = new JSONObject();
        dataResult.put("Data", result.getData());
        dataResult.put("MessageId", result.getMessageId());
        dataResult.put("Reason", result.getReason());
        dataResult.put("ErrorCode", result.getErrorCode());
        return dataResult;
    }

}
