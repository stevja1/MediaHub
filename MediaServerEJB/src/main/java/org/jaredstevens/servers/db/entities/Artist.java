package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist implements Serializable {
	private long id;
	private String name;
	private Set<Album> albums;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "artist")
	@OrderBy("releaseDate DESC")
	public Set<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}
}
