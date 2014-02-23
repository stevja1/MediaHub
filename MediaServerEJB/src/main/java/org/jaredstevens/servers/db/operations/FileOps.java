package org.jaredstevens.servers.db.operations;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.jaredstevens.servers.db.entities.File;
import org.jaredstevens.servers.db.interfaces.IFileOps;

/**
 * @author jstevens
 *
 */
@Stateless(name="FileOps",mappedName="FileOps")
@Remote
public class FileOps implements IFileOps {
	@PersistenceContext(unitName="MediaDB-PU",type=PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public File getById(long id) {
		File retVal = null;
		if(this.getEm() != null) {
			retVal = (File)this.getEm().find(File.class, id);
		}
		return retVal;
	}

	public List<File> getFilesByFilename(String filename) {
		List<File> retVal = null;
		EntityManager em = null;
		if(this.getEm() != null)
			em = this.getEm();
		if(em != null) {
			String sql = "SELECT f FROM File f WHERE f.filename=:filename";
			Query query = this.em.createQuery(sql, File.class);
			query.setParameter("filename", filename);
			List<File> results = query.getResultList();
			if(results != null) {
				retVal = results;
			}
		}
		return retVal;
	}

	/**
	 * Saves a file to the database.
	 * @param id The id of the file. Set this to -1 if this is a new file.
	 * @param filename The filename of the file.
	 * @param type The type of the file. Valid values are in File.FileType
	 * @return Returns a File object with an id on success, null on failure.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public File save(long id, String filename, File.FileType type) {
		File retVal = null;
		File fileEntry = new File();
		if(id > 0) fileEntry.setId(id);
		fileEntry.setFilename(filename);
		fileEntry.setType(type);
		this.getEm().persist(fileEntry);
		if(fileEntry.getId() == 0) this.getEm().flush();
		if(fileEntry != null) retVal = fileEntry;
		return retVal;
	}

	/**
	 * @todo Make it so this returns something useful.
	 * @param id The id of the record to delete.
	 * @return True on successful delete. False on failure.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			File fileEntry = null;
			fileEntry = (File)this.getEm().find(File.class, id);
			if(fileEntry != null) this.getEm().remove(fileEntry);
		}
		return true;
	}

	public Number getRowCount() {
		Number retVal = -1;
		Query sql = this.getEm().createQuery("SELECT count(f) FROM File f");
		retVal = (Number)sql.getSingleResult();
		return retVal;
	}

	/**
	 * @return the em
	 */
	public EntityManager getEm() {
		return em;
	}

	/**
	 * @param em the em to set
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}
}