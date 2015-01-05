package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.exceptions.InvalidUserException;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface IArtistOps {
	public Artist getById(long id);
	public List<Artist> getArtists(long userId, int pageSize, int pageIndex) throws InvalidUserException;
	public Artist getArtistByName(String name);
	public List<Artist> search(String query, int pageSize, int pageIndex);
	public Artist save(long id, String name) throws Exception;
	public boolean remove(long id);
}
