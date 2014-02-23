package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Album;

import javax.ejb.Remote;
import java.sql.Date;
import java.util.List;

@Remote
public interface IAlbumOps {
	public Album getById(long id);
	public List<Album> getArtistAlbums(long artistId);
	public List<Album> getAlbums(int pageCount, int pageIndex);
	public List<Album> search(String query);
	public Album save(long id, long artistId, String title, Date releaseDate, int trackCount, int discNum) throws Exception;
	public boolean remove(long id);
}
