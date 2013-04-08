/**
 * 
 */
package org.ow2.play.governance.user.api.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User bean
 * 
 * @author chamerling
 * 
 */
@XmlRootElement(name = "user")
public class User {
	
	public String id;

	/**
	 * The user id in the Play platform
	 */
	public String login;

	public String fullName;

	/**
	 * If any...
	 */
	public String password;

	public String email;

	public String avatarURL;
	
	/**
	 * The API token while waiting for a OAuth provider
	 */
	public String apiToken;
	
	/**
	 * Providers accounts for the user
	 */
	public List<Account> accounts = new ArrayList<Account>();

	/**
	 * List of groupIds the user belongs to. Groups are special resources.
	 * Resource date is the date user joined the group.
	 */
	public List<Resource> groups = new ArrayList<Resource>();
	
	/**
	 * Resources created by the user
	 */
	public List<Resource> resources = new ArrayList<Resource>();

	public User() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", fullName=" + fullName
				+ ", password=" + password + ", email=" + email
				+ ", avatarURL=" + avatarURL + ", apiToken=" + apiToken
				+ ", accounts=" + accounts + ", groups=" + groups
				+ ", resources=" + resources + "]";
	}

}
