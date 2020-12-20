package epsilon.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import epsilon.core.Utility;
import epsilon.model.Product;
import epsilon.model.Shop;
import epsilon.persistence.PersistenceModule;
import omega.annotation.Transactional;
import omega.service.BatchPreparer;
import omega.service.Builder;
import omega.service.Decorator;
import omega.service.ResultData;
import omega.service.Specification;

public class ShopService extends BaseService {

	public static final Decorator<Shop> toDecorator = new Decorator<Shop>() {
		@Override
		public Shop decorate(Shop entity) {
			if (entity == null) {
				return entity;
			}
			toDecorate(entity);
			return entity;
		}
	};

	public static final Decorator<Shop> fromDecorator = new Decorator<Shop>() {
		@Override
		public Shop decorate(Shop entity) {
			if (entity == null) {
				return entity;
			}
			fromDecorate(entity);
			return entity;
		}
	};

	public static Function<Shop, Shop> sanitizer = new Function<Shop, Shop>() {
		public Shop apply(Shop entity) {
			sanitizerBase.apply(entity);
			return entity;
		}
	};

	@Transactional
	public int save(Shop shop) {
		toDecorator.decorate(shop);
		if (shop.getId() != null) {
			return super.update("shop", shop, new String[] { "id" }, "slug", "name");
		} else {
			// using auto increment
			return super.create("shop", shop, "id", "slug", "name");
		}
	}

	@Transactional
	public List<Shop> list() {
		return list(Shop.class, "select * from shop order by name");
	}

	@Transactional
	public Shop find(Long id) {
		return find(Shop.class, "select * from shop where id = ?", id);
	}

	// custom sql
	@Transactional
	public Shop findAgain(Long id) {
		return find(Shop.class, "select * from shop where id in (" + id + ") or id + 1 - 1 = ?", id);
	}

	// map any column to map and list
	@Transactional
	public List<Shop> listAgain() {
		return list(Shop.class, "select id, name, slug as `map.whatever`, name as `additionalProductList[2].name`, name as `map.level1.level2.level3.level4.level5.name` from shop order by name");
	}

	@Transactional
	public List<Shop> listJoin() {
		return list(Shop.class, "select shop.id, product.slug as name from shop join product on product.shopId = shop.id order by shop.name");
	}

	// return as map
	@Transactional
	public List<HashMap> listJoinMap() {
		return list(HashMap.class, "select shop.id, product.slug as name from shop join product on product.shopId = shop.id order by shop.name");
	}

	// return as object array
	@Transactional
	public List<Object[]> listMultiple() {
		List<Object[]> list = list(Builder.build(PersistenceModule.classTypeMapping, null, new Specification[] { //
				new Specification("first", Shop.class), //
				new Specification("second", Product.class) //
		}), "select " + //
				"shop.id as firstId, shop.name as firstName, shop.slug as firstSlug, " + //
				"product.id as secondId, product.name as secondName, product.slug as secondSlug, product.quantity as secondQuantity " + //
				"from shop join product on product.shopId = shop.id order by shop.name");
		return list;
	}

	// build yourself
	@Transactional
	public List<Shop> listBuild(Long presentationId) {
		return apply(list(new Builder<Shop>() {
			public Shop build(ResultData rd) throws SQLException {
				Shop shop = new Shop();
				shop.setId(rd.getLong("shop.id"));
				shop.setSlug(rd.getString("shop.slug"));
				shop.setName(rd.getString("shop.name"));
				shop.putTransit("whateverName", rd.getString("product.name"));
				shop.putTransit("whateverSlug", rd.getString("product.Slug"));
				shop.putTransit("whateverQuantity", rd.getString("product.Quantity"));
				return shop;
			}
		}, "select shop.id, product.slug as name from shop join product on product.shopId = shop.id order by shop.name", //
				presentationId), sanitizer);
	}

	@Transactional
	public void batchInsert(List<Shop> entityList) {
		String sql = //
				"insert into shop " + //
						"(slug, name) " + //
						"values " + //
						"(?, ?)";
		batch(50, sql, toDecorator, new BatchPreparer<Shop>() {
			public Object[] toFieldArray(Shop entity) {
				return new Object[] { //
						entity.getSlug(), //
						entity.getName() };
			}
		}, entityList);
	}

}
