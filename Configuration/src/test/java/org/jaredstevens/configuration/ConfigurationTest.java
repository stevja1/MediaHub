package org.jaredstevens.configuration;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConfigurationTest {
	private Configuration config;

	@Test
	public void testReadConfig() {
		this.config = Configuration.getInstance();
		if(this.config == null) fail("Failed to parse the configuration.");
		if(!this.config.getScriptPath().equals("/home/jarestev/test-scripts")) fail("Failed to parse script path.");
		if(!this.config.getMediaPath().equals("/home/jarestev/test-media")) fail("Failed to parse media path.");
		if(!this.config.getMediaBaseURL().equals("http://apps.jaredstevens.org/test-media")) fail("Failed to match media base url.");
		if(!this.config.getMediaAPIURL().equals("http://apps.jaredstevens.org/test-MediaServerWS")) fail("Failed to match media api url.");
		if(!this.config.getAcoustIDKey().equals("TESTKEY")) fail("Failed to match AcoustID API key.");
	}
}