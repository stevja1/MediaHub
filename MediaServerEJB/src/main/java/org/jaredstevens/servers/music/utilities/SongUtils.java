package org.jaredstevens.servers.music.utilities;

import org.apache.openjpa.lib.util.Files;
import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.music.MetaData;
import org.jaredstevens.servers.music.pojos.Recording;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;

public class SongUtils {
	/**
	 * 1. Read in any meta data from the file
	 * 2. Generate chromaprint
	 * 3. Query MusicBrainz for meta data
	 * 4. If local copy has meta data, and it matches an entry with MusicBrainz, use that recording
	 * 5. If local copy has no meta data, use the first entry that matches in MusicBrainz
	 * 6. Generate hash name for file
	 * 7. Copy file to storage and record location in the db
	 */
	public static boolean importSong(String filename, EntityManager em, Configuration config) {
		boolean retVal = false;
		Recording recording = MetaData.buildRecordingRecord(filename, config);
		return retVal;
	}

	public static boolean copyToRepository(String filename, Configuration config) {
		boolean retVal = false;
		String destination;
		destination = config.getMediaRoot();
		try {
			Files.copy(new File(filename), new File(destination));
			retVal = true;
		} catch(IOException e) {}
		return retVal;
	}
}
