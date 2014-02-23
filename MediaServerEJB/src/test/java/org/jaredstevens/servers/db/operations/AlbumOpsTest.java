package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.sql.Date;

import static org.junit.Assert.*;

public class AlbumOpsTest {
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
		AlbumOps conn = new AlbumOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();

		Artist artist = OpsUtils.saveArtistRecord(this.em);
		if(artist.getId() <= 0) fail("Failed to save a test artist record.");

		Album album = OpsUtils.saveAlbumRecord(artist, this.em);
		if(album.getId() <= 0) fail("Failed to save a test album record.");

		Album album2 = conn.getById(album.getId());
		if(album2.getId() <=0) fail("Failed to fetch the new album record.");
		this.em.getTransaction().rollback();
	}

	public void saveTest() {

	}

	public void removeTest() {

	}

	public void searchTest() {

	}
}
