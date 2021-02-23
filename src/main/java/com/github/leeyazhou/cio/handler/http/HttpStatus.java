package com.github.leeyazhou.cio.handler.http;

public class HttpStatus {

	// --- 1xx Informational ---

	/** {@code 100 Continue} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_CONTINUE = new HttpStatus(100, "CONTINUE");
	/** {@code 101 Switching Protocols} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_SWITCHING_PROTOCOLS = new HttpStatus(101, "SWITCHING_PROTOCOLS");
	/** {@code 102 Processing} (WebDAV - RFC 2518) */
	public static final HttpStatus SC_PROCESSING = new HttpStatus(102, "PROCESSING");

	// --- 2xx Success ---

	/** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_OK = new HttpStatus(200, "OK");
	/** {@code 201 Created} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_CREATED = new HttpStatus(201, "CREATED");
	/** {@code 202 Accepted} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_ACCEPTED = new HttpStatus(202, "ACCEPTED");
	/** {@code 203 Non Authoritative Information} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_NON_AUTHORITATIVE_INFORMATION = new HttpStatus(203,
			"NON_AUTHORITATIVE_INFORMATION");
	/** {@code 204 No Content} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_NO_CONTENT = new HttpStatus(204, "NO_CONTENT");
	/** {@code 205 Reset Content} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_RESET_CONTENT = new HttpStatus(205, "RESET_CONTENT");
	/** {@code 206 Partial Content} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_PARTIAL_CONTENT = new HttpStatus(206, "PARTIAL_CONTENT");
	/**
	 * {@code 207 Multi-Status} (WebDAV - RFC 2518) or {@code 207 Partial Update OK}
	 * (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)
	 */
	public static final HttpStatus SC_MULTI_STATUS = new HttpStatus(207, "MULTI_STATUS");

	// --- 3xx Redirection ---

	/** {@code 300 Mutliple Choices} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_MULTIPLE_CHOICES = new HttpStatus(300, "MULTIPLE_CHOICES");
	/** {@code 301 Moved Permanently} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_MOVED_PERMANENTLY = new HttpStatus(301, "MOVED_PERMANENTLY");
	/**
	 * {@code 302 Moved Temporarily} (Sometimes {@code Found}) (HTTP/1.0 - RFC 1945)
	 */
	public static final HttpStatus SC_MOVED_TEMPORARILY = new HttpStatus(302, "MOVED_TEMPORARILY");
	/** {@code 303 See Other} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_SEE_OTHER = new HttpStatus(303, "SEE_OTHER");
	/** {@code 304 Not Modified} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_NOT_MODIFIED = new HttpStatus(304, "NOT_MODIFIED");
	/** {@code 305 Use Proxy} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_USE_PROXY = new HttpStatus(305, "USE_PROXY");
	/** {@code 307 Temporary Redirect} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_TEMPORARY_REDIRECT = new HttpStatus(307, "TEMPORARY_REDIRECT");

	// --- 4xx Client Error ---

	/** {@code 400 Bad Request} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_BAD_REQUEST = new HttpStatus(400, "BAD_REQUEST");
	/** {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_UNAUTHORIZED = new HttpStatus(401, "UNAUTHORIZED");
	/** {@code 402 Payment Required} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_PAYMENT_REQUIRED = new HttpStatus(402, "PAYMENT_REQUIRED");
	/** {@code 403 Forbidden} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_FORBIDDEN = new HttpStatus(403, "FORBIDDEN");
	/** {@code 404 Not Found} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_NOT_FOUND = new HttpStatus(404, "NOT_FOUND");
	/** {@code 405 Method Not Allowed} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_METHOD_NOT_ALLOWED = new HttpStatus(405, "METHOD_NOT_ALLOWED");
	/** {@code 406 Not Acceptable} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_NOT_ACCEPTABLE = new HttpStatus(406, "NOT_ACCEPTABLE");
	/** {@code 407 Proxy Authentication Required} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_PROXY_AUTHENTICATION_REQUIRED = new HttpStatus(407,
			"PROXY_AUTHENTICATION_REQUIRED");
	/** {@code 408 Request Timeout} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_REQUEST_TIMEOUT = new HttpStatus(408, "REQUEST_TIMEOUT");
	/** {@code 409 Conflict} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_CONFLICT = new HttpStatus(409, "CONFLICT");
	/** {@code 410 Gone} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_GONE = new HttpStatus(410, "GONE");
	/** {@code 411 Length Required} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_LENGTH_REQUIRED = new HttpStatus(411, "LENGTH_REQUIRED");
	/** {@code 412 Precondition Failed} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_PRECONDITION_FAILED = new HttpStatus(412, "PRECONDITION_FAILED");
	/** {@code 413 Request Entity Too Large} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_REQUEST_TOO_LONG = new HttpStatus(413, "REQUEST_TOO_LONG");
	/** {@code 414 Request-URI Too Long} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_REQUEST_URI_TOO_LONG = new HttpStatus(414, "REQUEST_URI_TOO_LONG");
	/** {@code 415 Unsupported Media Type} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_UNSUPPORTED_MEDIA_TYPE = new HttpStatus(415, "UNSUPPORTED_MEDIA_TYPE");
	/** {@code 416 Requested Range Not Satisfiable} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_REQUESTED_RANGE_NOT_SATISFIABLE = new HttpStatus(416,
			"REQUESTED_RANGE_NOT_SATISFIABLE");
	/** {@code 417 Expectation Failed} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_EXPECTATION_FAILED = new HttpStatus(417, "EXPECTATION_FAILED");

	/**
	 * Static constant for a 418 error. {@code 418 Unprocessable Entity} (WebDAV
	 * drafts?) or {@code 418 Reauthentication Required} (HTTP/1.1 drafts?)
	 */
	// not used
	// public static final HttpStatus SC_UNPROCESSABLE_ENTITY = new HttpStatus( 418,
	// "");

