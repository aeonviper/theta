package epsilon.service;

import java.util.List;

import epsilon.model.Tenant;
import omega.annotation.Transactional;

public class TenantService extends BaseService {

	@Transactional
	public List<Tenant> list() {
		return list(Tenant.class, "select * from tenant order by name");
	}

	@Transactional
	public Tenant findById(Long id) {
		return find(Tenant.class, "select * from tenant where id = ?", id);
	}

}
