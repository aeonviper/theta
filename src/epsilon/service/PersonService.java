package epsilon.service;

import java.util.List;
import java.util.function.Function;

import epsilon.core.Utility;
import epsilon.model.Person;
import omega.annotation.Transactional;
import omega.service.Decorator;

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
	public Person findById(Long id) {
		return fromDecorator.decorate(find(Person.class, "select * from person where id = ?", id));
	}

	@Transactional
	public Person findByEmail(String email) {
		return find(Person.class, "select id, name, email, password, active, role from person where email = ?", email);
	}

	@Transactional
	public List<Person> list() {
		return list(Person.class, "select id, mapData, name, email, password, active, role, attachmentListData from person order by id");
	}

}
