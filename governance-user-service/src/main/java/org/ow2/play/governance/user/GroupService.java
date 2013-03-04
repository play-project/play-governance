/**
 * 
 */
package org.ow2.play.governance.user;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Group;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author chamerling
 * 
 */
public class GroupService implements
		org.ow2.play.governance.user.api.GroupService {

	private MongoTemplate mongoTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.user.api.GroupService#create(org.ow2.play.governance
	 * .user.api.Group)
	 */
	@Override
	public Group create(Group group) throws UserException {
		if (group == null) {
			throw new UserException("Missing group information");
		}
		org.ow2.play.governance.user.bean.Group saveMe = toGroup(group);
		mongoTemplate.save(saveMe);
		return toGroup(saveMe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.user.api.GroupService#getGroupFromID(java.lang
	 * .String)
	 */
	@Override
	public Group getGroupFromID(String id) throws UserException {
		Query query = query(where("_id").is(id));

		org.ow2.play.governance.user.bean.Group group = mongoTemplate.findOne(
				query, org.ow2.play.governance.user.bean.Group.class);
		return toGroup(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.user.api.GroupService#getGroupFromName(java.lang
	 * .String)
	 */
	@Override
	public Group getGroupFromName(String name) throws UserException {
		org.ow2.play.governance.user.bean.Group group = mongoTemplate.findOne(
				query(where("name").is(name)),
				org.ow2.play.governance.user.bean.Group.class);
		return toGroup(group);
	}

	/**
	 * @param group
	 * @return
	 */
	private Group toGroup(org.ow2.play.governance.user.bean.Group group) {
		Group result = new Group();
		result.description = group.description;
		if (group.id != null)
			result.id = group.id.toStringMongod();
		result.metadata = group.metadata;
		result.name = group.name;
		result.type = group.type;
		return result;
	}

	/**
	 * @param group
	 * @return
	 */
	private org.ow2.play.governance.user.bean.Group toGroup(Group group) {
		org.ow2.play.governance.user.bean.Group result = new org.ow2.play.governance.user.bean.Group();
		result.description = group.description;
		result.metadata = group.metadata;
		result.name = group.name;
		result.type = group.type;
		return result;
	}
	
	/**
	 * @param mongoTemplate the mongoTemplate to set
	 */
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
