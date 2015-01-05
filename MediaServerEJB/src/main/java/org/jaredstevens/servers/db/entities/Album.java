package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Column(nullable=false)
	private String title;

	@Column(name="release_date",nullable=false)
	private Date releaseDate;

	@Column(name="track_count",nullable=false)
	private int trackCount;

	@Column(name="disc_num",nullable=false)
	private int discNum;

	@OneToMany(mappedBy = "album")
	@OrderBy("trackNum,id ASC")
	private Set<Song> songs;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() { return title; }

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	public int getDiscNum() {
		return discNum;
	}

	public void setDiscNum(int discNum) {
		this.discNum = discNum;
	}

	public Set<Song> getSongs() { return songs;	}

	public void setSongs(Set<Song> songs) {	this.songs = songs;	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		Date albumReleaseDate = this.getReleaseDate();
		String releaseDate;
		if(albumReleaseDate != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			releaseDate = df.format(albumReleaseDate);
		} else releaseDate = "null";
		retVal.append("id: ")
				.append(this.getId())
				.append(" title: ")
				.append(this.getTitle())
				.append(" releaseDate: ")
				.append(releaseDate)
				.append(" trackCount: ")
				.append(this.getTrackCount())
				.append(" discNum: ")
				.append(this.getDiscNum());
		return retVal.toString();
	}
}
