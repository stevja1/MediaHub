package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "song")
public class Song implements Serializable {
	private long id;
	private String title;
	private String fingerprint;
	private int duration;
	private int trackNum;
	private Album album;
	private File file;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false,length = 4000)
	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	@Column(nullable = false)
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Column(name = "track_num", nullable = false)
	public int getTrackNum() {
		return trackNum;
	}

	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}

	@ManyToOne(optional = false,targetEntity = Album.class)
	@JoinColumn(name="album_id",nullable=false)
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@ManyToOne(optional=true,targetEntity = File.class)
	@JoinColumn(name="file_id",nullable=false)
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
