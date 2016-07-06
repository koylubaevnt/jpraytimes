package com.koylubaevnt.logging.main;

import org.apache.log4j.Logger;

public class MainApplication {

	private static final Logger logger = Logger.getLogger(MainApplication.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.debug("DEBUG: Starting mass rate charge calculation...");
		logger.info("INFO: Starting mass rate charge calculation...");
		logger.warn("WARN: Starting mass rate charge calculation...");
		logger.error("ERROR: Starting mass rate charge calculation...");
	}

}
