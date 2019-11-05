package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * WebSocket message model
 */
public class Message {
    private Integer onlineCount;
    private String type;
    private String username;
    private String msg;

    public Message() {}

    public Message(final Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Message (final String jsonAsString) throws JSONException {
        JSONObject messageAsJson = JSONObject.parseObject(jsonAsString);
        final String messageType = messageAsJson.getString("type");
        this.username = messageAsJson.getString("username");
        // When logging in, we send a message with type "ENTER" to make sure we broadcast when user logs in
        if (messageType != null) {
            this.type = messageType;
            this.msg = this.username +  " joined the chat";
        } else {
            this.msg = messageAsJson.getString("msg");
            this.type = "SPEAK";
        }
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(final Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public String getJSONMessageToString() {
        final JSONObject messageAsJson = new JSONObject();
        if (this.type != null && this.type.equals("SPEAK")) {
            messageAsJson.put("username", this.username);
        }
        messageAsJson.put("type", this.type);
        messageAsJson.put("msg", this.msg);
        messageAsJson.put("onlineCount", this.onlineCount);
        return messageAsJson.toJSONString();
    }

}
