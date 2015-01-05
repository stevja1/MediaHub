package org.jaredstevens.apps.mediaserver.models;

import org.jaredstevens.servers.db.entities.Song;

import java.util.ArrayList;
import java.util.List;

public class Song_JSON {
	private long id;
	private String title;
	private int duration;
	private int trackNum;
	private Album_JSON album;
	private File_JSON file;
	private Artist_JSON artist;

	public static Song_JSON songFactory(Song song) {
		Song_JSON retVal = new Song_JSON();
		retVal.setId(song.getId());
		retVal.setTitle(song.getTitle());
		retVal.setDuration(song.getDuration());
		retVal.setTrackNum(song.getTrackNum());
		retVal.setAlbum(Album_JSON.albumFactory(song.getAlbum()));
		retVal.setFile(File_JSON.fileFactory(song.getFile()));
		retVal.setArtist(Artist_JSON.artistFactory(song.getArtist()));
		return retVal;
	}

	public static List<Song_JSON> songFactory(List<Song> songs) {
		List<Song_JSON> retVal = null;
		for(Song song : songs) {
			if(retVal == null) retVal = new ArrayList<Song_JSON>();
			retVal.add(Song_JSON.songFactory(song));
		}
		return retVal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Album_JSON getAlbum() {
		return album;
	}

	public void setAlbum(Album_JSON album) {
		this.album = album;
	}

	public File_JSON getFile() {
		return file;
	}

	public void setFile(File_JSON file) {
		this.file = file;
	}

	public Artist_JSON getArtist() { return artist;	}

	public void setArtist(Artist_JSON artist) { this.artist = artist; }
}
