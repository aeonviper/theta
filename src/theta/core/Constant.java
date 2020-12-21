package theta.core;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

public class Constant extends epsilon.core.Constant {

	private static final String jwtSecretKey = "vmFvHV%pHxA[3iW[5$TLKzkp}XZFexlgW6e*Zssw9cosEvpO@n~[$m5LmVXEYMnvZLaTVfuT4qFIPfJU{QdrIL24I9<3(Q<^09Bj8b5e9S[}O1c<uBQZIto#SSaB$mzd";
	public static final Signer jwtSigner = HMACSigner.newSHA512Signer(jwtSecretKey);
	public static final Verifier jwtVerifier = HMACVerifier.newVerifier(jwtSecretKey);
	public static final String jwtCookieName = "theta-jwt";

}
