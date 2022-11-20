package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person extends BaseModel {

	protected String name;
	protected String email;
	protected String password;
	protected Boolean active;

	protected LocalDate birthDate;
	protected Set<PersonRole> roleSet = new HashSet<>();
	protected String roleSetData;

	protected String image;
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

	public Set<PersonRole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<PersonRole> roleSet) {
		this.roleSet = roleSet;
	}

	public String getRoleSetData() {
		return roleSetData;
	}

	public void setRoleSetData(String roleSetData) {
		this.roleSetData = roleSetData;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

}
