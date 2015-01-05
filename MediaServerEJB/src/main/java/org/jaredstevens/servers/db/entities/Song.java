package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "song")
public class Song implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@ManyToOne(optional = false,targetEntity = Album.class)
	@JoinColumn(name="album_id",nullable=false)
	private Album album;

	@ManyToOne(optional = false,targetEntity = Artist.class)
	@JoinColumn(name="artist_id",nullable=false)
	private Artist artist;

	@ManyToOne(optional=true,targetEntity = File.class)
	@JoinColumn(name="file_id",nullable=false)
	private File file;

	@ManyToMany(targetEntity = org.jaredstevens.servers.db.entities.User.class)
	@JoinTable(name="user_songs")
	private Set<User> users;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false,length = 4000)
	private String fingerprint;

	@Column(nullable = false)
	private int duration;

	@Column(name = "track_num", nullable = false)
	private int trackNum;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getTrackNum() {
		return trackNum;
	}
	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}
	public Set<User> getUsers() { return users; }
	public void setUsers(Set<User> users) { this.users = users; }
}
