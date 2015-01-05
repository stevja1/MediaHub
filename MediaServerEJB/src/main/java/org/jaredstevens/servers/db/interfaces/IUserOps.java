package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.User;

import javax.ejb.Remote;

@Remote
public interface IUserOps {
	public User getById(long id);
	public User getByOAuthId(String OAuthId);
	public User save(long id, String firstName, String lastName, String OAuthId) throws Exception;
	public boolean remove(long id);
}
