package com.sicco.erp.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
	String notify_type;
	String msg_type;
	String content;
	String url;
	String state;

	public NotificationModel(String notify, String msg, String content, String url, String state) {
		this.notify_type = notify;
		this.msg_type = msg;
		this.content = content;
		this.url = url;
		this.state = state;
	}

	public boolean equalsContent(NotificationModel other) {
		return ((this.notify_type.equals(other.notify_type))
				&& (this.msg_type.equals(other.msg_type)) 
				&& (this.content.equals(other.content))
				&& (this.url.equals(other.url)));
	}

	public String toString() {
		String ret = "NotificationModel: " +
				"notify_type = " + notify_type + "," 
				+ " msg_type = " + msg_type + ", " 
				+ " content = " + content + ", "
				+ " url = " + url + ", " 
				+ " state = " + state;
		return ret;
	}
	
	public String getMsg(){
		return msg_type;
	}
	public String getContent(){
		return content;
	}
	public String getUrl(){
		return url;
	}
	public String getNotify(){
		return notify_type;
	}
}
