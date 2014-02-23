package org.jaredstevens.apps.mediaserver.models;

import org.jaredstevens.servers.db.entities.File;

import java.util.ArrayList;
import java.util.List;

public class File_JSON {
	private long id;
	private String filename;
	private File.FileType type;

	public static File_JSON fileFactory(File file) {
		File_JSON retVal = new File_JSON();
		retVal.setId(file.getId());
		retVal.setFilename(file.getFilename());
		retVal.setType(file.getType());
		return retVal;
	}

	public static List<File_JSON> fileFactory(List<File> files) {
		ArrayList<File_JSON> retVal = null;
		for(File file : files) {
			if(retVal == null) retVal = new ArrayList<File_JSON>();
			retVal.add(File_JSON.fileFactory(file));
		}
		return retVal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File.FileType getType() {
		return type;
	}

	public void setType(File.FileType type) {
		this.type = type;
	}
}
