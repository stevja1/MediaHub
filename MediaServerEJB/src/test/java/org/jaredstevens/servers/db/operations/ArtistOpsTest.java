package org.jaredstevens.servers.db.operations;

import static org.junit.Assert.*;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.File;
import org.jaredstevens.servers.db.entities.Song;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jaredstevens.servers.db.entities.Artist;

import java.util.ArrayList;

public class ArtistOpsTest {
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
		ArtistOps conn = new ArtistOps();
		conn.setEm(this.em);

		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record");
		Artist artist2 = conn.getById(artist.getId());
		if(artist2 == null || artist2.getId() <= 0) fail("Failed to get artist data.");
		this.em.getTransaction().rollback();
	}
	
	@Test
	public void testArtistRelationships() {
		ArtistOps conn = new ArtistOps();
		conn.setEm(this.em);

		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		Album album = OpsUtils.saveAlbumRecord(artist, this.em);
		File file = OpsUtils.saveFileRecord(this.em);
		Song song = OpsUtils.saveSongRecord(album, file, this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record");
		this.em.flush();
		Artist artist2 = conn.getById(artist.getId());
		if(artist2 == null || artist2.getId() <= 0) fail("Failed to get artist data.");
		if(artist2.getAlbums() == null || artist2.getAlbums().size() <= 0) fail("Failed to get album data linked to artist.");
		Album[] albums = artist2.getAlbums().toArray(new Album[artist2.getAlbums().size()]);
		if(albums[0].getSongs() == null || albums[0].getSongs().size() <= 0) fail("Failed to get song data linked to album.");
		this.em.getTransaction().rollback();
	}

	@Test
	@Ignore
	public void getByExistingIdTest() {
		ArtistOps conn = new ArtistOps();
		conn.setEm(this.em);

		this.em.getTransaction().begin();
		long artistId = 45851; // Make sure you set this to an artist that already exists and has an album and songs
		Artist artist2 = conn.getById(artistId);
		if(artist2 == null || artist2.getId() <= 0) fail("Failed to get artist data.");
		Album[] albums = artist2.getAlbums().toArray(new Album[artist2.getAlbums().size()]);
		if(albums == null || albums.length <= 0) fail("No albums returned.");
		Song[] songs = albums[0].getSongs().toArray(new Song[albums[0].getSongs().size()]);
		if(songs == null || songs.length <= 0) fail("No songs were returned.");
		System.out.println("Song: "+songs[0].getTitle());
		this.em.getTransaction().rollback();
	}

	@Test
	public void saveTest() {
		ArtistOps conn = new ArtistOps();
		conn.setEm(this.em);

		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record");
		this.em.getTransaction().rollback();
	}

	@Test
	public void removeTest() {
		ArtistOps conn = new ArtistOps();
		conn.setEm(this.em);

		this.em.getTransaction().begin();
		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist == null || artist.getId() <= 0) fail("Failed to save artist record");
		conn.remove(artist.getId());
		Artist artist2 = conn.getById(artist.getId());
		if(artist2 != null && artist2.getId() > 0) fail("Failed to remove record.");
		this.em.getTransaction().rollback();
	}

	@Test
	public void searchTest() {
	}
}
