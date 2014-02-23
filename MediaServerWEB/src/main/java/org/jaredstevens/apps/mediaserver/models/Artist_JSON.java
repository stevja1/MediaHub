package org.jaredstevens.apps.mediaserver.models;

import org.jaredstevens.servers.db.entities.Artist;

import java.util.ArrayList;
import java.util.List;

public class Artist_JSON {
	private long id;
	private String name;

	public static Artist_JSON artistFactory(Artist artist) {
		Artist_JSON retVal = new Artist_JSON();
		retVal.setId(artist.getId());
		retVal.setName(artist.getName());
		return retVal;
	}

	public static List<Artist_JSON> artistFactory(List<Artist> artists) {
		ArrayList<Artist_JSON> retVal = null;
		for(Artist artist : artists) {
			if(retVal == null) retVal = new ArrayList<Artist_JSON>();
			retVal.add(Artist_JSON.artistFactory(artist));
		}
		return retVal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
