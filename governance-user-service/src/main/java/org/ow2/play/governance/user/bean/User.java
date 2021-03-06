/**
 * 
 */
package org.ow2.play.governance.user.bean;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.ow2.play.governance.user.api.bean.Account;
import org.ow2.play.governance.user.api.bean.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User bean
 * 
 * @author chamerling
 * 
 */
@Document
public class User {

	@Id
	public ObjectId id;

	public String login;

	public String fullName;
	
	public String password;

	public String email;

	public String avatarURL;
	
	public String apitoken;
	
	public String date;

	public List<Account> accounts = new ArrayList<Account>();

	public List<Resource> groups = new ArrayList<Resource>();
	
	/**
	 * Resources created by the user
	 */
	public List<Resource> resources = new ArrayList<Resource>();

	public User() {

	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", email=" + email
				+ ", accounts=" + accounts + ", groups=" + groups + ", resources=" + resources + "]";
	}
}
