package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.*;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;
import org.jaredstevens.servers.db.interfaces.ISongOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.HashSet;
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
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s WHERE s.artist=:artist ORDER BY s.album,s.trackNum");
		sql.setParameter("artist",artist);
		return (List<Song>)sql.getResultList();
	}

	public List<Song> getAlbumSongs(long userId, long albumId) throws InvalidUserException {
		User user = null;
		if(userId > 0) user = this.getEm().find(User.class, userId);
		if(user == null) throw new InvalidUserException();
		Album album = this.getEm().find(Album.class, albumId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s JOIN s.users u WHERE s.album=:album AND u.id=:userId");
		sql.setParameter("album",album);
		sql.setParameter("userId", userId);
		return (List<Song>)sql.getResultList();
	}

	public Song getSongByAlbumArtist(long artistId, long albumId, String title) {
		Song retVal;
		Album album = this.getEm().find(Album.class, albumId);
		Artist artist = this.getEm().find(Artist.class, artistId);
		Query sql = this.getEm().createQuery("SELECT DISTINCT s FROM Song s WHERE s.artist=:artist AND s.album=:album AND s.title=:title");
		sql.setParameter("artist", artist);
		sql.setParameter("album", album);
		sql.setParameter("title", title);
		try {
			retVal = (Song) sql.getSingleResult();
		} catch(NoResultException e) {
			// No results came back, therefore return null
			retVal = null;
		}
		return retVal;
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

	public List<Song> search(String query, int pageSize, int pageIndex) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Song save(long id, long userId, long artistId, long albumId, long fileId, String title, int duration, int trackNum, String fingerprint) throws Exception {
		Song retVal = null;
		Song songEntry = new Song();
		File file = null;
		Artist artist = null;
		Album album = null;
		User user = null;
		if(id > 0) {
			songEntry = this.getEm().find(Song.class, id);
			if(songEntry == null) throw new Exception("Couldn't find song id: "+id);
		}
		if(fileId > 0) file = this.getEm().find(File.class, fileId);
		if(albumId > 0) album = this.getEm().find(Album.class, albumId);
		if(artistId > 0) artist = this.getEm().find(Artist.class, artistId);
		if(userId > 0) user = this.getEm().find(User.class, userId);
		if(user == null) throw new InvalidUserException();
		songEntry.setTitle(title);
		songEntry.setDuration(duration);
		songEntry.setTrackNum(trackNum);
		songEntry.setFingerprint(fingerprint);
		songEntry.setFile(file);
		songEntry.setAlbum(album);
		songEntry.setArtist(artist);
		HashSet<User> users = new HashSet<User>();
		users.add(user);
		songEntry.setUsers(users);
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
