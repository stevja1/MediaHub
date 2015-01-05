package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String OAuthId;

	@ManyToMany(targetEntity = org.jaredstevens.servers.db.entities.Song.class, mappedBy="users")
	private Set<Song> songs;

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getOAuthId() { return OAuthId; }
	public void setOAuthId(String OAuthId) { this.OAuthId = OAuthId; }
	public Set<Song> getSongs() { return songs; }
	public void setSongs(Set<Song> songs) { this.songs = songs; }
}
