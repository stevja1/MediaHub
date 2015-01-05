package org.jaredstevens.apps.mediaserver.models;

public class Chromaprint {
	private String fingerprint;
	private String duration;
	public Chromaprint() {
		this.setFingerprint("");
		this.setDuration("");
	}

	public Chromaprint(String fingerprint, String duration) {
		this.setFingerprint(fingerprint);
		this.setDuration(duration);
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
