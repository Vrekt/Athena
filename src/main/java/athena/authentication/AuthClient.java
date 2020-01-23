package athena.authentication;

import java.util.Base64;

/**
 * A collection of client IDs and secret tokens that are used for authentication.
 * Allows access to certain services only accessible through a certain client. 
 */
public enum AuthClient {

	FORTNITE_PC_GAME_CLIENT("ec684b8c687f479fadea3cb2ad83f5c6", "e1f31c211f28413186262d37a13fc84d"),
	FORTNITE_IOS_GAME_CLIENT("3446cd72694c4a4485d81b77adbb2141", "9209d4a5e25a457fb9b07489d313b41a"),
	FORTNITE_ANDROID_GAME_CLIENT("3f69e56c7649492c8cc29f1af08a8a12", "b51ee9cb12234f50a69efa67ef53812e"),
	FORTNITE_CHINA_GAME_CLIENT("efe3cbb938804c74b20e109d0efc1548", "6e31bdbae6a44f258474733db74f39ba"),
	KAIROS_PC_CLIENT("5b685653b9904c1d92495ee8859dcb00", "7Q2mcmneyuvPmoRYfwM7gfErA6iUjhXr"),
	KAIROS_IOS_CLIENT("61d2f70175e84a6bba80a5089e597e1c", "FbiZv3wbiKpvVKrAeMxiR6WhxZWVbrvA"),
	KAIROS_ANDROID_CLIENT("0716a69cb8b2422fbb2a8b0879501471", "cGthdfG68tyE7M3ZHMu3sXUBwqhibKFp");
	
	public String clientId;
	public String secret;
	
	AuthClient(String clientId, String secret) {
		this.clientId = clientId;
		this.secret = secret;
	}
	
	public String getClientId() {
		return this.clientId;
	}
	
	public String getSecret() {
		return this.secret;
	}
	
	/**
	 * Get a valid authentication token.
	 * @return authentication token that can be used on account service.
	 */
	public String getToken() {
		String plainToken = this.clientId + ":" + this.secret;
		return new String(Base64.getEncoder().encode(plainToken.getBytes()));
	}
}
