package org.jaredstevens.servers.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "file")
public class File implements Serializable {
	private static final long serialVersionUID = -2643583108587251245L;
	public enum FileType {
		TV_SHOW,
		SONG,
		FILM,
		IMAGE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Basic
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private FileType type;

	@Basic
	@Column(nullable=false)
	private String filename;

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public FileType getType() {
		return this.type;
	}
	public void setType(FileType type) {
		this.type = type;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
