package theta.core;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Utility extends epsilon.core.Utility {

	public static final Type typeListOfLong = new TypeToken<List<Long>>() {
	}.getType();

	public static final Type typeListOfString = new TypeToken<List<String>>() {
	}.getType();

}
