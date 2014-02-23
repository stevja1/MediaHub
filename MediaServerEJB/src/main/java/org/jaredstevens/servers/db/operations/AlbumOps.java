package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.interfaces.IAlbumOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
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
			retVal = (Album)this.getEm().find(Album.class, id);
		}
		return retVal;
	}

	public List<Album> getArtistAlbums(long artistId) {
		List<Album> retVal = null;
		Artist artist = this.getEm().find(Artist.class, artistId);
		Query sql = this.getEm().createQuery("SELECT a FROM Album a WHERE a.artist=:artist");
		sql.setParameter("artist", artist);
		retVal = (List<Album>)sql.getResultList();
		return retVal;
	}

	public List<Album> getAlbums(int pageCount, int pageIndex) {
		List<Album> retVal = null;
		int firstResultIndex = pageCount * pageIndex;
		Query sql = this.getEm().createQuery("SELECT a FROM Album a");
		sql.setFirstResult(firstResultIndex);
		sql.setMaxResults(pageCount);
		retVal = (List<Album>)sql.getResultList();
		return retVal;
	}

	public List<Album> search(String query) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Album save(long id, long artistId, String title, Date releaseDate, int trackCount, int discNum) throws Exception {
		Album retVal = null;
		Album albumEntry = new Album();
		Artist artist = null;
		if(id > 0) {
			albumEntry = this.getEm().find(Album.class, id);
			if(albumEntry == null) throw new Exception("Couldn't find album id: "+id);
		}
		if(artistId > 0) artist = (Artist)this.getEm().find(Artist.class, artistId);
		albumEntry.setArtist(artist);
		albumEntry.setTitle(title);
		albumEntry.setReleaseDate(releaseDate);
		albumEntry.setTrackCount(trackCount);
		albumEntry.setDiscNum(discNum);
		this.getEm().persist(albumEntry);
		if(albumEntry.getId() == 0) this.getEm().flush();
		if(albumEntry != null) retVal = albumEntry;
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			Album albumEntry = null;
			albumEntry = (Album)this.getEm().find(Album.class, id);
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
