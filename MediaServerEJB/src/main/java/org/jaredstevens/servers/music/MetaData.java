package org.jaredstevens.servers.music;

import com.mpatric.mp3agic.*;
import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.music.pojos.Recording;

import java.io.*;

public class MetaData {
	public static String getArtist(ID3v1 tag) {
		String artist;
		if(tag.getClass().getSimpleName() == "ID3v23Tag") {
			// Process v2 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
			else if(((ID3v2)tag).getAlbumArtist() != null) artist = ((ID3v2)tag).getAlbumArtist();
			else if(((ID3v2)tag).getOriginalArtist() != null) artist = ((ID3v2)tag).getOriginalArtist();
			else artist = "Unknown Artist";
		} else {
			// Process v1 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
			else artist = "Unknown Artist";
		}
		return artist;
	}

	public static int getTrack(String track) {
		int retVal = -1;
		if(track.contains("/")) {
			retVal = Integer.valueOf(track.split("/")[0]);
		} else {
			// Check for number format exception
			try {
				retVal = Integer.valueOf(track);
			} catch(NumberFormatException e) {
				System.err.println("Couldn't parse the track.");
			}
		}
		return retVal;
	}

	public static int getAlbumTrackCount(String track) {
		int retVal = -1;
		if(track.contains("/")) {
			retVal = Integer.valueOf(track.split("/")[1]);
		}
		return retVal;
	}

	public static ID3v1 getTag(String filename) {
		ID3v1 retVal = null;
		Mp3File fileData = null;
		try {
			fileData = new Mp3File(filename);
		} catch(InvalidDataException e) {
			System.err.println("InvalidDataException: "+e.getMessage());
		} catch(UnsupportedTagException e) {
			System.err.println("UnsupportedTagException: "+e.getMessage());
		} catch(FileNotFoundException e) {
			System.err.println("Skipping missing file: "+filename+"\n"+e.getMessage());
		} catch(IOException e) {
			System.err.println("IOException: "+e.getMessage());
		}
		if(fileData != null) {
			if(fileData.hasId3v2Tag()) retVal = fileData.getId3v2Tag();
			else if(fileData.hasId3v1Tag()) retVal = fileData.getId3v1Tag();
		}
		return retVal;
	}

	public static byte[] getChromaprint(String filename, Configuration config) {
		byte[] retVal = null;
		String scriptsRoot;
		if(config != null) scriptsRoot = config.getScriptsRoot();
		else scriptsRoot = null;
		String command = "fpcalc "+filename;
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.directory(new File(scriptsRoot));
		String cwd = pb.directory().getAbsolutePath();
		if(cwd == null) cwd = System.getProperty("user.dir");
		System.out.println("CWD:"+cwd);
		BufferedReader outputBuffer;
		pb.redirectErrorStream(true);
		try {
			// Line 1: FILE=<FILENAME>
			// Line 2: DURATION=<DURATION IN SECONDS>
			// Line 3: FINGERPRINT=<GIANT STRING THING>
			Process p = pb.start();
			outputBuffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			// Throw out the filename - we already know what that is
			line = outputBuffer.readLine();

			// Get the duration
			line = outputBuffer.readLine();
			String[] durationParts = line.split("=");
			int duration = Integer.valueOf(durationParts[1]);

			// Get the fingerprint
			line = outputBuffer.readLine();
			String[] fingerprintParts = line.split("=");
			String fingerprint = fingerprintParts[1];
			retVal = fingerprint.getBytes();

			outputBuffer.close();
		} catch(IOException e) {
			System.err.println("Error: "+e.getMessage());
		}
		return retVal;
	}

	public static Recording buildRecordingRecord(String filename, Configuration config) {
		Recording retVal = new Recording();
		ID3v1 tag = MetaData.getTag(filename);
		retVal.setArtistName(MetaData.getArtist(tag));
		retVal.setAlbumName(tag.getAlbum());
		retVal.setTrackCount(MetaData.getTrack(tag.getTrack()));
		retVal.setAlbumTrackCount(MetaData.getAlbumTrackCount(tag.getTrack()));
		retVal.setFingerprint(MetaData.getChromaprint(filename, config));
		return retVal;
	}
}
