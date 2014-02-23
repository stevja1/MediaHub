package org.jaredstevens.servers.db.operations;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jaredstevens.servers.db.entities.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class FileOpsTest {
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
	public void testGetById() {
		FileOps conn = new FileOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		// Create a test record
		File file = OpsUtils.saveFileRecord(this.em);
		if(file == null || file.getId() < 0) fail("Failed to save record.");
		// Check to see if we can query it
		File savedFile = conn.getById(file.getId());
		if(savedFile == null || savedFile.getId() != file.getId()) fail("Failed to retrieve record");
		// Rollback our changes
		this.em.getTransaction().rollback();
	}

	@Test
	public void testGetFilesByFilename() {
		FileOps conn = new FileOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		// Create a test record
		File file1 = OpsUtils.saveFileRecord(this.em);
		File file2 = OpsUtils.saveFileRecord(this.em);
		if(file1 == null || file2 == null || file1.getId() < 0 || file2.getId() < 0 || file1.getId() == file2.getId())
			fail("Failed to save records.");
		// Check to see if we can query it
		String filename = "/home/jstevens/01 - Happy Pills.mp3";
		List<File> files = conn.getFilesByFilename(filename);
		if(files == null || files.size() < 2) fail("Failed to query records");
		// Rollback our changes
		this.em.getTransaction().rollback();
	}

	@Test
	public void testSave() {
		FileOps conn = new FileOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		// Create a test record
		File file = OpsUtils.saveFileRecord(this.em);
		if(file.getId() < 0) fail("Failed to save record.");
		// Check to see if we can query it
		File savedFile = conn.getById(file.getId());
		if(savedFile.getId() != file.getId()) fail("Failed to retrieve saved record");
		// Rollback our changes
		this.em.getTransaction().rollback();
	}

	@Test
	public void testRemove() {
		FileOps conn = new FileOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		// Create a test record
		File file = OpsUtils.saveFileRecord(this.em);
		if(file.getId() < 0) fail("Failed to save record.");
		// Try to remove it
		conn.remove(file.getId());
		// Check to see if we can query it
		File savedFile = conn.getById(file.getId());
		if(savedFile != null) fail("Failed to remove saved record");
		// Rollback our changes
		this.em.getTransaction().rollback();
	}

	@Test
	public void testGetRowCount() {
		FileOps conn = new FileOps();
		conn.setEm(this.em);
		this.em.getTransaction().begin();
		// Create a test record
		File file1 = OpsUtils.saveFileRecord(this.em);
		File file2 = OpsUtils.saveFileRecord(this.em);
		if(file1.getId() < 0 || file2.getId() < 0 || file1.getId() == file2.getId()) fail("Failed to save records.");
		// Get row count
		Number rowCount = conn.getRowCount();
		if(rowCount.longValue() < 2) fail("There should be at least two rows");
		// Rollback our changes
		this.em.getTransaction().rollback();
	}
}
