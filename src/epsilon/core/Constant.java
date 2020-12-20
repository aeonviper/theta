package epsilon.core;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

public class Constant extends orion.core.Constant {

	private static final String jwtSecretKey = "vmFvHV%pHxA[3iW[5$TLKzkp}XZFexlgW6e*Zssw9cosEvpO@n~[$m5LmVXEYMnvZLaTVfuT4qFIPfJU{QdrIL24I9<3(Q<^09Bj8b5e9S[}O1c<uBQZIto#SSaB$mzd";
	public static final Signer jwtSigner = HMACSigner.newSHA512Signer(jwtSecretKey);
	public static final Verifier jwtVerifier = HMACVerifier.newVerifier(jwtSecretKey);
	public static final String jwtCookieName = "epsilon-jwt";

	public static final File assetPath;
	public static final File assetFilePath;

	public static final Properties propertyRepository;

	static {
		File file = null;
		Properties properties = new Properties();
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(applicationConfigurationFile);
			if (url != null) {
				properties.load(url.openStream());

				file = new File(url.getPath());
				file = file.getParentFile().getParentFile().getParentFile();
				file = new File(file, "asset");
				System.out.println("Asset location: " + file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading file. Cause: " + e);
		} finally {

		}
		propertyRepository = properties;

		assetPath = file;
		assetFilePath = new File(assetPath, "file");
		assetFilePath.mkdirs();

		new File(assetPath, "small").mkdirs();
		new File(assetPath, "medium").mkdirs();
		new File(assetPath, "large").mkdirs();
	}
}
