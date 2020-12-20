package epsilon.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Utility extends orion.core.Utility {

	public static final Gson gson = new GsonBuilder() //
			// .disableHtmlEscaping() //
			.create();

	public static final JsonParser jsonParser = new JsonParser();

	public static final DateTimeFormatter fileDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
	public static final DateTimeFormatter fullDateTimeFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
	public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
	public static final DateTimeFormatter isoDateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;
	public static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");

	public static final Type typeMapOfStringObject = new TypeToken<Map<String, Object>>() {
	}.getType();

	public static final Type typeListOfLong = new TypeToken<List<Long>>() {
	}.getType();

	public static final Type typeListOfString = new TypeToken<List<String>>() {
	}.getType();

	public static boolean copyFile(File source, File destination) {
		return copyFile(source, destination, true);
	}

	public static boolean copyFile(File source, File destination, boolean overwrite) {
		try {
			if (!overwrite && destination != null && destination.exists()) {
				return true;
			}
			Files.copy(source, destination);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
