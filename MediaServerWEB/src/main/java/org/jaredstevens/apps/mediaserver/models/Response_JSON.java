package org.jaredstevens.apps.mediaserver.models;

public class Response_JSON {
	public enum Status {
		SUCCESS, ERROR
	}
	private Status status;
	private String message;
	private Object data;

	public Response_JSON() {
		this.setStatus(Status.SUCCESS);
		this.setMessage("");
	}
	public Response_JSON(Status status, String message) {
		this.setStatus(status);
		this.setMessage(message);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
