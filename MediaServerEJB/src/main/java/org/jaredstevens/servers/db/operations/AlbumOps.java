package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.User;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;
import org.jaredstevens.servers.db.interfaces.IAlbumOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Stateless(name="AlbumOps",mappedName="AlbumOps")
@Remote
public class AlbumOps implements IAlbumOps {
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Album getById(long id) {
		Album retVal = null;
		if(this.getEm() != null) {
			retVal = this.getEm().find(Album.class, id);
		}
		return retVal;
	}

	/**
	 * Gets all of the artists albums
	 * @param artistId The artist id
	 * @return A list of album objects
	 */
	public List<Album> getArtistAlbums(long userId, long artistId) throws InvalidUserException {
		User user = this.getEm().find(User.class, userId);
		if(user == null) throw new InvalidUserException();

		Artist artist = this.getEm().find(Artist.class, artistId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT al FROM Album al JOIN al.songs s JOIN s.users u WHERE s.artist=:artist AND u.id=:userId");
		sql.setParameter("artist", artist);
		sql.setParameter("userId", userId);
		@SuppressWarnings("unchecked")
		List<Album> retVal = sql.getResultList();
		return retVal;
	}

	/**
	 * Searches for an album name by artist.
	 * @param artistId The artistId who's albums will be searched
	 * @param albumName The name of the album you're looking for
	 * @return An Album object on success, null on failure.
	 */
	public Album getArtistAlbumByAlbumName(long artistId, String albumName) {
		Artist artist = this.getEm().find(Artist.class, artistId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT al FROM Song s JOIN s.album al WHERE s.artist=:artist AND al.title=:title");
		sql.setParameter("artist", artist);
		sql.setParameter("title", albumName);
		Album retVal;
		try {
			 retVal = (Album) sql.getSingleResult();
		} catch(NoResultException e) {
			// We didn't find a match, therefore, return null.
			retVal = null;
		}
		return retVal;
	}

	/**
	 * Gets a paginated list of all albums in the database
	 * @param pageSize The number of results per page
	 * @param pageIndex Which page to get
	 * @return A list of album objects
	 */
	public List<Album> getAlbums(int pageSize, int pageIndex) {
		int firstResultIndex = pageSize * pageIndex;
		Query sql = this.getEm().createQuery("SELECT a FROM Album a");
		sql.setFirstResult(firstResultIndex);
		sql.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Album> retVal = sql.getResultList();
		return retVal;
	}

	/**
	 * Searches the database for the album pattern. A paginated list is returned.
	 * @param query The pattern to search for
	 * @param pageSize The number of results per page
	 * @param pageIndex Which page to get
	 * @return A list of album objects
	 */
	public List<Album> search(String query, int pageSize, int pageIndex) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Saves an album record in the database
	 * @param id The album id to save
	 * @param title The title of the album
	 * @param releaseDate The release date of the album
	 * @param trackCount The number of tracks on the album
	 * @param discNum The disc number in a compilation of discs when applicable. If only a single disc, set to 1.
	 * @return The saved album. If a new album entry is created, the id property of this object will be populated.
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Album save(long id, String title, Date releaseDate, int trackCount, int discNum) throws Exception {
		Album retVal;
		Album albumEntry = new Album();
		if(id > 0) {
			albumEntry = this.getEm().find(Album.class, id);
			if(albumEntry == null) throw new Exception("Couldn't find album id: "+id);
		}
		albumEntry.setTitle(title);
		albumEntry.setReleaseDate(releaseDate);
		albumEntry.setTrackCount(trackCount);
		albumEntry.setDiscNum(discNum);
		this.getEm().persist(albumEntry);
		if(albumEntry.getId() == 0) this.getEm().flush();
		retVal = albumEntry;
		return retVal;
	}

	/**
	 * Removes an album record from the database
	 * @param id The id of the record to remove
	 * @return True on success, false on failure
	 * @todo Throw some exceptions if crap breaks here.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			Album albumEntry;
			albumEntry = this.getEm().find(Album.class, id);
			if(albumEntry != null) this.getEm().remove(albumEntry);
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
