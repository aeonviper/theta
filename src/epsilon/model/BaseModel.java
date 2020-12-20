package epsilon.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import epsilon.core.Utility;
import epsilon.security.Principal;

public class BaseModel {

	protected Long id;

	protected Map<String, Object> map = new HashMap<>();
	protected String mapData;

	protected LocalDateTime created;
	protected Long creatorId;
	protected String creator;

	protected LocalDateTime edited;
	protected Long editorId;
	protected String editor;

	protected Map<String, Object> transitMap = new HashMap<>();

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

	public Object getTransit(String key) {
		if (transitMap != null) {
			return transitMap.get(key);
		}
		return null;
	}

	public Object putTransit(String key, Object value) {
		if (transitMap != null) {
			return transitMap.put(key, value);
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getMapData() {
		return mapData;
	}

	public void setMapData(String mapData) {
		this.mapData = mapData;
	}

	public Map<String, Object> getTransitMap() {
		return transitMap;
	}

	public void setTransitMap(Map<String, Object> transitMap) {
		this.transitMap = transitMap;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public LocalDateTime getEdited() {
		return edited;
	}

	public void setEdited(LocalDateTime edited) {
		this.edited = edited;
	}

	public Long getEditorId() {
		return editorId;
	}

	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

}
