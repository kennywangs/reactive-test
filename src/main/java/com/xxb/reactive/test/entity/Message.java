package com.xxb.reactive.test.entity;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 177570782104136718L;
	
	private String id;
	
	private String from;
	
	private String to;
	
	private String event = "message";
	
	private Short type;
	
	private Object payload;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
