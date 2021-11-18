package theta.core;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

import theta.model.PersonRole;

public class Utility extends epsilon.core.Utility {

	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final Type typeListOfLong = new TypeToken<List<Long>>() {
	}.getType();

	public static final Type typeListOfString = new TypeToken<List<String>>() {
	}.getType();

	public static final Type typeSetOfPersonRole = new TypeToken<Set<PersonRole>>() {
	}.getType();

	public static Integer page(Integer page) {
		if (page != null && page >= 1) {
			return page;
		}
		return 1;
	}

	public static Integer pageSize(Integer pageSize) {
		if (pageSize != null && (pageSize > 0 || pageSize == -1)) {
			return pageSize;
		}
		return 500;
	}

}
