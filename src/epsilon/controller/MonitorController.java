package epsilon.controller;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import epsilon.model.Tenant;
import epsilon.service.TenantService;
import orion.annotation.Parameter;
import orion.annotation.Path;
import orion.view.View;
import orion.view.View.Type;

public class MonitorController extends BaseController {

	@Inject
	TenantService tenantService;

	@Path(value = "/system/monitor/ping", allow = {}, deny = {})
	public View ping() {
		List<Tenant> tenantList = tenantService.list();
		if (!tenantList.isEmpty()) {
			return new View(Type.TEXT_PLAIN, "OK");
		}
		return new View(Type.TEXT_PLAIN, "Error");
	}

	@Path(value = "/system/monitor/storage", allow = {}, deny = {})
	public View storage( //
			@Parameter("token") String token, //
			@Parameter("action") String action, //
			@Parameter("drive") String drive, //
			@Parameter("thresholdSize") Long thresholdSize, //
			@Parameter("thresholdPercentage") Integer thresholdPercentage //
	) {
		if ("epsilon".equals(token)) {
			if ("list".equals(action)) {
				StringBuilder sb = new StringBuilder();
				for (File file : File.listRoots()) {
					sb.append(file.toString() + " - drive: " + hex(file.toString()) + " total:" + file.getTotalSpace() + " usable:" + file.getUsableSpace() + " free:" + file.getFreeSpace() + "\n");
				}
				return new View(Type.TEXT_PLAIN, sb.toString());
			} else if ("check".equals(action)) {
				if (thresholdPercentage == null || thresholdPercentage < 0) {
					thresholdPercentage = 20;
				}
				for (File file : File.listRoots()) {
					if (hex(file.toString()).equals(drive)) {
						if (thresholdSize != null) {
							long value = file.getFreeSpace();
							if (value < thresholdSize) {
								return new View(Type.TEXT_PLAIN, "Error: threshold reached - " + file.toString() + " - total:" + file.getTotalSpace() + " usable:" + file.getUsableSpace() + " free:" + file.getFreeSpace() + " threshold:" + thresholdSize + " value:" + value);
							} else {
								return new View(Type.TEXT_PLAIN, "OK - " + file.toString() + " - total:" + file.getTotalSpace() + " usable:" + file.getUsableSpace() + " free:" + file.getFreeSpace() + " threshold:" + thresholdSize + " value:" + value);
							}
						}
						if (thresholdPercentage != null) {
							int value = (int) Math.floor((double) file.getFreeSpace() * 100 / file.getTotalSpace());
							if (value < thresholdPercentage) {
								return new View(Type.TEXT_PLAIN, "Error: threshold reached - " + file.toString() + " - total:" + file.getTotalSpace() + " usable:" + file.getUsableSpace() + " free:" + file.getFreeSpace() + " threshold:" + thresholdPercentage + " value:" + value);
							} else {
								return new View(Type.TEXT_PLAIN, "OK - " + file.toString() + " - total:" + file.getTotalSpace() + " usable:" + file.getUsableSpace() + " free:" + file.getFreeSpace() + " threshold:" + thresholdPercentage + " value:" + value);
							}
						}
						return new View(Type.TEXT_PLAIN, "Error: no threshold to check");
					}
				}
				return new View(Type.TEXT_PLAIN, "Error: could not find drive");
			} else {
				return new View(Type.TEXT_PLAIN, "Error: unknown action");
			}
		} else {
			return new View(Type.TEXT_PLAIN, "Error: wrong token");
		}
	}

	protected String hex(String text) {
		return String.format("%x", new BigInteger(1, text.getBytes()));
	}

}
