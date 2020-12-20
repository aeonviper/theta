package epsilon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends BaseModel {

	protected String slug;
	protected String name;

	// transient
	protected List<Product> additionalProductList = new ArrayList<>();

	public Shop() {
		additionalProductList.add(new Product());
		additionalProductList.add(new Product());
		additionalProductList.add(new Product());

		Map<String, String> level5 = new HashMap<>();
		Map<String, Map> level4 = new HashMap<>();
		Map<String, Map> level3 = new HashMap<>();
		Map<String, Map> level2 = new HashMap<>();
		Map<String, Map> level1 = new HashMap<>();

		level4.put("level5", level5);
		level3.put("level4", level4);
		level2.put("level3", level3);
		level1.put("level2", level2);
		map.put("level1", level1);
	}

	public Shop(Long id, String slug, String name) {
		this.id = id;
		this.slug = slug;
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getAdditionalProductList() {
		return additionalProductList;
	}

	public void setAdditionalProductList(List<Product> additionalProductList) {
		this.additionalProductList = additionalProductList;
	}

}
