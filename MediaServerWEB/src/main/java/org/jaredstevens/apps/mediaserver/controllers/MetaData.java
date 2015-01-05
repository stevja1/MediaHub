package org.jaredstevens.apps.mediaserver.controllers;

import com.mpatric.mp3agic.*;
import org.jaredstevens.apps.mediaserver.models.Recording;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Date;
import java.util.Calendar;

public class MetaData {
	public static Logger logger = LoggerFactory.getLogger(MetaData.class);
	/**
	 * Gets the artist data from the ID3 tag.
	 * @param tag The tag to process
	 * @return Returns the artist on success, null on failure.
	 */
	public static String getArtist(ID3v1 tag) {
		String artist = null;
		if(tag.getClass().getSimpleName().contains("ID3v2")) {
			// Process v2 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
			else if(((ID3v2)tag).getAlbumArtist() != null) artist = ((ID3v2)tag).getAlbumArtist();
			else if(((ID3v2)tag).getOriginalArtist() != null) artist = ((ID3v2)tag).getOriginalArtist();
		} else {
			// Process v1 version of the tag
			if(tag.getArtist() != null) artist = tag.getArtist();
		}
		return artist;
	}

	/**
	 * Extracts the recording track number from the raw ID3 tag track property.
	 * @param track The track data to process
	 * @return Returns the track on success, -1 on failure.
	 */
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

	/**
	 * Extracts the total number of tracks from the track information stored in the tag.
	 * @param track The raw track data stored in the tag. We're expecting something formatted like this: '1/8'
	 * @return Returns -1 on failure, the total number of tracks on success.
	 */
	public static int getAlbumTrackCount(String track) {
		int retVal = -1;
		if(track.contains("/")) {
			retVal = Integer.valueOf(track.split("/")[1]);
		}
		return retVal;
	}

	/**
	 * Parses out the disc number from the tag. If a disc is part of a multi-disc set, this is useful.
	 * This method expects data in the format "1/3" where "1" is the disc and "3" is the number of total discs.
	 * @param tag The tag to process
	 * @return An integer containing the disc number. -1 is returned on failure.
	 */
	public static int getDiscNum(ID3v1 tag) {
		int retVal = -1;
		if(tag.getClass().getSimpleName().contains("ID3v2")) {
			String setData = ((ID3v2)tag).getPartOfSet();
			if(setData.contains("/")) {
				String[] setDataParts = setData.split("/");
				if(setDataParts.length == 2) {
					retVal = Integer.parseInt(setDataParts[0]);
				}
			}
		}
		return retVal;
	}

	/**
	 * Extracts the tag from a recording. Returns either an ID3v1 or ID3v2 tag on success, null on failure.
	 * @param fileData The fileData for the MP3 file. This can be retrieved using MetaData.getFileData()
	 * @return Returns the tag on success, null on failure.
	 */
	public static ID3v1 getTag(Mp3File fileData) {
		ID3v1 retVal = null;

		if(fileData != null) {
			if(fileData.hasId3v2Tag()) retVal = fileData.getId3v2Tag();
			else if(fileData.hasId3v1Tag()) retVal = fileData.getId3v1Tag();

			if(retVal != null) {
				MetaData.logger.debug("Tag type is {}", retVal.getClass().getSimpleName());
				if (retVal.getClass().getSimpleName().contains("ID3v2")) MetaData.logger.debug("Tag is ID3v2 tag.");
				else if (retVal.getClass().getSimpleName().contains("ID3v1"))
					MetaData.logger.debug("Tag is ID3v1 tag.");
				else
					MetaData.logger.warn("Couldn't find a tag match ({}) for file {}", retVal.getClass().getSimpleName(), fileData.getFilename());
			}
		}

		return retVal;
	}

	/**
	 * Gets the MP3 file data for parsing tags.
	 * @param filename The filename of the file to process.
	 * @return An Mp3File object on success, null on failure.
	 */
	public static Mp3File getFileData(String filename) {
		Mp3File fileData = null;
		try {
			fileData = new Mp3File(filename);
		} catch(InvalidDataException e) {
			MetaData.logger.warn("InvalidDataException: ", e);
		} catch(UnsupportedTagException e) {
			MetaData.logger.warn("UnsupportedTagException: ", e);
		} catch(FileNotFoundException e) {
			MetaData.logger.warn("Skipping missing file: {}", filename);
			MetaData.logger.warn("Exception: ", e);
		} catch(IOException e) {
			MetaData.logger.warn("IOException: ", e);
		}
		return fileData;
	}

	/**
	 * Attempts to extract the album release date from the ID3 tag data. If we find an ID3v2 tag, we'll return a
	 * java.sql.Date object containing the release date. If we find an ID3v1 tag, we'll return a java.sql.Date object
	 * with the date set to the year we found but with January 1 set as the rest of the date.
	 * @param tag The tag to process
	 * @return A date object on success. On some kind of failure, return null.
	 */
	public static Date getReleaseDate(ID3v1 tag) {
		Date releaseDate = null;
		if(tag.getClass().getSimpleName().contains("ID3v1")) {
			String tagYear = tag.getYear();
			Calendar cal = Calendar.getInstance();
			// Since we only get the release year with ID3v2 tags, we'll initialize the date to Jan 1.
			cal.set(Integer.valueOf(tagYear), Calendar.JANUARY, 1);
			releaseDate = new Date(cal.getTime().getTime());
		} else if(tag.getClass().getSimpleName().contains("ID3v2")) {
			String tagDate = ((ID3v2) tag).getDate();
			if(tagDate == null) {
				// Use this until we can get the recording date from an ID3v2 tag
				String tagYear = tag.getYear();
				if(tagYear != null) {
					Calendar cal = Calendar.getInstance();
					// Since we only get the release year with ID3v2 tags, we'll initialize the date to Jan 1.
					cal.set(Integer.valueOf(tagYear), Calendar.JANUARY, 1);
					releaseDate = new Date(cal.getTime().getTime());
				}
			}
		}
		if(releaseDate == null) MetaData.logger.warn("Couldn't parse release date information for this tag.", tag.toString());
		return releaseDate;
	}

	/**
	 * Parses out the recording data using ID3 tags and returns a recording object.
	 * @param filename The filename of the recording to process.
	 * @return A recording object on success, null on failure.
	 */
	public static Recording buildRecordingRecord(String filename) {
		Recording retVal = null;
		Mp3File fileData = MetaData.getFileData(filename);
		ID3v1 tag = MetaData.getTag(fileData);
		if(tag != null) {
			retVal = new Recording();
			retVal.setArtistName(MetaData.getArtist(tag));
			retVal.setAlbumName(tag.getAlbum());
			retVal.setAlbumReleaseDate(getReleaseDate(tag));
			retVal.setAlbumTrackCount(MetaData.getAlbumTrackCount(tag.getTrack()));
			retVal.setAlbumDiscNum(MetaData.getDiscNum(tag));
			retVal.setSongTitle(tag.getTitle());
			retVal.setTrackNum(MetaData.getTrack(tag.getTrack()));
			retVal.setDuration((int) fileData.getLengthInSeconds());
		}
		return retVal;
	}
}
