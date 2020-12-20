package epsilon.controller;

import javax.inject.Inject;

import epsilon.core.Utility;
import epsilon.model.Person;
import epsilon.service.PersonService;
import orion.annotation.Parameter;
import orion.annotation.Path;
import orion.validation.field.RequiredField;
import orion.validation.field.RequiredStringField;
import orion.view.View;

public class PersonController extends BaseController {

	@Inject
	PersonService personService;

	@Path(value = "/system/person/add", allow = { "Administrator", "Tenant" }, deny = {})
	public View add(@Parameter("person") Person person) {
		if (!( //
		validateRequired(new RequiredField("person", person)) //
				&& validateRequired(new RequiredField("role", person.getRole())) //
				&& validateRequiredString( //
						new RequiredStringField("name", person.getName()), //
						new RequiredStringField("email", person.getEmail()), //
						new RequiredStringField("password", person.getPassword()) //

				) //
		)) {
			return badRequestNotification;
		}

		person.setActive(Utility.isTrue(person.getActive()));
		person.setPassword(Utility.hashPassword(person.getPassword()));
		person.createdBy(principal);
		return personService.save(person) == 1 ? ok : error;
	}

	@Path(value = "/system/person/edit", allow = { "Administrator", "Tenant" }, deny = {})
	public View edit(@Parameter("person") Person person) {
		if (!( //
		validateRequired(new RequiredField("person", person)) //
				&& validateRequired( //
						new RequiredField("id", person.getId()), //
						new RequiredField("role", person.getRole()) //
				) //
				&& validateRequiredString( //
						new RequiredStringField("name", person.getName()), //
						new RequiredStringField("email", person.getEmail()) //
				) //
		)) {
			return badRequestNotification;
		}

		Person personEntity = personService.findById(person.getId());
		if (personEntity != null) {
			personEntity.setName(person.getName());
			personEntity.setEmail(person.getEmail());
			if (Utility.isNotBlank(person.getPassword())) {
				personEntity.setPassword(Utility.hashPassword(person.getPassword()));
			}
			personEntity.setActive(Utility.isTrue(person.getActive()));
			personEntity.setRole(person.getRole());
			personEntity.editedBy(principal);
			return personService.save(personEntity) == 1 ? ok : error;
		}
		return notFound;
	}

	@Path(value = "/system/person/delete", allow = { "Administrator", "Tenant" }, deny = {})
	public View delete(@Parameter("id") Long id) {
		if (!( //
		validateRequired(new RequiredField("id", id)) //
		)) {
			return badRequestNotification;
		}
		return personService.deleteById(id) == 1 ? ok : error;
	}

	@Path(value = "/system/person", allow = { "Administrator", "Tenant" }, deny = {})
	public View find(@Parameter("id") Long id) {
		// validate or no validate?
		Person person = personService.apply(personService.findById(id), personService.sanitizer, personService.sanitizerPassword);
		return person != null ? ok(person) : notFound;
	}

	@Path(value = "/system/person/list", allow = { "Administrator", "Tenant" }, deny = {})
	public View list() {
		return ok(personService.apply(personService.list(), personService.sanitizer, personService.sanitizerPassword));
	}

}
