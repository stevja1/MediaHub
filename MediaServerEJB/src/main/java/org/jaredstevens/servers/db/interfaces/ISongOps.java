package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ISongOps {
	public Song getById(long id);
	public List<Song> getArtistSongs(long artistId);
	public List<Song> getAlbumSongs(long userId, long albumId) throws InvalidUserException;
	public Song getSongByAlbumArtist(long artistId, long albumId, String title);
	public List<Song> getSongs(int pageSize, int pageIndex);
	public List<Song> search(String query, int pageSize, int pageIndex);
	public Song save(long id, long userId, long artistId, long albumId, long fileId, String title, int duration, int trackNum, String fingerprint) throws Exception;
	public boolean remove(long id);
}
