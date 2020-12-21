package theta.controller;

import orion.annotation.Path;
import orion.view.View;
import theta.model.PersonRole;

public class CommonController extends BaseController {

	@Path(value = "/system/common/person/role/list", allow = {}, deny = {})
	public View listPersonRole() {
		return ok(PersonRole.values());
	}

}
