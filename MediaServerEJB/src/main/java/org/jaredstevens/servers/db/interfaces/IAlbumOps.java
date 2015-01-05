package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;

import javax.ejb.Remote;
import java.sql.Date;
import java.util.List;

@Remote
public interface IAlbumOps {
	public Album getById(long id);
	public List<Album> getArtistAlbums(long userId, long artistId) throws InvalidUserException;
	public Album getArtistAlbumByAlbumName(long artistId, String albumName);
	public List<Album> getAlbums(int pageSize, int pageIndex);
	public List<Album> search(String query, int pageSize, int pageIndex);
	public Album save(long id, String title, Date releaseDate, int trackCount, int discNum) throws Exception;
	public boolean remove(long id);
}
