/**
 * 
 */
package org.ow2.play.governance.api;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author chamerling
 * 
 */
@WebService
public interface GovernanceManagement {

	/**
	 * Starts the governance : Init all that is required
	 * 
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void start() throws GovernanceExeption;

	/**
	 * Stops the governance : TBD
	 * 
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void stop() throws GovernanceExeption;

}
