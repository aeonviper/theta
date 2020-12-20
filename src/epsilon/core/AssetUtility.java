package epsilon.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import executable.Executable;

public class AssetUtility {

	public static char codecCharacter = '^';

	public static File convertBinaryLinux = new File("/usr/bin/convert");
	public static File convertBinaryWindows = new File("c:\\system\\application\\imagemagick\\convert.exe");

	public static File identifyBinaryLinux = new File("/usr/bin/identify");
	public static File identifyBinaryWindows = new File("c:\\system\\application\\imagemagick\\identify.exe");

	public static File compositeBinaryLinux = new File("/usr/bin/composite");
	public static File compositeBinaryWindows = new File("c:\\system\\application\\imagemagick\\composite.exe");

	private static Integer zero = new Integer(0);

	public static String encodeAssetName(String text) {
		StringBuilder sb = new StringBuilder();
		for (char q : text.toCharArray()) {
			if ((q >= 'a' && q <= 'z') || (q >= 'A' && q <= 'Z') || (q >= '0' && q <= '9') || (q == ' ') || (q == '_') || (q == '.') || (q == '-')) {
				sb.append(q);
			} else {
				sb.append(codecCharacter + Integer.toHexString((int) q) + codecCharacter);
			}
		}
		return sb.toString();
	}

	public static String decodeAssetName(String text) {
		int state = 0;
		StringBuilder sb = new StringBuilder();
		StringBuilder region = new StringBuilder();
		for (char q : text.toCharArray()) {
			if (state == 0) {
				if (q == codecCharacter) {
					state = 1;
					region = new StringBuilder();
				} else {
					sb.append(q);
				}
			} else if (state == 1) {
				if (q == codecCharacter) {
					state = 0;
					int n = (int) Long.parseLong(region.toString(), 16);
					sb.append((char) n);
				} else {
					region.append(q);
				}

			}
		}
		return sb.toString();
	}

	public static boolean resizeAsset(File source, File destination, int width, int height, boolean overwrite) {
		File convertBinary = File.separatorChar == '/' ? convertBinaryLinux : convertBinaryWindows;
		if (convertBinary.exists()) {
			if (source != null && source.isFile()) {
				String fileName = source.getName().toLowerCase();
				if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
					if (overwrite || !destination.isFile()) {
						List<String> logList = new ArrayList<>();

						File parentFile = destination.getParentFile();
						if (parentFile != null && !parentFile.exists()) {
							parentFile.mkdirs();
						}

						// only shrink large image, use the flag '>'
						Executable executable = new Executable( //
								logList, //
								new String[] { convertBinary.getAbsolutePath(), (width <= 100 && height <= 100) ? "-thumbnail" : "-resize", width + "x" + height + ">", source.getAbsolutePath(), destination.getAbsolutePath() }, //
								"/tmp", //
								"/tmp", //
								60, //
								Collections.emptyMap() //
						);
						executable.run();
						if (!zero.equals(executable.getStatusCode())) {
							for (String log : logList) {
								System.out.println(log);
							}
						}
						return zero.equals(executable.getStatusCode());
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		} else {
			return Utility.copyFile(source, destination);
		}
		return false;
	}

	public static boolean resizeAsset(String fileName, boolean overwrite) {
		File source = new File(Constant.assetPath + File.separator + "file" + File.separator + fileName);
		return resizeAsset(source, new File(Constant.assetPath + File.separator + "small" + File.separator + fileName), 100, 100, overwrite) //
				&& resizeAsset(source, new File(Constant.assetPath + File.separator + "medium" + File.separator + fileName), 300, 300, overwrite) //
				&& resizeAsset(source, new File(Constant.assetPath + File.separator + "large" + File.separator + fileName), 600, 600, overwrite);
	}

	public static boolean resizeAsset(String fileName) {
		return resizeAsset(fileName, true);
	}

	private static String[] assetLocationList = new String[] { File.separator + "file", File.separator + "small", File.separator + "medium", File.separator + "large" };

	public static void deleteAsset(String fileName) {
		File oldFile = null;

		for (String location : assetLocationList) {
			oldFile = new File(Constant.assetPath + location + File.separator + fileName);
			if (oldFile.canWrite()) {
				oldFile.delete();
			}
		}
	}

	public static Map<String, String> sizeImage(File source) {
		File identifyBinary = File.separatorChar == '/' ? identifyBinaryLinux : identifyBinaryWindows;
		if (identifyBinary.exists() && source != null && source.isFile()) {
			String fileName = source.getName().toLowerCase();
			if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
				List<String> logList = new ArrayList<>();

				Executable executable = new Executable( //
						logList, //
						new String[] { identifyBinary.getAbsolutePath(), "-ping", "-format", "%w %h", source.getAbsolutePath() }, //
						"/tmp", //
						"/tmp", //
						30, //
						Collections.emptyMap() //
				);
				executable.run();
				if (zero.equals(executable.getStatusCode())) {
					if (logList.size() >= 1) {
						String[] fields = logList.get(0).split(" ", 2);
						Map<String, String> sizeMap = new HashMap<>();
						sizeMap.put("width", fields[0]);
						sizeMap.put("height", fields[1]);
						return sizeMap;
					}
				} else {
					for (String log : logList) {
						System.out.println(log);
					}
				}
			}
		}
		return null;
	}

	public static String imageMetaData(File source) {
		File identifyBinary = File.separatorChar == '/' ? identifyBinaryLinux : identifyBinaryWindows;
		if (identifyBinary.exists() && source != null && source.isFile()) {
			String fileName = source.getName().toLowerCase();
			if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
				List<String> logList = new ArrayList<>();

				Executable executable = new Executable( //
						logList, //
						new String[] { identifyBinary.getAbsolutePath(), "-ping", "-format", "%r", source.getAbsolutePath() }, //
						"/tmp", //
						"/tmp", //
						30, //
						Collections.emptyMap() //
				);
				executable.run();
				if (zero.equals(executable.getStatusCode())) {
					if (logList.size() >= 1) {
						return logList.get(0);
					}
				} else {
					for (String log : logList) {
						System.out.println(log);
					}
				}
			}
		}
		return null;
	}

	public static boolean isColorspaceCMYK(File source) {
		String metaData = imageMetaData(source);
		return Utility.stringify(metaData).contains("CMYK");
	}

	protected static String escape(String value) {
		if (File.separatorChar == '/') {
			return Utility.stringify(value).replace("\\", "\\\\").replace("'", "\\'");
		} else {
			return Utility.stringify(value).replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
		}

	}

}
