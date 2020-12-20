package epsilon.controller;

import epsilon.model.PersonRole;
import orion.annotation.Path;
import orion.view.View;

public class CommonController extends BaseController {

	@Path(value = "/system/common/person/role/list", allow = {}, deny = {})
	public View listPersonRole() {
		return ok(PersonRole.values());
	}

}
