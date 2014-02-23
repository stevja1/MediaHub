package org.jaredstevens.servers.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "file")
public class File implements Serializable {
	private static final long serialVersionUID = -2643583108587251245L;
	public enum FileType {
		SHOW,
		SONG,
		FILM,
		IMAGE
	}

	private long id;
	private String filename;
	private FileType type;

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
	/**
	 * @return the file type
	 */
	@Basic
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	public FileType getType() {
		return this.type;
	}

	/**
	 * @param type The file type
	 */
	public void setType(FileType type) {
		this.type = type;
	}

	@Basic
	@Column(nullable=false)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
