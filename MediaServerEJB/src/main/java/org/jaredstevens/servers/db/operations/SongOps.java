package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.File;
import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.interfaces.ISongOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateless(name="SongOps",mappedName="SongOps")
@Remote
public class SongOps implements ISongOps {
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Song getById(long id) {
		Song retVal = null;
		if(this.getEm() != null) {
			retVal = this.getEm().find(Song.class, id);
		}
		return retVal;
	}

	public List<Song> getArtistSongs(long artistId) {
		Artist artist = this.getEm().find(Artist.class, artistId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s JOIN s.album al WHERE al.artist=:artist ORDER BY al.id,s.trackNum");
		sql.setParameter("artist",artist);
		return (List<Song>)sql.getResultList();
	}

	public List<Song> getAlbumSongs(long albumId) {
		Album album = this.getEm().find(Album.class, albumId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s WHERE s.album=:album");
		sql.setParameter("album",album);
		return (List<Song>)sql.getResultList();
	}

	public List<Song> getSongs(int pageCount, int pageIndex) {
		List<Song> retVal = null;
		int firstResultIndex = pageCount * pageIndex;
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s");
		sql.setFirstResult(firstResultIndex);
		sql.setMaxResults(pageCount);
		retVal = (List<Song>)sql.getResultList();
		return retVal;
	}

	public List<Song> search(String query) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Song save(long id, long fileId, long albumId, String title, int duration, int trackNum, String fingerprint) throws Exception {
		Song retVal = null;
		Song songEntry = new Song();
		File file = null;
		Album album = null;
		if(id > 0) {
			songEntry = this.getEm().find(Song.class, id);
			if(songEntry == null) throw new Exception("Couldn't find song id: "+id);
		}
		if(fileId > 0) file = (File)this.getEm().find(File.class, fileId);
		if(albumId > 0) album = (Album)this.getEm().find(Album.class, albumId);
		songEntry.setTitle(title);
		songEntry.setDuration(duration);
		songEntry.setTrackNum(trackNum);
		songEntry.setFingerprint(fingerprint);
		songEntry.setFile(file);
		songEntry.setAlbum(album);
		this.getEm().persist(songEntry);
		if(songEntry.getId() == 0) this.getEm().flush();
		if(songEntry != null) retVal = songEntry;
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			Song songEntry = null;
			songEntry = (Song)this.getEm().find(Song.class, id);
			if(songEntry != null) this.getEm().remove(songEntry);
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
