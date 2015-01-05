package org.jaredstevens.apps.mediaserver.controllers;

import org.jaredstevens.apps.mediaserver.models.Chromaprint;
import org.jaredstevens.apps.mediaserver.models.Recording;
import org.jaredstevens.configuration.Configuration;
import org.jaredstevens.servers.db.entities.*;
import org.jaredstevens.servers.db.interfaces.*;
import org.jaredstevens.servers.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.ejb.EJB;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class FileUploadController {
	@EJB(mappedName="java:module/FileOps")
	private IFileOps fileService;

	@EJB(mappedName="java:module/ArtistOps")
	private IArtistOps artistService;

	@EJB(mappedName="java:module/AlbumOps")
	private IAlbumOps albumService;

	@EJB(mappedName="java:module/SongOps")
	private ISongOps songService;

	@EJB(mappedName="java:module/UserOps")
	private IUserOps userService;

	private static Configuration config = Configuration.getInstance();
	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	/**
	 * Generates a unique filename based on a source string and the current time.
	 * Used to convert byte[] to String:
	 * http://stackoverflow.com/a/304275
	 * @param fingerprint The original file name
	 * @return A String containing a new name
	 */
	public String getMD5Hash(String fingerprint) {
		StringBuffer retVal = new StringBuffer();
		byte[] checksum;
		String hashingAlgorithm = "MD5";
		try {
			checksum = MessageDigest.getInstance(hashingAlgorithm).digest(fingerprint.getBytes());
			// Quickly convert the byte array to something sane looking
			if(checksum != null && checksum.length > 0){
				for(byte b : checksum) {
					retVal.append(Integer.toString((b & 0xff ) + 0x100, 16));
				}
			}
		} catch(NoSuchAlgorithmException e) {
			FileUploadController.logger.error("Hashing algorithm ({}) doesn't exist: {}", hashingAlgorithm, e.getMessage());
		}
		return retVal.toString();
	}

	/**
	 * Upload multiple files. Saves each file to disk and updates the database.
	 * @param files The file data
	 * @return Returns a String indicating success or failure.
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody
	String uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		if(userId == null) {
			return "User not authenticated.";
		}
		User user = this.userService.getById(userId);
		StringBuffer message = new StringBuffer();
		for (MultipartFile file : files) {
			String name = file.getOriginalFilename();
			try {
				byte[] fileData = file.getBytes();

				String type = RecordingProcessor.detectFileType(fileData);
				Chromaprint cp;
				if(RecordingProcessor.isSupportedAudioType(type)) {
					String tempFilename = this.getMD5Hash(name + System.currentTimeMillis());
					// Write a temp file so that we can get the fingerprint
					Utilities.writeFile(fileData, config.getMediaPath(), tempFilename);
					Recording recording;
					if(type.equals("audio/mpeg")) {
						recording = RecordingProcessor.getRecordingInfo(FileUploadController.config.getMediaPath(), tempFilename);
					} else {
						recording = null;
//						recording = RecordingProcessor.getRecordingInfo(cp);
					}
					cp = RecordingProcessor.getChromaprint(tempFilename);
					if(recording != null){
						recording.setFingerprint(cp.getFingerprint());
						// We always want to use the chromaprint duration because if we ever need to lookup meta data in an
						// API, its this duration that it will expect.
						if(recording.getDuration() != Integer.parseInt(cp.getDuration()))
							recording.setDuration(Integer.parseInt(cp.getDuration()));

						// Generate a useful message
						message.append("<strong>You successfully uploaded file=").append(name).append("</strong><br />")
								.append("Artist: ").append(recording.getArtistName()).append("<br />")
								.append("Album: ").append(recording.getAlbumName()).append("<br />")
								.append("Song Title: ").append(recording.getSongTitle()).append("<br />")
								.append("FINGERPRINT: ").append(cp.getFingerprint().substring(0, 40)).append("...<br /><br />");
					}

					// Delete the temporary file
					Utilities.deleteFile(config.getMediaPath(), tempFilename);

					// Write a permanent file
					String uniqueFilename = this.getMD5Hash(cp.getFingerprint());
					Utilities.writeFile(fileData, config.getMediaPath(), uniqueFilename);
					List<File> fileRecords = this.fileService.getFilesByFilename(uniqueFilename);
					File fileRecord = null;
					// If file already exists, don't save a new one!
					if(fileRecords == null || fileRecords.size() <= 0) {
						fileRecord = this.fileService.save(-1, uniqueFilename, org.jaredstevens.servers.db.entities.File.FileType.SONG);
					} else {
						fileRecord = fileRecords.get(0);
					}
					this.saveRecordingData(recording, fileRecord, user);
				} else {
					message.append("Didn't recognize file type ").append(type).append(" for file ").append(name).append("<br />");
				}
			} catch (Exception e) {
				FileUploadController.logger.error("There was a problem processing the file {}. {}", name, e);
				logger.error("Exception thrown: {}", e);
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		return message.toString();
	}

	public void saveRecordingData(Recording recording, File file, User user) {
		// Try to find the artist. If no record is found, create a new one.
		Artist artist = this.artistService.getArtistByName(recording.getArtistName());
		if(artist == null) {
			try {
				artist = this.artistService.save(-1, recording.getArtistName());
			} catch(Exception e) {
				// We should never get here since -1 shouldn't match anything in the database.
			}
		}

		// Try to find the album. If no record is found, create a new one.
		Album album = this.albumService.getArtistAlbumByAlbumName(artist.getId(), recording.getAlbumName());
		FileUploadController.logger.info("album record: {}", album);
		if(album == null) {
			try {
				album = this.albumService.save(
						-1,
						recording.getAlbumName(),
						recording.getAlbumReleaseDate(),
						recording.getAlbumTrackCount(),
						recording.getAlbumDiscNum()
				);
				FileUploadController.logger.info("NEW album record: {}", album);
			} catch(Exception e) {
				// We should never get here since -1 shouldn't match anything in the database.
			}
		}

		// Try to find the song. If no record is found, create a new one.
		Song song = this.songService.getSongByAlbumArtist(artist.getId(), album.getId(), recording.getSongTitle());
		if(song == null) {
			try {
				song = this.songService.save(
						-1,
						user.getId(),
						artist.getId(),
						album.getId(),
						file.getId(),
						recording.getSongTitle(),
						recording.getDuration(),
						recording.getTrackNum(),
						recording.getFingerprint()
				);
			} catch(Exception e) {
				// We should never get here since -1 shouldn't match anything in the database.
			}
		}
	}

	/**
	 * Detect the type of multiple files.
	 * @param files The file data
	 * @return Returns a String indicating success or failure.
	 */
	@RequestMapping(value = "/detectType", method = RequestMethod.POST)
	public @ResponseBody
	String detectType(@RequestParam("file") MultipartFile[] files, HttpSession session) {

		String message = "";
		String type;
		for (MultipartFile file : files) {
			String name = file.getOriginalFilename();
			try {
				byte[] fileData = file.getBytes();
				type = RecordingProcessor.detectFileType(fileData);
				message += "Detected file type: "+type+" for file named "+name+"<br />";
			} catch (Exception e) {
				FileUploadController.logger.error("There was a problem processing the file {}.", name, e);
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		return message;
	}
}
