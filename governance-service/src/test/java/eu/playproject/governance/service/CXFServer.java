/**
 * 
 */
package eu.playproject.governance.service;

import java.util.concurrent.TimeUnit;

import org.petalslink.dsb.commons.service.api.Service;
import org.petalslink.dsb.cxf.CXFHelper;

import eu.playproject.governance.api.EventGovernance;
import eu.playproject.governance.api.TopicMetadataService;

/**
 * @author chamerling
 * 
 */
public class CXFServer {

	public static void main(String[] args) {
		String localhost = "http://localhost:8887/play/governance/";
		Service topicMetadata = CXFHelper.getService(localhost,
				TopicMetadataService.class,
				new eu.playproject.governance.service.TopicMetadataService());

		Service eventService = CXFHelper.getService(localhost,
				EventGovernance.class,
				new eu.playproject.governance.service.EventGovernanceService());

		topicMetadata.start();
		eventService.start();

		try {
			TimeUnit.DAYS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
