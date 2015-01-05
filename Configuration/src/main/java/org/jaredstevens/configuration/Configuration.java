package org.jaredstevens.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

/**
 * This is a singleton class for our application's configuration. It loads the configuration from a file called
 * 'MediaServerConfig.json' that should reside somewhere in the classpath. I've done it this way so that its easy to
 * test this code with a unit test, as well as to deploy it on test/stage/prod environments.
 */
public class Configuration {
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	private String scriptPath;
	private String mediaPath;
	private String mediaBaseURL;
	private String mediaAPIURL;
	private String acoustIDKey;
	private static String configFile = Configuration.getConfigFilename();
	private static Configuration ourInstance = Configuration.readConfig(Configuration.configFile);

	public static Configuration getInstance() {
		return ourInstance;
	}

	private Configuration() {
	}

	/**
	 * Read in the configuration file
	 * @param file The file to read
	 * @return A configuration object
	 */
	public static Configuration readConfig(String file) {
		Configuration retVal = null;
		BufferedReader br = null;
		FileReader fr = null;
		try {
			// Read in JSON configuration
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			retVal = Configuration.parseConfig(br);
		} catch (FileNotFoundException e) {
			Configuration.logger.error("Couldn't find a valid configuration file: {}", file);
		} finally {
			try {
				if (br != null) br.close();
				if (fr != null) fr.close();
			} catch(IOException e) {
				Configuration.logger.error("There was a problem closing our readers: {}", e.getMessage());
			}
		}
		return retVal;
	}

	/**
	 * A method used to get the configuration filename. Checks for a file called MediaServerConfig.json in the
	 * classpath. If not found, it defaults to '/config/MediaServerConfig.json' on the local file system.
	 * @return The path to a configuration file or null if none was found in the classpath.
	 */
	public static String getConfigFilename() {
		String retVal = null;
		// Try to get the filename from the classpath
		URL configURL = ClassLoader.getSystemResource("MediaServerConfig.json");
		if(configURL == null) {
			Configuration.logger.error("Couldn't find a configuration file in the classpath: {}",
					System.getProperty("java.class.path"));
		} else {
			retVal = configURL.getPath();
			retVal = Configuration.cleanConfigFilename(retVal);
		}
		// If nothing is found, try the default location
		if(retVal == null) {
			retVal = "/config/MediaServerConfig.json";
		}
		return retVal;
	}

	/**
	 * URLDecodes the path. This is helpful because the ClassLoader.getSystemResource method returns url encoded paths.
	 * @param path The path to decode
	 * @return A string containing the url decoded path. Returns null if there is a problem parsing the path.
	 */
	public static String cleanConfigFilename(String path) {
		String retVal = null;
		String enc = "UTF-8";
		try {
			retVal = URLDecoder.decode(path, enc);
		} catch(UnsupportedEncodingException e) {
			Configuration.logger.error("Unsupported encoding ("+enc+") used when decoding the config filename: "+e.getMessage());
		}
		return retVal;
	}

	/**
	 * Parses the configuration from a JSON string to a Configuration object.
	 * @param br A BufferedReader to use when reading in the JSON
	 * @return A Configuration object populated with fields from the JSON. Returns null if there is a failure.
	 */
	public static Configuration parseConfig(BufferedReader br) {
		Configuration retVal = null;
		Gson gson = new Gson();
		try {
			retVal = gson.fromJson(br, Configuration.class);
		} catch (JsonSyntaxException e) {
			Configuration.logger.error("There was a problem parsing the configuration: {}", e.getMessage());
		}
		return retVal;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaBaseURL() {
		return mediaBaseURL;
	}

	public void setMediaBaseURL(String mediaBaseURL) {
		this.mediaBaseURL = mediaBaseURL;
	}

	public String getMediaAPIURL() { return mediaAPIURL; }

	public void setMediaAPIURL(String mediaAPIURL) { this.mediaAPIURL = mediaAPIURL; }

	public String getAcoustIDKey() { return acoustIDKey; }

	public void setAcoustIDKey(String acoustIDKey) { this.acoustIDKey = acoustIDKey; }
}
