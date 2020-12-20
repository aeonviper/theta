package epsilon.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Utility;
import epsilon.model.Product;
import epsilon.model.Shop;
import epsilon.service.ShopService;
import omega.service.TransactionContext;
import omega.service.TransactionService;
import orion.annotation.Parameter;
import orion.annotation.Path;
import orion.annotation.PathSet;
import orion.validation.field.LongRangeField;
import orion.view.View;

@Singleton
public class ShopController extends BaseController {

	@Inject
	ShopService shopService;

	@Inject
	TransactionService transactionService;

	@PathSet({ //
			@Path(value = "/system/common/shop/([0-9]+)", name = "entityId", allow = {}, deny = {}), //
			@Path(value = "/system/common/(store|shop)/find/([0-9]+)", name = "qualifier,entityId", method = "POST", allow = {}, deny = {}) //
	})
	public View find( //
			@Parameter("entityId") Long entityId, //
			@Parameter("qualifier") String qualifier, //
			@Parameter("genericMap") Map genericMap, //
			@Parameter("shop") Shop shop //
	) {
		if (!validateLongRange(new LongRangeField("whatever", entityId).setMin(0L).setMax(100L))) {
			return badRequestNotification;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (entityId.equals(777L)) {
			return forbidden("777 = entityId");
		}

		System.out.println("> thread: " + Thread.currentThread() + " " + this);

		System.out.println("> genericMap:\n" + gson.toJson(genericMap));

		System.out.println("> shop:\n" + gson.toJson(shop));

		List<Shop> shopList;

		System.out.println("> list:\n" + gson.toJson(shopService.list()));
		System.out.println("> listAgain:\n" + gson.toJson(shopService.listAgain()));
		System.out.println("> listJoin:\n" + gson.toJson(shopService.listJoin()));

		List<HashMap> mapList = shopService.listJoinMap();
		System.out.println("> listJoinMap:\n" + gson.toJson(mapList));

		List<Object[]> multipleList = shopService.listMultiple();
		System.out.println("> listMultiple:\n" + gson.toJson(multipleList));

		for (Object[] array : multipleList) {
			Shop s = (Shop) array[0];
			Product p = (Product) array[1];
			System.out.println(s.getId() + " " + s.getName() + " -> " + p.getId() + " " + p.getName());
		}

		Map<String, Shop> shopMap = new HashMap<>();
		shopMap.put("store1", new Shop(1L, "shop1", "Shop 1"));
		shopMap.put("store2", new Shop(2L, "shop2", "Shop 2"));

		return ok(Utility.makeMap("store3", new Shop(3L, "shop3", "Shop 3"), "shop", shop, "genericMap", genericMap));
	}

	@Path(value = "/system/common/shop/bulk/add", allow = {}, deny = {}) //
	public View bulkAdd(@Parameter("limit") Integer limit) {
		limit = Utility.coalesce(limit, 1000);
		long start = System.currentTimeMillis();
		for (int i = 0; i < limit; i++) {
			Shop shop = new Shop(null, "store-" + i, "Store-" + i);
			shopService.save(shop);
		}
		long finish = System.currentTimeMillis();
		System.out.println(finish - start + "ms");
		return ok(finish - start);
	}

	@Path(value = "/system/common/shop/bulk/add/transaction", allow = {}, deny = {}) //
	public View bulkAddTransaction(@Parameter("limit") Integer limit) {
		limit = Utility.coalesce(limit, 1000);
		long start = System.currentTimeMillis();
		Integer result = transactionService.action(new TransactionContext<Integer>() {
			public Integer action(Object... array) {
				Integer limit = (Integer) array[0];
				for (int i = 0; i < limit; i++) {
					Shop shop = new Shop(null, "store-" + i, "Store-" + i);
					shopService.save(shop);
				}
				return 0;
			}
		}, limit);
		long finish = System.currentTimeMillis();
		System.out.println(finish - start + "ms");
		return ok(finish - start);
	}

	@Path(value = "/system/common/shop/bulk/add/batch", allow = {}, deny = {}) //
	public View bulkAddBatch(@Parameter("limit") Integer limit) {
		limit = Utility.coalesce(limit, 1000);
		long start = System.currentTimeMillis();
		List<Shop> shopList = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			Shop shop = new Shop(null, "store-" + i, "Store-" + i);
			shopList.add(shop);
		}
		shopService.batchInsert(shopList);
		long finish = System.currentTimeMillis();
		System.out.println(finish - start + "ms");
		return ok(finish - start);
	}

}
