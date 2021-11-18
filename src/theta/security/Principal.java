package theta.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import theta.model.PersonRole;

public class Principal {

	private Long id;
	private Long tenantId;
	private String name;
	private String email;
	private Set<PersonRole> roleSet = new HashSet<>();
	private String token;
	private Map<String, Object> map = new HashMap<>();

	public static final Principal System = new Principal(null, null, null, null);

	public Principal(Long id, String email, String name, Set<PersonRole> roleSet) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.roleSet = roleSet;
	}

	public Principal essence() {
		Principal principal = new Principal(this.id, this.email, this.name, this.roleSet);
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

	public Set<PersonRole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<PersonRole> roleSet) {
		this.roleSet = roleSet;
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
