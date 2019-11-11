package com.fullstack.utility;

import java.io.File;

public class Constants {

	public static final String USER_FOLDER = "src/main/resources/static/image/user//";
	public static final String POST_FOLDER = "src/main/resources/static/image/post//";
	public static final File DEFAULT_PIC = new File("src/main/resources/static/image/user/temp/profile.png");
	
	// Security Constants
	public static final String SECRET = "^[a-zA-Z0-9._]+$\r\nGuidelines89797987forAlphabeticalArraNumeralsandOtherSymbo$";
	public static final long EXPIRATION_TIME = 432_000_000; // 5 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_TYPE = "Authorization";
	public static final String CLIENT_DOMAIN_URL = "http://localhost:4200/*";
}
