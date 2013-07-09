/**
 *
 * Copyright (c) 2013, Linagora
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.governance.platform.user.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Collections2;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.permission.api.Constants;
import org.ow2.play.governance.permission.api.PermissionChecker;
import org.ow2.play.governance.permission.api.PermissionService;
import org.ow2.play.governance.resources.ResourceHelper;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.UserService;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Type;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Permission check implementation which uses DB queries.
 * 
 * @author chamerling
 * 
 */
public class PermissionCheck implements PermissionChecker {

	private UserService userService;

	private PermissionService permissionService;

	@Override
	public boolean checkGroup(String user, final String group) {
		User u = null;
		try {
			u = userService.getUser(user);
		} catch (UserException e) {
			// logger
			e.printStackTrace();
		}

		return u != null && u.groups != null
				&& Iterables.tryFind(u.groups, new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return input.uri != null && input.name != null
								&& group.equals(input.uri + "#" + input.name);
					}
				}).isPresent();
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.governance.platform.user.service.PermissionChecker#checkResource(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkResource(final String user, final String resource) {

		Set<String> groups = getGroupsForResource(resource);
		Optional<String> ok = Iterables.tryFind(groups,
				new Predicate<String>() {
					public boolean apply(String group) {
						return checkGroup(user, group);
					};
				});
		return ok.isPresent();
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.governance.platform.user.service.PermissionChecker#checkRole(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkRole(String user, String resource, String role) {
		// return checkResource(user, resource) && roleIsOK(role);
		return true;
	}

    /**
     * Get all the user resources and check if the input one is available with the given mode.
     *
     * @param user
     * @param resource resource URI
     * @param mode mode URI
     * @return
     */
    @Override
    public boolean checkMode(String user, String resource, String mode) {
        // TODO : we should be able to do it in one mongodb query

        // load the user just one time instead of calling checkGroup N times...
        User u = null;
        try {
            u = userService.getUser(user);
        } catch (UserException e) {
            // logger
            e.printStackTrace();
        }

        return Sets.intersection(getGroupsForResourceInMode(resource, mode), Sets.newHashSet(Collections2.transform(u.groups, new Function<Resource, String>() {
            @Override
            public String apply(Resource input) {
                System.out.println(input);
                return input.uri + "#" + input.name;
            }
        }))).size() > 0;

    }

    /* (non-Javadoc)
         * @see org.ow2.play.governance.platform.user.service.PermissionChecker#getGroupsForResource(java.lang.String)
         */
	@Override
	public Set<String> getGroupsForResource(String resource) {
		Set<String> result = Sets.newHashSet();
		List<MetaResource> permissions = null;
		try {
			permissions = permissionService.getWhereAccessTo(resource);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return result;
		}

		if (permissions == null) {
			return result;
		}

		for (MetaResource metaResource : permissions) {
			for (Metadata metadata : metaResource.getMetadata()) {
				if (metadata.getName().equals(
						org.ow2.play.governance.permission.api.Constants.AGENT)) {
					List<String> groups = Lists.transform(metadata.getData(),
							new Function<Data, String>() {
								public String apply(Data data) {
									if (data.getType().equals(Type.URI)
											&& data.getValue().endsWith(
													"#group")) {
										return data.getValue();
									}
									// FIXME Check in guava if there is a better
									// way to manage that...
									return "";
								};
							});
					// add all the groups which are not empty values
					for (String string : groups) {
						if (string != null && !string.equals("")) {
							result.add(string);
						}
					}
				} else {
					// TODO LOGGER System.out.println("Not an agent..." +
					// metadata.getName());
				}
			}
		}
		return result;
	}

    /**
     * Get all the groups where the resource is available in a given mode
     *
     * @param resource
     * @param mode
     * @return
     */
    public Set<String> getGroupsForResourceInMode(final String resource, final String mode) {
        if (resource == null || mode == null) {
            return Sets.newHashSet();
        }

        Set<String> result = Sets.newHashSet();

        // get all the permissions where the access is defined for this resource
        // ie the permission is defined for the given resource
        List<MetaResource> permissions = null;
        try {
            permissions = permissionService.getWhereAccessTo(resource);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            return result;
        }

        if (permissions == null) {
            return result;
        }

        // get all the permissions where the mode is set like the input one
        Iterable<MetaResource> permissionsWhereModeIsOK = Iterables.filter(permissions, new Predicate<MetaResource>() {
            @Override
            public boolean apply(MetaResource input) {

                // get the mode metadata
                Metadata md = Iterables.tryFind(input.getMetadata(), new Predicate<Metadata>() {
                    @Override
                    public boolean apply(org.ow2.play.metadata.api.Metadata input) {
                        return input.getName().equals(Constants.MODE);
                    }
                }).orNull();

                if (md == null) {
                    return false;
                }

                // found a mode metadata, let's check the mode value is the input one
                Data found = Iterables.tryFind(md.getData(), new Predicate<Data>() {
                    @Override
                    public boolean apply(Data input) {
                        return Type.URI.equals(input.getType()) && mode.equals(input.getValue());
                    }
                }).orNull();

                if (found != null) {
                    return true;
                }
                return false;
            }
        });

        // we now have all the permissions where the resource and mode are like the input one:
        // extract the groups from them (ie agent metadata)
        Iterable<Set<String>> groupss = Iterables.transform(permissionsWhereModeIsOK, new Function<MetaResource, Set<String>>() {
            public Set<String> apply(MetaResource input) {

                Set<String> result = Sets.newHashSet();

                // first time we extract all permissions where the agent is set
                Metadata md = Iterables.tryFind(input.getMetadata(), new Predicate<Metadata>() {
                    @Override
                    public boolean apply(org.ow2.play.metadata.api.Metadata input) {
                        return input.getName().equals(Constants.AGENT);
                    }
                }).orNull();

                if (md == null) {
                    return result;
                }

                Iterable<Data> all = Iterables.filter(md.getData(), new Predicate<Data>() {
                    @Override
                    public boolean apply(org.ow2.play.metadata.api.Data input) {
                        return input != null && input.getValue() != null && input.getValue().endsWith("#group");
                    }
                });

                result = Sets.newHashSet(Iterables.transform(all, new Function<Data, String>() {
                    @Override
                    public String apply(org.ow2.play.metadata.api.Data input) {
                        return input.getValue();
                    }
                }));

                return result;
            };
        });

        // change the set of set<string> into set<string> (eliminate duplicates)
        Iterator<Set<String>> iter = groupss.iterator();
        while(iter.hasNext()) {
            Iterables.addAll(result, iter.next());
        }
        return result;
    }

    /**
     *
     * @param permissionService
     */
    public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}