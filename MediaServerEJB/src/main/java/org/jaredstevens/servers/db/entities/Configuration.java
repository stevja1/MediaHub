package org.jaredstevens.servers.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="configuration")
public class Configuration implements Serializable {
	private long id;
	private String songPath;

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="song_path")
	public String getSongPath() {
		return songPath;
	}

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}
}
