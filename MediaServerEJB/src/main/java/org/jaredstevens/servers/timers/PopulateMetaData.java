package org.jaredstevens.servers.timers;

import com.mpatric.mp3agic.*;
import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.operations.AlbumOps;
import org.jaredstevens.servers.db.operations.ArtistOps;
import org.jaredstevens.servers.db.operations.ConfigurationOps;
import org.jaredstevens.servers.db.operations.SongOps;
import org.jaredstevens.servers.utilities.Utilities;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Stateless
@Remote
public class PopulateMetaData implements IPopulateMetaData {
	@Resource
	private SessionContext sessionCtx;
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Serializable startTimer() {
		Calendar now = Calendar.getInstance();
		long startTime = now.getTimeInMillis() / 1000;
		startTime += 10;

		TimerService timerService = this.sessionCtx.getTimerService();
		Timer timer = timerService.createTimer(10000, null);
//		Timer timer = timerService.createTimer(10000, 10000, null);
		System.out.println("Starting timer: "+startTime);
		return timer.getInfo();
	}

	public boolean stopTimer() {
		TimerService timerService = this.sessionCtx.getTimerService();
		Collection<Timer> timers = timerService.getTimers();
		for(Timer timer : timers) {
			timer.cancel();
		}
		return false;
	}

	public String getTimers() {
		String retVal = "";
		TimerService timerService = this.sessionCtx.getTimerService();
		Collection<Timer> timers = timerService.getTimers();
		for(Timer timer : timers) {
			retVal += timer.getHandle().toString();
			// toString()+"\n";
		}
		return retVal;
	}

	@Timeout
	public void populateMetaData(Timer timer) throws Exception {
		ConfigurationOps configConn = new ConfigurationOps();
		configConn.setEm(this.getEm());
		Configuration config = Utilities.getConfig(configConn);
		String path = config.getSongPath();
		org.jaredstevens.servers.db.entities.File fileRecord;
		SongOps songConn = new SongOps();
		ArtistOps artistConn = new ArtistOps();
		AlbumOps albumConn = new AlbumOps();
		artistConn.setEm(this.em);
		albumConn.setEm(this.em);
		songConn.setEm(this.em);
		List<Song> songs;
		String artistName;
		String albumName;
		String title;
		int trackNum;
		int albumTrackCount;
		ID3v2 tag;
		int page = 0;
		int songsProcessed = 0;
		Artist artistRecord;
		Album albumRecord;
		while((songs = songConn.getSongs(50,page)).size() > 0 && songsProcessed < 300) {
			++page;
			for(Song song : songs) {
				if(song.getAlbum().getTitle().equals("Unknown Album") && song.getAlbum().getArtist().getName().equals("Unknown Artist")) {
					fileRecord = song.getFile();
					tag = (ID3v2)this.getTag(Utilities.buildPath(path,fileRecord.getFilename()));
					if(tag == null) continue;
					artistName = PopulateMetaData.getArtist(tag);
					title = tag.getTitle();
					albumName = tag.getAlbum();
					trackNum = PopulateMetaData.getTrack(tag.getTrack());
					albumTrackCount = PopulateMetaData.getAlbumTrackCount(tag.getTrack());

					System.out.println("Looking up artist: "+artistName);
					artistRecord = artistConn.getArtistByName(artistName);
					if(!artistName.equals("Unknown Artist") && artistRecord == null) {
						artistRecord = artistConn.save(-1, artistName);
						System.out.println("Saving artist: "+artistName+". New ID: " + artistRecord.getId());
					}

					// New artist - getAlbums() == null
					// Existing artist, no albums - getAlbums == null
					// Existing artist, has albums, but not matching one - getAlbums != null but this.findAlbum returns null
					// Existing artist, has albums, and matching one - this.findAlbum returns Album
					albumRecord = null;
					List<Album> albums = albumConn.getArtistAlbums(artistRecord.getId());
					if(albums != null && albums.size() > 0) {
						albumRecord = this.findAlbum(albums, albumName);
					}
					if(albumRecord == null && albumName != null) {
						albumRecord = albumConn.save(-1, artistRecord.getId(), albumName, new Date(0L), albumTrackCount, 1);
						System.out.println("Saving album: "+albumName+". New ID: " + albumRecord.getId());
					}

					if(!artistRecord.getName().equals("Unknown Artist") && !albumRecord.getTitle().equals("Unknown Album")) {
						songConn.save(song.getId(), song.getFile().getId(), albumRecord.getId(), title, song.getDuration(), trackNum, song.getFingerprint());
						System.out.println("Saving song: "+title+". New ID: " + song.getId());
					}
					++songsProcessed;
					System.out.println("Processed file: "+fileRecord.getFilename());
				}
			}
		}
	}

	public Album findAlbum(List<Album> albums, String albumName) {
		Album retVal = null;
		for(Album album : albums) {
			if(album.getTitle().equals(albumName)) {
				retVal = album;
				break;
			}
		}
		return retVal;
	}

	public static String getArtist(ID3v1 tag) {
		String artist;
		if(tag.getClass().getSimpleName() == "ID3v23Tag") {
			// Process v2 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
			else if(((ID3v2)tag).getAlbumArtist() != null) artist = ((ID3v2)tag).getAlbumArtist();
			else if(((ID3v2)tag).getOriginalArtist() != null) artist = ((ID3v2)tag).getOriginalArtist();
			else artist = "Unknown Artist";
		} else {
			// Process v1 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
			else artist = "Unknown Artist";
		}
		return artist;
	}

	public static int getTrack(String track) {
		int retVal = -1;
		if(track.contains("/")) {
			retVal = Integer.valueOf(track.split("/")[0]);
		} else {
			// Check for number format exception
			try {
				retVal = Integer.valueOf(track);
			} catch(NumberFormatException e) {
				System.out.println("Couldn't parse the track.");
			}
		}
		return retVal;
	}

	public static int getAlbumTrackCount(String track) {
		int retVal = -1;
		if(track.contains("/")) {
			retVal = Integer.valueOf(track.split("/")[1]);
		}
		return retVal;
	}

	public ID3v1 getTag(String filename) {
		ID3v1 retVal = null;
		Mp3File fileData = null;
		try {
			fileData = new Mp3File(filename);
		} catch(InvalidDataException e) {
			System.out.println("InvalidDataException: "+e.getMessage());
		} catch(UnsupportedTagException e) {
			System.out.println("UnsupportedTagException: "+e.getMessage());
		} catch(FileNotFoundException e) {
			System.out.println("Skipping missing file: "+filename+"\n"+e.getMessage());
		} catch(IOException e) {
			System.out.println("IOException: "+e.getMessage());
		}
		if(fileData != null) {
			if(fileData.hasId3v2Tag()) retVal = fileData.getId3v2Tag();
			else if(fileData.hasId3v1Tag()) retVal = fileData.getId3v1Tag();
		}
		return retVal;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
