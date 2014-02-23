package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.File;
import org.jaredstevens.servers.db.entities.Song;

import javax.persistence.EntityManager;
import java.sql.Date;

public class OpsUtils {
	public static Artist saveArtistRecord(EntityManager em) {
		Artist artist = null;
		ArtistOps conn = new ArtistOps();
		conn.setEm(em);
		long id = -1;
		String name = "Norah Jones";
		try {
			artist = conn.save(id, name);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return artist;
	}

	public static Album saveAlbumRecord(Artist artist, EntityManager em) {
		Album album = null;
		AlbumOps conn = new AlbumOps();
		conn.setEm(em);

		long id = -1;
		String title = "Happy Pills";
		Date releaseDate = new Date(1390096747000L);
		int trackCount = 1;
		int discNum = 1;

		try {
			album = conn.save(id, artist.getId(), title, releaseDate, trackCount, discNum);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return album;
	}

	public static Song saveSongRecord(Album album, File file, EntityManager em) {
		Song song = null;
		SongOps conn = new SongOps();
		conn.setEm(em);

		long id = -1;
		long fileId = file.getId();
		long albumId = album.getId();
		String title = "Happy Pills";
		int duration = 195;
		int trackNum = 1;
		String fingerprint = "ABCDEFjerkiphgjkehajgsdlhlkjh";

		try {
			song = conn.save(id, fileId, albumId, title, duration, trackNum, fingerprint);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return song;
	}

	public static File saveFileRecord(EntityManager em) {
		FileOps conn = new FileOps();
		conn.setEm(em);

		long id = -1;
		String filename = "/home/jstevens/01 - Happy Pills.mp3";
		File.FileType fileType = File.FileType.SONG;

		File file = conn.save(id, filename, fileType);
		return file;
	}
}
