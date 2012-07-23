/**
 * 
 */
package eu.playproject.governance.service;

import java.util.concurrent.TimeUnit;

import org.petalslink.dsb.commons.service.api.Service;
import org.petalslink.dsb.cxf.CXFHelper;

import eu.playproject.governance.api.EventGovernance;

/**
 * @author chamerling
 * 
 */
public class CXFServer {

	public static void main(String[] args) {
		String localhost = "http://localhost:8887/play/governance/";

		Service eventService = CXFHelper.getService(localhost,
				EventGovernance.class,
				new eu.playproject.governance.service.EventGovernanceService());

		eventService.start();

		try {
			TimeUnit.DAYS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
