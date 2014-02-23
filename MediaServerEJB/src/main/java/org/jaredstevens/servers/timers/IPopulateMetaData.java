package org.jaredstevens.servers.timers;

import javax.ejb.Remote;
import java.io.Serializable;

@Remote
public interface IPopulateMetaData {
	public Serializable startTimer();
	public boolean stopTimer();
	public String getTimers();
}
