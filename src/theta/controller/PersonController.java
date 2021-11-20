package theta.controller;

import java.io.File;
import java.util.Iterator;

import javax.inject.Inject;

import epsilon.core.AssetUtility;
import omega.service.TransactionContext;
import omega.service.TransactionService;
import orion.annotation.Parameter;
import orion.annotation.Path;
import orion.controller.Attachment;
import orion.view.View;
import theta.core.Constant;
import theta.core.Utility;
import theta.model.Person;
import theta.service.PersonService;

public class PersonController extends BaseController {

	@Inject
	PersonService personService;

	@Inject
	TransactionService transactionService;

	protected Attachment attachment;
	protected Attachment attachment1;
	protected Attachment attachment2;
	protected Attachment attachment3;
	protected Attachment attachment4;
	protected Attachment attachment5;
	protected Attachment attachment6;

	@Path(value = "/system/person/add", allow = { "Administrator", "Tenant" }, deny = {})
	public View add(@Parameter("person") Person person) {
		if (!( //
		validateRequired("person", person) //
				&& validateRequired("roleSet", person.getRoleSet()) //
				&& validateRequiredString( //
						"name", person.getName(), //
						"email", person.getEmail(), //
						"password", person.getPassword() //

				) //
		)) {
			return badRequestNotification;
		}

		View view = validate(person);
		if (view != null) {
			return view;
		}

//		request.getParameter("query");
//		PrintWriter pw = response.getWriter();
//		pw.println();
//		return new View(Type.TEXT_HTML, "<div>html</div>");

		person.setActive(Utility.isTrue(person.getActive()));
		person.setBirthDate(Utility.parseDate(person.getTransit("birthDate")));
		person.setPassword(Utility.hashPassword(person.getPassword()));
		person.createdBy(principal);

		return transactionService.action(new TransactionContext<View>() {
			public View action() {
				int result = personService.save(person);
				if (result == 1) {

					String fileName;
					String attachmentPath;

					if (attachment != null) {
						fileName = person.getId() + "_" + AssetUtility.encodeAssetName(attachment.getName());
						attachmentPath = Constant.assetPath + File.separator + "file" + File.separator + fileName;
						if (!(attachment.accept(new File(attachmentPath)) && AssetUtility.resizeAsset(fileName))) {
							throw new RuntimeException("Unable to save " + attachment.getName());
						}
						person.setImage(fileName);
					}

					for (Attachment attachment : new Attachment[] { attachment1, attachment2, attachment3, attachment4, attachment5, attachment6 }) {
						if (attachment != null) {
							fileName = "person_attachment_" + person.getId() + "_" + AssetUtility.encodeAssetName(attachment.getName());
							attachmentPath = Constant.assetPath + File.separator + "file" + File.separator + fileName;
							if (!(attachment.accept(new File(attachmentPath)) && AssetUtility.resizeAsset(fileName))) {
								return error("Unable to save " + attachment.getName());
							}
							person.getAttachmentList().add(fileName);
						}
					}
					person.setAttachmentList(Utility.uniquefy(person.getAttachmentList()));

					if (personService.save(person) == 1) {
						return ok(person.getId());
					}
				}
				throw new RuntimeException("Unable to save");
			}
		});
	}

