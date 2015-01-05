package org.jaredstevens.servers.db.interfaces;

import javax.ejb.Remote;
import java.util.List;
import org.jaredstevens.servers.db.entities.File;

@Remote
public interface IFileOps {
	public File getById(long id);
	public List<File> getFilesByFilename(String filename);
	public List<File> getFilesByFileType(File.FileType type, int pageSize, int pageIndex);
	public File save(long id, String filename, File.FileType type);
	public boolean remove(long id);
	public Number getRowCount();
}
