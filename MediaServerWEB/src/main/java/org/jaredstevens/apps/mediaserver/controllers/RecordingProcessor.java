package org.jaredstevens.apps.mediaserver.controllers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tika.Tika;
import org.jaredstevens.apps.mediaserver.models.Chromaprint;
import org.jaredstevens.apps.mediaserver.models.Recording;
import org.jaredstevens.configuration.Configuration;
import org.jaredstevens.servers.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class will do the following:
 * 1. Provide shell access to fpcalc for calculating chromaprint fingerprints for songs
 * 2. Call an API to download meta info for the recording based on the fingerprint
 */
public class RecordingProcessor {
	private static Logger logger = LoggerFactory.getLogger(RecordingProcessor.class);

	/**
	 * Tries to detect what kind of file we have.
	 * @param filename The filename of the file to check.
	 * @return A String containing the file type.
	 */
	public static String detectFileType(String filename) {
		String retVal;
		Tika tika = new Tika();
		retVal = tika.detect(filename);
		return retVal;
	}

	/**
	 * Tries to detect what kind of file we have.
	 * @param fileData The raw file data. This can actually just be the first few bytes and it will run a little faster.
	 * @return A String containing the file type.
	 */
	public static String detectFileType(byte[] fileData) {
		String retVal;
		Tika tika = new Tika();
		retVal = tika.detect(fileData);
		return retVal;
	}

	/**
	 * Checks this file to make sure it is a supported format. Otherwise chromaprint wont work.
	 * @param type The file type provided by Tika.
	 * @return true if the file is supported, false if not.
	 */
	public static boolean isSupportedAudioType(String type) {
		return type.equals("audio/mpeg") || type.equals("audio/x-flac") || type.equals("audio/vorbis");
	}

	/**
	 * Gets the recording information using MP3 ID3 tags.
	 * @param path The path to the recording on disk
	 * @param filename The filename of the file to process
	 * @return A Recording object on success, null on failure.
	 */
	public static Recording getRecordingInfo(String path, String filename) {
		String fullFilename = Utilities.buildPath(path, filename);
		return MetaData.buildRecordingRecord(fullFilename);
	}

	/**
	 * Gets recording information from the AcoustID database.
	 * @param chromaprint Contains a fingerprint and duration for a track.
	 * @todo Simplify this method
	 */
	public static Recording getRecordingInfo(Chromaprint chromaprint) {
		Recording retVal = null;
		Configuration config = Configuration.getInstance();
		// http://api.acoustid.org/v2/lookup
		URI uri = null;
		try {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost("api.acoustid.org")
					.setPath("/v2/lookup")
					.setParameter("format", "json")
					.setParameter("client", config.getAcoustIDKey())
					.setParameter("fingerprint", chromaprint.getFingerprint())
					.setParameter("duration", chromaprint.getDuration())
					.build();
		} catch(URISyntaxException e) {
			// Log an error of some sort
		}
		if(uri != null) {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet(uri);
			CloseableHttpResponse response = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String line;
			try {
				response = client.execute(getRequest);
				HttpEntity entity = response.getEntity();
				isr = new InputStreamReader(entity.getContent());
				br = new BufferedReader(isr);
				line = br.readLine();
				while(line != null) {
					// @todo parse the response into a Recording record
					logger.error("Line: {}", line);
					line = br.readLine();
				}
			} catch(IOException e) {
				// Do something here
			} finally {
				try {
					if (response != null) response.close();
				} catch(IOException e) {

				}
				try {
					if(br != null) br.close();
				} catch(IOException e) {

				}
				try {
					if(isr != null) isr.close();
				} catch(IOException e) {

				}
			}
		}
		return retVal;
	}

	/**
	 * Call fpcalc to get a fingerprint ID. This code is a perfect example of why exceptions
	 * are so *sarcastically* awesome.
	 * @todo break this up so its easier to read later
	 * @param filename The name of the media file to fingerprint. This should be a fully qualified path.
	 * @return A String containing the fingerprint or null on error. Error messages are logged.
	 */
	public static Chromaprint getChromaprint(String filename) {
		Configuration config = Configuration.getInstance();
		String fingerprint = null;
		String duration = null;
		StringBuffer errorMessage = new StringBuffer();
		Chromaprint retVal = null;
		InputStreamReader isr = null;
		BufferedReader processOutput = null;
		String command = "fpcalc";
		ProcessBuilder pb = new ProcessBuilder(command, filename);
		Process p = null;
		pb.directory(new File(config.getMediaPath()));
		pb.redirectErrorStream();
		try {
			p = pb.start();
			isr = new InputStreamReader(p.getInputStream());
			processOutput = new BufferedReader(isr);
			String line = processOutput.readLine();
			int i = 0;
			while (line != null) {
				if (line.startsWith("FINGERPRINT=")) {
					fingerprint = line.substring(12, line.length());
					if(!fingerprint.isEmpty())
						fingerprint = fingerprint.trim();
					if(retVal == null) retVal = new Chromaprint();
					retVal.setFingerprint(fingerprint);
				} else if(line.startsWith("DURATION=")) {
					duration = line.substring(9, line.length());
					if(!duration.isEmpty())
						duration = duration.trim();
					if(retVal == null) retVal = new Chromaprint();
					retVal.setDuration(duration);
				} else if (line.startsWith("ERROR:")) {
					errorMessage.append(line);
				}
				line = processOutput.readLine();
				++i;
				if(i > 30) {
					// Don't loop forever if we get unexpected output.
					errorMessage.append("Read a ton of data I don't understand. Bailing.");
					break;
				}
			}
		} catch(IOException e) {
			RecordingProcessor.logger.error("Couldn't get fingerprint -- IOException: ", e);
		} finally {
			// Close our buffers, catch any loose errors
			try {
				if (null != isr) isr.close();
			} catch(IOException e) {
				RecordingProcessor.logger.error("Couldn't close the InputStreamReader -- IOException: ", e);
			}
			try {
				if(null != processOutput) processOutput.close();
			} catch(IOException e) {
				RecordingProcessor.logger.error("Couldn't close the BufferedReader -- IOException: ", e);
			}
			// We should be done with the process. Destroy it if it hasn't terminated already.
			try {
				p.exitValue();
			} catch(IllegalThreadStateException e) {
				p.destroy();
			}
		}
		if(fingerprint.isEmpty()) {
			RecordingProcessor.logger.error("Error getting fingerprint: {}", errorMessage);
		}
		return retVal;
	}
}
