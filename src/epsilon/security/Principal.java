package epsilon.security;

import java.util.HashMap;
import java.util.Map;

import epsilon.model.PersonRole;

public class Principal {

	private Long id;
	private Long tenantId;
	private String name;
	private String email;
	private PersonRole role;
	private String token;
	private Map<String, Object> map = new HashMap<>();

	public static final Principal System = new Principal(null, null, null, null);

	public Principal(Long id, String email, String name, PersonRole role) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.role = role;
	}

	public Principal essence() {
		Principal principal = new Principal(this.id, this.email, this.name, this.role);
		principal.setMap(null);
		return principal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

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

	public PersonRole getRole() {
		return role;
	}

	public void setRole(PersonRole role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
