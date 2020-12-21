package theta.model;

import theta.core.Utility;
import theta.security.Principal;

public class BaseModel extends epsilon.model.BaseModel {

	public void createdBy(Principal principal) {
		setCreated(Utility.now());
		setCreatorId(principal.getId());
		setCreator(Utility.gson.toJson(principal.essence()));
	}

	public void editedBy(Principal principal) {
		setEdited(Utility.now());
		setEditorId(principal.getId());
		setEditor(Utility.gson.toJson(principal.essence()));
	}

}
