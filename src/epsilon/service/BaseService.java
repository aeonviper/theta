package epsilon.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import common.BeanUtility;
import epsilon.core.Utility;
import epsilon.model.BaseModel;
import omega.annotation.Transactional;
import omega.service.GenericService;

public class BaseService extends GenericService {

	@Transactional
	public <T> int create(String tableName, String sequenceName, T entity, String... array) {
		String sql = "insert into " + tableName + " (id," + Utility.join(array, ",") + ") values ((select next value for " + sequenceName + ")," + Utility.repeat("?", array.length, ",") + ")";
		List parameterList = new ArrayList<>();
		try {
			for (String entry : array) {
				parameterList.add(BeanUtility.instance().getPropertyUtils().getProperty(entity, entry));
			}
			return write(sql, parameterList.toArray());
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public <T> int create(String tableName, BaseModel entity, String... array) {
		int result = super.insert(tableName, entity, array);
		if (result == 1) {
			entity.setId(id());
		}
		return result;
	}

	@Transactional
	public Long sequence(String name) {
		return select(Long.class, "select next value for " + name + "");
	}

	@Transactional
	public Long id() {
		return select(Long.class, "select last_insert_id()");
	}

	public static void toDecorate(BaseModel entity) {
		if (entity == null) {
			return;
		}
		if (entity.getMap() != null) {
			entity.setMapData(Utility.gson.toJson(entity.getMap()));
		}
	}

	public static void fromDecorate(BaseModel entity) {
		if (entity == null) {
			return;
		}
		if (Utility.isNotBlank(entity.getMapData())) {
			entity.setMap(Utility.gson.fromJson(entity.getMapData(), Utility.typeMapOfStringObject));
			entity.setMapData(null);
		}
	}

	public static <T extends BaseModel> List<T> apply(List<T> list, Function<T, T>... functionArray) {
		if (list != null) {
			for (T entry : list) {
				for (Function function : functionArray) {
					function.apply(entry);
				}
			}
		}
		return list;
	}

	public static <T extends BaseModel> T apply(T entity, Function<T, T>... functionArray) {
		if (entity != null) {
			for (Function function : functionArray) {
				function.apply(entity);
			}
		}
		return entity;
	}

	public static Function<BaseModel, BaseModel> sanitizerBase = new Function<BaseModel, BaseModel>() {
		public BaseModel apply(BaseModel entity) {
			if (entity != null) {
				entity.setCreatorId(null);
				entity.setCreator(null);
				entity.setCreated(null);
				entity.setEditorId(null);
				entity.setEditor(null);
				entity.setEdited(null);
				entity.setMapData(null);
			}
			return entity;
		}
	};

}
