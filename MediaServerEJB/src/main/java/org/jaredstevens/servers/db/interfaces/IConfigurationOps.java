package org.jaredstevens.servers.db.interfaces;

import org.jaredstevens.servers.db.entities.Configuration;

import javax.ejb.Remote;

@Remote
public interface IConfigurationOps {
	public Configuration get();
	public Configuration save(String songPath, String scriptsRoot);
}
