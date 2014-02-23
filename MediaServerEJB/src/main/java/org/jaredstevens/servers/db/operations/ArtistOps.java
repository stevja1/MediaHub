package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.interfaces.IArtistOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.ArrayList;
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

	public List<Artist> getArtists(int pageCount, int pageIndex) {
		List<Artist> retVal;
		int firstResultIndex = pageCount * pageIndex;
		Query sql = this.getEm().createQuery("SELECT a FROM Artist a");
		sql.setFirstResult(firstResultIndex);
		sql.setMaxResults(pageCount);
		retVal = (List<Artist>)sql.getResultList();
		return retVal;
	}

	public Artist getArtistByName(String artistName) {
		Artist artist = null;
		Query sql = this.getEm().createQuery("SELECT a FROM Artist a WHERE a.name=:artistName");
		sql.setMaxResults(1);
		sql.setParameter("artistName", artistName);
		List<Artist> result = sql.getResultList();
		if(result != null && result.size() > 0) artist = result.get(0);
		return artist;
	}

	public List<Artist> search(String query) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Artist save(long id, String name) throws Exception {
		Artist retVal = null;
		Artist artistEntry = new Artist();
		if(id > 0) {
			artistEntry = this.getEm().find(Artist.class, id);
			if(artistEntry == null) throw new Exception("Unable to find artist id: "+id);
		}
		artistEntry.setName(name);
		this.getEm().persist(artistEntry);
		if(artistEntry.getId() == 0) this.getEm().flush();
		if(artistEntry != null) retVal = artistEntry;
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			Artist artistEntry;
			artistEntry = (Artist)this.getEm().find(Artist.class, id);
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
