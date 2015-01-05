package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.*;

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

	public static Album saveAlbumRecord(EntityManager em) {
		Album album = null;
		AlbumOps conn = new AlbumOps();
		conn.setEm(em);

		long id = -1;
		String title = "Happy Pills";
		Date releaseDate = new Date(1390096747000L);
		int trackCount = 1;
		int discNum = 1;

		try {
			album = conn.save(id, title, releaseDate, trackCount, discNum);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return album;
	}

	public static Song saveSongRecord(User user, Artist artist, Album album, File file, EntityManager em) {
		if(artist == null || album == null || file == null || em == null) return null;
		Song song = null;
		SongOps conn = new SongOps();
		conn.setEm(em);

		long id = -1;
		long fileId = file.getId();
		long albumId = album.getId();
		long artistId = artist.getId();
		long userId = user.getId();
		String title = "Happy Pills";
		int duration = 195;
		int trackNum = 1;
		String fingerprint = "ABCDEFjerkiphgjkehajgsdlhlkjh";

		try {
			song = conn.save(id, userId, albumId, artistId, fileId, title, duration, trackNum, fingerprint);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return song;
	}

	public static User saveUserRecord(EntityManager em) {
		UserOps conn = new UserOps();
		conn.setEm(em);
		long id = -1;
		String firstName = "Jared", lastName = "Stevens", OAuthId = "1234567";
		User user;
		try {
			user = conn.save(id, firstName, lastName, OAuthId);
		} catch(Exception e) {
			user = null;
		}
		return user;
	}

	public static File saveFileRecord(EntityManager em) {
		FileOps conn = new FileOps();
		conn.setEm(em);

		long id = -1;
		String filename = "/home/jstevens/01 - Happy Pills.mp3";
		File.FileType fileType = File.FileType.SONG;

		return conn.save(id, filename, fileType);
	}
}
