package epsilon.model;

import java.util.ArrayList;
import java.util.List;

public class Person extends BaseModel {

	protected String name;
	protected String email;
	protected String password;
	protected Boolean active;
	protected PersonRole role;

	protected List<String> attachmentList = new ArrayList<>();
	protected String attachmentListData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public PersonRole getRole() {
		return role;
	}

	public void setRole(PersonRole role) {
		this.role = role;
	}

	public List<String> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<String> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getAttachmentListData() {
		return attachmentListData;
	}

	public void setAttachmentListData(String attachmentListData) {
		this.attachmentListData = attachmentListData;
	}

}
