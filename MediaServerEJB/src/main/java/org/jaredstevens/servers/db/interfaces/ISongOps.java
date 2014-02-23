package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Song;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ISongOps {
	public Song getById(long id);
	public List<Song> getArtistSongs(long artistId);
	public List<Song> getAlbumSongs(long albumId);
	public List<Song> getSongs(int pageCount, int pageIndex);
	public List<Song> search(String query);
	public Song save(long id, long fileId, long albumId, String title, int duration, int trackNum, String fingerprint) throws Exception;
	public boolean remove(long id);
}
