package org.jaredstevens.apps.mediaserver.models;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Recording {
	private String artistName;
	private String albumName;
	private Date albumReleaseDate;
	private int albumTrackCount;
	private int albumDiscNum;
	private String songTitle;
	private int trackNum;
	private int duration;
	private String fingerprint;

	/* Getters and setters */
	public String getArtistName() { return artistName; }
	public void setArtistName(String artistName) { this.artistName = artistName; }
	public String getAlbumName() { return albumName; }
	public void setAlbumName(String albumName) { this.albumName = albumName; }
	public Date getAlbumReleaseDate() { return albumReleaseDate; }
	public void setAlbumReleaseDate(Date albumReleaseDate) { this.albumReleaseDate = albumReleaseDate; }
	public int getAlbumTrackCount() { return albumTrackCount; }
	public void setAlbumTrackCount(int albumTrackCount) { this.albumTrackCount = albumTrackCount; }
	public int getAlbumDiscNum() { return albumDiscNum; }
	public void setAlbumDiscNum(int albumDiscNum) { this.albumDiscNum = albumDiscNum; }
	public String getSongTitle() { return songTitle; }
	public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
	public int getTrackNum() { return trackNum; }
	public void setTrackNum(int trackNum) { this.trackNum = trackNum; }
	public int getDuration() { return duration; }
	public void setDuration(int duration) { this.duration = duration; }
	public String getFingerprint() { return fingerprint; }
	public void setFingerprint(String fingerprint) { this.fingerprint = fingerprint; }

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		Date albumReleaseDate = this.getAlbumReleaseDate();
		String releaseDate;
		if(albumReleaseDate != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			releaseDate = df.format(albumReleaseDate);
		} else releaseDate = "null";

		retVal.append("artistName: ")
				.append(this.getArtistName())
				.append(" albumName: ")
				.append(this.getAlbumName())
				.append(" albumReleaseDate: ")
				.append(releaseDate)
				.append(" albumTrackCount: ")
				.append(this.getAlbumTrackCount())
				.append(" albumDiscNum: ")
				.append(this.getAlbumDiscNum())
				.append(" songTitle: ")
				.append(this.getSongTitle())
				.append(" trackNum: ")
				.append(this.getTrackNum())
				.append(" duration: ")
				.append(this.getDuration())
				.append(" fingerprint: ")
				.append(this.getFingerprint());
		return retVal.toString();
	}
}
