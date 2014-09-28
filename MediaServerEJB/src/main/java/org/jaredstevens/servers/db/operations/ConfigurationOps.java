package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.db.interfaces.IConfigurationOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless(name="ConfigurationOps",mappedName="ConfigurationOps")
@Remote
public class ConfigurationOps implements IConfigurationOps {
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Configuration get() {
		Configuration retVal = null;
		Query sql = this.getEm().createQuery("SELECT c FROM Configuration c");
		try {
			retVal = (Configuration)sql.getSingleResult();
		} catch(NoResultException e) {
			// No rows came back -- return null
		}
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Configuration save(String mediaRoot, String scriptsRoot) {
		Configuration config = new Configuration();
		config.setScriptsRoot(scriptsRoot);
		config.setMediaRoot(mediaRoot);
		this.getEm().persist(config);
		return config;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
