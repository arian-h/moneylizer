package com.karen.moneylizer.security;

public final class SecurityConstants {
	/*
	 * Authentication will only be initiated for the requests with matching URLs
	 */
	protected final static String DEFAULT_FILTER_PROCESS_URL = "/**";

	public static final String AUTHENTICATION_HEADER = "Authorization";

	public static final String BEARER = "Bearer";

	public static final String JWT_SECRET = System.getenv("MONEYLIZER_JWT_SECRET_KEY");

	public static final long EXPIRATIONTIME = 864_000_000; // 10 days

}