package com.tp.Model;


/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Class that passes the message higher.
 *
 */
public class InvalidMoveException extends Exception {

	public InvalidMoveException(String message) {
		super(message);
	}

}
