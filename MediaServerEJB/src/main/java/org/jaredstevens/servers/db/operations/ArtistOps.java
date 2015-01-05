package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.User;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;
import org.jaredstevens.servers.db.interfaces.IArtistOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateless(name="ArtistOps",mappedName="ArtistOps")
@Remote
public class ArtistOps implements IArtistOps {
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Artist getById(long id) {
		Artist retVal = null;
		if(this.getEm() != null) {
			retVal = this.getEm().find(Artist.class, id);
		}
		return retVal;
	}

	/**
	 * Gets a paginated list of artist objects from the database
	 * @param pageSize The number of results per page to return
	 * @param pageIndex Which page to return
	 * @return A list of artist objects
	 */
	public List<Artist> getArtists(long userId, int pageSize, int pageIndex) throws InvalidUserException {
		User user = this.getEm().find(User.class, userId);
		if(user == null) throw new InvalidUserException();

		int firstResultIndex = pageSize * pageIndex;
		Query sql = this.getEm().createQuery("SELECT DISTINCT a FROM Artist a JOIN a.songs s JOIN s.users u WHERE u.id=:userId");
		sql.setParameter("userId", userId);
		sql.setFirstResult(firstResultIndex);
		sql.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Artist> retVal = (List<Artist>)sql.getResultList();
		return retVal;
	}

	/**
	 * Returns an artist object by name. If more than one record matches, only the first is returned.
	 * @param artistName The name of the artist to search for
	 * @return An artist record
	 */
	public Artist getArtistByName(String artistName) {
		Artist artist = null;
		Query sql = this.getEm().createQuery("SELECT a FROM Artist a WHERE a.name=:artistName");
		sql.setMaxResults(1);
		sql.setParameter("artistName", artistName);
		@SuppressWarnings("unchecked")
		List<Artist> result = sql.getResultList();
		if(result != null && result.size() > 0) artist = result.get(0);
		return artist;
	}

	/**
	 * Search for an artist using a query
	 * @param query The query
	 * @param pageSize The number of results per page
	 * @param pageIndex The page index to get
	 * @return A list of artist objects
	 */
	public List<Artist> search(String query, int pageSize, int pageIndex) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Saves an artist record to the database
	 * @param id The id of the artist for record updates
	 * @param name The name of the artist
	 * @return The new artist record if one is created, the updated record on update.
	 * @throws Exception Thrown if no matching record could be found when an id is provided.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Artist save(long id, String name) throws Exception {
		Artist retVal;
		Artist artistEntry = new Artist();
		if(id > 0) {
			artistEntry = this.getEm().find(Artist.class, id);
			if(artistEntry == null) throw new Exception("Unable to find artist id: "+id);
		}
		artistEntry.setName(name);
		this.getEm().persist(artistEntry);
		if(artistEntry.getId() == 0) this.getEm().flush();
		retVal = artistEntry;
		return retVal;
	}

	/**
	 * Removes an artist record
	 * @param id The id of the record to remove
	 * @return Returns true on success, false on failure.
	 * @todo add some error handling and throw some exceptions of we can't find a matching record.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			Artist artistEntry;
			artistEntry = this.getEm().find(Artist.class, id);
			if(artistEntry != null) this.getEm().remove(artistEntry);
		}
		return true;
	}

	public EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
