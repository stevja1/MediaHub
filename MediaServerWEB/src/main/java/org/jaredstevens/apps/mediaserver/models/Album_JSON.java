package org.jaredstevens.apps.mediaserver.models;

import org.jaredstevens.servers.db.entities.Album;

import java.sql.Date;
import java.util.*;

public class Album_JSON {
	private long id;
	private Artist_JSON artist;
	private String title;
	private Date releaseDate;
	private int trackCount;
	private int discNum;

	public static Album_JSON albumFactory(Album album) {
		Album_JSON retVal = new Album_JSON();
		retVal.setId(album.getId());
		retVal.setArtist(Artist_JSON.artistFactory(album.getArtist()));
		retVal.setTitle(album.getTitle());
		retVal.setReleaseDate(album.getReleaseDate());
		retVal.setTrackCount(album.getTrackCount());
		retVal.setDiscNum(album.getDiscNum());
		return retVal;
	}

	public static List<Album_JSON> albumFactory(List<Album> albums) {
		ArrayList<Album_JSON> retVal = null;
		for(Album album : albums) {
			if(retVal == null) retVal = new ArrayList<Album_JSON>();
			retVal.add(Album_JSON.albumFactory(album));
		}
		return retVal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Artist_JSON getArtist() {
		return artist;
	}

	public void setArtist(Artist_JSON artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

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
}
