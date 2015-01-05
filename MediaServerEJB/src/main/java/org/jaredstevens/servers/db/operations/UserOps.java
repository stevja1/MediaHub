package org.jaredstevens.servers.db.operations;

import org.jaredstevens.servers.db.entities.User;
import org.jaredstevens.servers.db.interfaces.IUserOps;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless(name="UserOps",mappedName="UserOps")
@Remote
public class UserOps implements IUserOps {
	@PersistenceContext(unitName="MediaDB-PU",type= PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public User getById(long id) {
		User retVal = null;
		if(this.getEm() != null) {
			retVal = this.getEm().find(User.class, id);
		}
		return retVal;
	}

	public User getByOAuthId(String OAuthId) {
		User retVal;
		Query sql = this.getEm().createQuery("SELECT u FROM User u WHERE u.OAuthId=:OAuthId");
		sql.setParameter("OAuthId", OAuthId);
		try {
			retVal = (User) sql.getSingleResult();
		} catch(NoResultException e) {
			retVal = null;
		}
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User save(long id, String firstName, String lastName, String OAuthId) throws Exception {
		User retVal;
		if(id > 0) {
			retVal = this.getEm().find(User.class, id);
			if(retVal == null) throw new Exception("Unable to find user id: "+id);
		} else retVal = new User();
		retVal.setFirstName(firstName);
		retVal.setLastName(lastName);
		retVal.setOAuthId(OAuthId);
		this.getEm().persist(retVal);
		if(retVal.getId() == 0) this.getEm().flush();
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(long id) {
		if(id > 0) {
			User userEntry;
			userEntry = this.getEm().find(User.class, id);
			if(userEntry != null) this.getEm().remove(userEntry);
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
