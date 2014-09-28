package org.jaredstevens.servers.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="configuration")
public class Configuration implements Serializable {
	@Id
	private long id;

	@Column(name="media_root")
	private String mediaRoot;

	@Column(name="scripts_root")
	private String scriptsRoot;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMediaRoot() {
		return mediaRoot;
	}

	public void setMediaRoot(String mediaRoot) {
		this.mediaRoot = mediaRoot;
	}

	public String getScriptsRoot() {
		return scriptsRoot;
	}

	public void setScriptsRoot(String scriptsRoot) {
		this.scriptsRoot = scriptsRoot;
	}
}
