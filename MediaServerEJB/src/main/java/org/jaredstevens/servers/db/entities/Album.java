package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album implements Serializable {
	private long id;
	private Artist artist;
	private String title;
	private Date releaseDate;
	private int trackCount;
	private int discNum;
	private Set<Song> songs;

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

	@ManyToOne(optional = false,targetEntity = Artist.class)
	@JoinColumn(name = "artist_id",nullable = false)
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	@Column(nullable=false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="release_date",nullable=false)
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name="track_count",nullable=false)
	public int getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	@Column(name="disc_num",nullable=false)
	public int getDiscNum() {
		return discNum;
	}

	public void setDiscNum(int discNum) {
		this.discNum = discNum;
	}

	@OneToMany(mappedBy = "album")
	@OrderBy("trackNum,id ASC")
	public Set<Song> getSongs() {
		return songs;
	}

	public void setSongs(Set<Song> songs) {
		this.songs = songs;
	}
}
