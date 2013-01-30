/**
 * 
 */
package org.ow2.play.governance.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.ow2.play.governance.user.api.Account;
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
	private ObjectId id;

	public String login;

	public String password;

	public String email;

	public String avatarURL;

	public List<Account> accounts = new ArrayList<Account>();

	public List<String> groups = new ArrayList<String>();

	public User() {

	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", email=" + email
				+ ", accounts=" + accounts + ", groups=" + groups + "]";
	}
}