	/**
	 * Static constant for a 419 error. {@code 419 Insufficient Space on Resource}
	 * (WebDAV - draft-ietf-webdav-protocol-05?) or
	 * {@code 419 Proxy Reauthentication Required} (HTTP/1.1 drafts?)
	 */
	public static final HttpStatus SC_INSUFFICIENT_SPACE_ON_RESOURCE = new HttpStatus(419,
			"INSUFFICIENT_SPACE_ON_RESOURCE");
	/**
	 * Static constant for a 420 error. {@code 420 Method Failure} (WebDAV -
	 * draft-ietf-webdav-protocol-05?)
	 */
	public static final HttpStatus SC_METHOD_FAILURE = new HttpStatus(420, "METHOD_FAILURE");
	/** {@code 422 Unprocessable Entity} (WebDAV - RFC 2518) */
	public static final HttpStatus SC_UNPROCESSABLE_ENTITY = new HttpStatus(422, "UNPROCESSABLE_ENTITY");
	/** {@code 423 Locked} (WebDAV - RFC 2518) */
	public static final HttpStatus SC_LOCKED = new HttpStatus(423, "LOCKED");
	/** {@code 424 Failed Dependency} (WebDAV - RFC 2518) */
	public static final HttpStatus SC_FAILED_DEPENDENCY = new HttpStatus(424, "FAILED_DEPENDENCY");

	// --- 5xx Server Error ---

	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_HttpStatusERNAL_SERVER_ERROR = new HttpStatus(500,
			"HttpStatusERNAL_SERVER_ERROR");
	/** {@code 501 Not Implemented} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_NOT_IMPLEMENTED = new HttpStatus(501, "NOT_IMPLEMENTED");
	/** {@code 502 Bad Gateway} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_BAD_GATEWAY = new HttpStatus(502, "BAD_GATEWAY");
	/** {@code 503 Service Unavailable} (HTTP/1.0 - RFC 1945) */
	public static final HttpStatus SC_SERVICE_UNAVAILABLE = new HttpStatus(503, "SERVICE_UNAVAILABLE");
	/** {@code 504 Gateway Timeout} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_GATEWAY_TIMEOUT = new HttpStatus(504, "GATEWAY_TIMEOUT");
	/** {@code 505 HTTP Version Not Supported} (HTTP/1.1 - RFC 2616) */
	public static final HttpStatus SC_HTTP_VERSION_NOT_SUPPORTED = new HttpStatus(505, "HTTP_VERSION_NOT_SUPPORTED");

	/** {@code 507 Insufficient Storage} (WebDAV - RFC 2518) */
	public static final HttpStatus SC_INSUFFICIENT_STORAGE = new HttpStatus(507, "INSUFFICIENT_STORAGE");

	private final int code;
	private final String status;

	public HttpStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return code + " " + status;
	}

}
