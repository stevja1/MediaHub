package org.jaredstevens.servers.db.exceptions;

public class InvalidUserException extends Exception {
	public InvalidUserException() {
		super("User not found.");
	}

	public InvalidUserException(String message) {
		super(message);
	}
}
