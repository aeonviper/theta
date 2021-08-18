package theta.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import omega.annotation.Transactional;
import omega.service.Decorator;
import theta.core.Utility;
import theta.model.Person;

public class PersonService extends BaseService {

	public static final Decorator<Person> toDecorator = new Decorator<Person>() {
		@Override
		public Person decorate(Person entity) {
			if (entity == null) {
				return entity;
			}
			toDecorate(entity);

			if (entity.getAttachmentList() != null) {
				entity.setAttachmentListData(Utility.gson.toJson(entity.getAttachmentList()));
			}

			return entity;
		}
	};

	public static final Decorator<Person> fromDecorator = new Decorator<Person>() {
		@Override
		public Person decorate(Person entity) {
			if (entity == null) {
				return entity;
			}
			fromDecorate(entity);

			if (Utility.isNotBlank(entity.getAttachmentListData())) {
				entity.setAttachmentList(Utility.gson.fromJson(entity.getAttachmentListData(), Utility.typeListOfString));
				entity.setAttachmentListData(null);
			}

			return entity;
		}
	};

	public static Function<Person, Person> sanitizer = new Function<Person, Person>() {
		public Person apply(Person entity) {
			sanitizerBase.apply(entity);
			return entity;
		}
	};

	public static Function<Person, Person> sanitizerPassword = new Function<Person, Person>() {
		public Person apply(Person entity) {
			if (entity != null) {
				entity.setPassword(null);
			}
			return entity;
		}
	};

	@Transactional
	public int save(Person person) {
		toDecorator.decorate(person);
		if (person.getId() != null) {
			return super.update("person", person, new String[] { "id" }, //
					"mapData", "edited", "editor", "editorId", //
					"name", "email", "password", "active", "role", "attachmentListData");
		} else {
			person.setId(sequence("entitySequence"));
			return super.insert("person", person, "id", //
					"mapData", "created", "creator", "creatorId", //
					"name", "email", "password", "active", "role", "attachmentListData");
		}
	}

	@Transactional
	public int deleteById(Long id) {
		return write("delete from person where id = ?", id);
	}

	@Transactional
	public int editPassword(Person person) {
		return super.update("person", person, new String[] { "id" }, "password");
	}

	@Transactional
	public Long total() {
		return selectFind(Long.class, "select count(1) from person");
	}

	@Transactional
	public Person findById(Long id) {
		return fromDecorator.decorate(find(Person.class, "select * from person where id = ?", id));
	}

	@Transactional
	public Person findByEmail(String email) {
		return fromDecorator.decorate(find(Person.class, "select id, name, email, password, active, role from person where email = ?", email));
	}

	@Transactional
	public List<Person> list(Boolean active) {
		return fromDecorator.decorate(list(Person.class, "select id, mapData, name, email, password, active, role, attachmentListData from person where (? is null or active = ?) order by id", active, active));
	}

	@Transactional
	public List<Person> list() {
		return list(null);
	}

	@Transactional
	public List<Person> list(Integer offset, Integer limit, String sortField, String sortDirection) {
		List<String> fieldList = Arrays.asList("id", "name", "email", "active", "role");
		if (fieldList.contains(sortField) && ("asc".equals(sortDirection) || "desc".equals(sortDirection))) {
		} else {
			sortField = "id";
			sortDirection = "desc";
		}
		return list(Person.class, "select id, mapData, name, email, password, active, role, attachmentListData from person order by " + sortField + " " + sortDirection + " limit ? offset ?", limit, offset);
	}

}
