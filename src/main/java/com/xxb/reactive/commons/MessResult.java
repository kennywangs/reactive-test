package com.xxb.reactive.commons;

import java.io.Serializable;

public class MessResult implements Serializable {
	
	private static final long serialVersionUID = -4306397426870629336L;
	/**
	 * 操作结果是否成功
	 */
	private boolean success;
	/**
	 * 返回提示消息
	 */
	private String msg;
	/**
	 * 返回其他对象
	 */
	private Object data;
	
	public MessResult() {}
	
	public MessResult(boolean success, String msg) {
		this.setSuccess(success);
		this.setMsg(msg);
		this.setData(null);
	}
	
	public MessResult(boolean success, String msg, Object data) {
		this.setSuccess(success);
		this.setMsg(msg);
		this.setData(data);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
