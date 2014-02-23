package org.jaredstevens.servers.utilities;

import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.db.operations.ConfigurationOps;

public class Utilities {
	public static String buildPath(String part1, String part2) {
		String retVal;
		if(part1.endsWith("/") && part2.startsWith("/")) {
			part1 = part1.substring(0, part1.length()-1);
			retVal = part1+part2;
		} else if(!part1.endsWith("/") && !part2.startsWith("/")) {
			retVal = part1+"/"+part2;
		} else retVal = part1+part2;
		return retVal;
	}

	public static Configuration getConfig(ConfigurationOps conn) {
		return conn.get();
	}
}
