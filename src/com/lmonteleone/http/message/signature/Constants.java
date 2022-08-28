package com.lmonteleone.http.message.signature;

/**
 * 
 * Constants useful for implementation and testing
 * 
 * **/
public class Constants {
	
	public static final String PATH = "/wally-services/protocol/tests/signature";
	public static final String HOST = "staging.authservices.satispay.com";
	public static final String URL = "https://staging.authservices.satispay.com/wally-services/protocol/tests/signature";
	public static final String CONTENT_TYPE = "application/json";

	public static final String KEY_ID = "signature-test-66289";
	public static final String ALGOTITHM = "rsa-sha256";
	
	public static final String DIGEST_ALGORITHM = "SHA-256";
	public static final String KEY_TYPE = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	
	public static final String REQUEST_TARGET = "(request-target)";
	
	public static final String CHARSETS_TYPE = "UTF-8";
	public static final String PEM_FILE_PATH = "files/client-rsa-private-key.pem";
	
	
	public static final String DATE = "Mon, 18 Mar 2019 15:10:24 +0000";
	public static final String BODY = "{\n" + "  \"flow\": \"MATCH_CODE\",\n" + "  \"amount_unit\": 70,\n"
			+ "  \"currency\": \"EUR\"\n" + "}\n";
	public static final String EMPTY_BODY = "";
	
	public static final String SEPARETOR = "---------------------------------------------------------------------------------------------------";
	
	public enum HttpMethods {
		GET,
		DELETE,
		POST,
		PUT,
		PATCH
	}
	
	public enum HttpMessageHeader {
		Host,
		Date,
		Digest
	}
	
	public enum AuthHeader {
		Signature, 
		keyId,
		algorithm, 
		headers, 
		signature
	}

}
