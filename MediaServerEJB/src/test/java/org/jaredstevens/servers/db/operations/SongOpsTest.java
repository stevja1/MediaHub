package org.jaredstevens.servers.db.operations;

import static org.junit.Assert.*;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jaredstevens.servers.db.entities.Song;

public class SongOpsTest {
	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		// Setup an EntityManager
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("MediaDBTest-PU", System.getProperties());
		this.em = factory.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		this.em.getEntityManagerFactory().close();
	}

	@Test
	public void getByIdTest() {
		SongOps conn = new SongOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record.");
		Album album = OpsUtils.saveAlbumRecord(artist, this.em);
		if(album == null || album.getId() <= 0) fail("Failed to save album record.");
		File file = OpsUtils.saveFileRecord(this.em);
		if(file == null || file.getId() <= 0) fail("Failed to save file record.");
		Song song = OpsUtils.saveSongRecord(album, file, this.em);
		if(song == null || song.getId() <= 0) fail("Failed to save song.");
		Song song2 = conn.getById(song.getId());
		if(song2 == null || song2.getId() != song.getId()) fail("Failed to fetch the song.");
		this.em.getTransaction().rollback();
	}

	@Test
	public void saveTest() {
		SongOps conn = new SongOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record.");
		Album album = OpsUtils.saveAlbumRecord(artist, this.em);
		if(album == null || album.getId() <= 0) fail("Failed to save album record.");
		File file = OpsUtils.saveFileRecord(this.em);
		if(file == null || file.getId() <= 0) fail("Failed to save file record.");
		Song song = OpsUtils.saveSongRecord(album, file, this.em);
		if(song == null || song.getId() <= 0) fail("Failed to save song.");
		this.em.getTransaction().rollback();
	}

	@Test
	public void removeTest() {
		SongOps conn = new SongOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record.");
		Album album = OpsUtils.saveAlbumRecord(artist, this.em);
		if(album == null || album.getId() <= 0) fail("Failed to save album record.");
		File file = OpsUtils.saveFileRecord(this.em);
		if(file == null || file.getId() <= 0) fail("Failed to save file record.");
		Song song = OpsUtils.saveSongRecord(album, file, this.em);
		if(song == null || song.getId() <= 0) fail("Failed to save song.");
		conn.remove(song.getId());
		Song song2 = conn.getById(song.getId());
		if(song2 != null && song2.getId() > 0) fail("Failed to fetch the song.");
		this.em.getTransaction().rollback();
	}
}
