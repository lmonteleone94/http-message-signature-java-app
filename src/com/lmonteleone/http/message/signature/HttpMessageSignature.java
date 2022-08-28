package com.lmonteleone.http.message.signature;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.LinkedHashMap;

import org.apache.commons.codec.binary.Base64;

public class HttpMessageSignature {
	
	
	/***
	 * 
	 * Create Message Header
	 * 
	 */
	public static LinkedHashMap<String, String> getMessageHeader(String host, String date, String body) throws NoSuchAlgorithmException {
		LinkedHashMap<String, String> header = new LinkedHashMap<String, String>();
		
		header.put(Constants.HttpMessageHeader.Host.name(), host);
		header.put(Constants.HttpMessageHeader.Date.name(), date);
		header.put(Constants.HttpMessageHeader.Digest.name(), createDigest(body, Constants.DIGEST_ALGORITHM));

		return header;
	}

	/***
	 * 
	 * Create Digest Field
	 * 
	 */
	private static String createDigest(String body, String digestAlgorithm) throws NoSuchAlgorithmException {
		return digestAlgorithm + "=" + new String(Base64.encodeBase64(
				MessageDigest.getInstance(digestAlgorithm).digest(body.getBytes(StandardCharsets.UTF_8))));
	}
	
	/***
	 * 
	 * get Private Key from .pem file
	 * 
	 */
	private static PrivateKey getPrivateKey(String filename)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		//Read pem file
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();
		
		//Trasform private key to String
		String temp = new String(keyBytes);
		String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
		privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
		
		
		Base64 b64 = new Base64();
		byte[] decoded = b64.decode("\n" + privKeyPEM + "\n");
		
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
		KeyFactory kf = KeyFactory.getInstance(Constants.KEY_TYPE);
		return kf.generatePrivate(spec);
	}
	
	/***
	 * 
	 * Create Signing String from Header 
	 * 
	 */
	public static String createSigningString(String method, LinkedHashMap<String, String> headers, String path) {
		StringBuilder signingString = new StringBuilder(Constants.REQUEST_TARGET + ": " + method.toLowerCase() + " " + path);

		for (String key : headers.keySet()) {
			signingString.append("\n" + key.toLowerCase() + ": " + headers.get(key));
		}

		return signingString.toString();
	}
	
	/***
	 * 
	 * Sign with RSA (rsa-sha256) algorithm the previously created Signing String with private key, using Base64 as output
	 * 
	 */
	public static String createSignature(String signingString) throws Exception {
		PrivateKey privateKey = getPrivateKey(Constants.PEM_FILE_PATH);
		
		Signature sign = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
		sign.initSign(privateKey);
		sign.update(signingString.getBytes(Constants.CHARSETS_TYPE));
		
		return new String(Base64.encodeBase64(sign.sign()), Constants.CHARSETS_TYPE);
	}
	
	/***
	 * 
	 * Create Authorization Header Field
	 * 
	 */
	public static String createAuthHeader(String key_id, String algorithm, LinkedHashMap<String, String> headers,
			String signature) {
		StringBuilder headerFields = new StringBuilder(Constants.REQUEST_TARGET);

		for (String key : headers.keySet()) {
			headerFields.append(" " + key.toLowerCase());
		}

		StringBuilder authHeader = new StringBuilder("");
		authHeader.append(Constants.AuthHeader.Signature.name() + " ");
		authHeader.append(Constants.AuthHeader.keyId.name() + "=\"" + key_id + "\", ");
		authHeader.append(Constants.AuthHeader.algorithm.name() + "=\"" + algorithm + "\", ");
		authHeader.append(Constants.AuthHeader.headers.name() + "=\"" + headerFields + "\", ");
		authHeader.append(Constants.AuthHeader.signature.name() + "=\"" + signature + "\"");
		return authHeader.toString();
	}

	
}