	@Path(value = "/system/person/edit", allow = { "Administrator", "Tenant" }, deny = {})
	public View edit(@Parameter("person") Person person) {
		if (!( //
		validateRequired("person", person) //
				&& validateRequired( //
						"id", person.getId(), //
						"roleSet", person.getRoleSet() //
				) //
				&& validateRequiredString( //
						"name", person.getName(), //
						"email", person.getEmail() //
				) //
		)) {
			return badRequestNotification;
		}

		View view = validate(person);
		if (view != null) {
			return view;
		}

		Person personEntity = personService.findById(person.getId());
		if (personEntity != null) {
			personEntity.setName(person.getName());
			personEntity.setEmail(person.getEmail());
			if (Utility.isNotBlank(person.getPassword())) {
				personEntity.setPassword(Utility.hashPassword(person.getPassword()));
			}
			personEntity.setActive(Utility.isTrue(person.getActive()));
			personEntity.setBirthDate(Utility.parseDate(person.getTransit("birthDate")));
			personEntity.setRoleSet(person.getRoleSet());

			return transactionService.action(new TransactionContext<View>() {
				public View action() {

					if (attachment != null) {
						AssetUtility.deleteAsset(personEntity.getImage());
					}

					String fileName;
					String attachmentPath;

					if (attachment != null) {
						fileName = personEntity.getId() + "_" + AssetUtility.encodeAssetName(attachment.getName());
						attachmentPath = Constant.assetPath + File.separator + "file" + File.separator + fileName;
						if (!(attachment.accept(new File(attachmentPath)) && AssetUtility.resizeAsset(fileName))) {
							throw new RuntimeException("Unable to save " + attachment.getName());
						}
						personEntity.setImage(fileName);
					}

					for (Attachment attachment : new Attachment[] { attachment1, attachment2, attachment3, attachment4, attachment5, attachment6 }) {
						if (attachment != null) {
							fileName = "person_attachment_" + personEntity.getId() + "_" + AssetUtility.encodeAssetName(attachment.getName());
							attachmentPath = Constant.assetPath + File.separator + "file" + File.separator + fileName;
							if (!(attachment.accept(new File(attachmentPath)) && AssetUtility.resizeAsset(fileName))) {
								return error("Unable to save " + attachment.getName());
							}
							personEntity.getAttachmentList().add(fileName);
						}
					}
					personEntity.setAttachmentList(Utility.uniquefy(personEntity.getAttachmentList()));
					personEntity.editedBy(principal);

					if (personService.save(personEntity) == 1) {
						return ok;
					}
					throw new RuntimeException("Unable to save");
				}
			});
		}
		return notFound;
	}

	@Path(value = "/system/person/delete", allow = { "Administrator", "Tenant" }, deny = {})
	public View delete(@Parameter("id") Long id) {
		if (!( //
		validateRequired("id", id) //
		)) {
			return badRequestNotification;
		}
		return personService.deleteById(id) == 1 ? ok : error;
	}

	@Path(value = "/system/person/attachment/delete", allow = { "Administrator", "Tenant" }, deny = {})
	public View deleteAttachment(@Parameter("id") Long id, @Parameter("name") String name) {
		if (!( //
		validateRequired("id", id) //
				&& validateRequiredString("name", name) //
		)) {
			return badRequestNotification;
		}
		Person person = personService.findById(id);
		if (person != null) {
			Iterator<String> iterator = person.getAttachmentList().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().equals(name)) {
					AssetUtility.deleteAsset(name);
					iterator.remove();
					return personService.save(person) == 1 ? ok : error;
				}
			}
		}
		return badRequest;
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

	@Path(value = "/system/person/list/pagination", allow = { "Administrator", "Tenant" }, deny = {})
	public View list(@Parameter("page") Integer page, @Parameter("pageSize") Integer pageSize, @Parameter("sortField") String sortField, @Parameter("sortDirection") String sortDirection) {
		page = Utility.page(page);
		pageSize = Utility.pageSize(pageSize);
		return ok(Utility.makeMap( //
				"list", personService.apply( //
						pageSize == -1 ? personService.list() : personService.list((page - 1) * pageSize, pageSize, sortField, sortDirection), //
						personService.sanitizer, personService.sanitizerPassword), //
				"total", personService.total() //
		));
	}

	View validate(Person person) {
		if (person.getEmail().endsWith("@gmail.com")) {
			return badRequest("GMail account is not allowed");
		}
		return null;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public void setAttachment1(Attachment attachment1) {
		this.attachment1 = attachment1;
	}

	public void setAttachment2(Attachment attachment2) {
		this.attachment2 = attachment2;
	}

	public void setAttachment3(Attachment attachment3) {
		this.attachment3 = attachment3;
	}

	public void setAttachment4(Attachment attachment4) {
		this.attachment4 = attachment4;
	}

	public void setAttachment5(Attachment attachment5) {
		this.attachment5 = attachment5;
	}

	public void setAttachment6(Attachment attachment6) {
		this.attachment6 = attachment6;
	}

}
