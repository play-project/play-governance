package org.ow2.play.governance.resources;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class ModeHelper {

    public static final String ACL_PREFIX = "http://www.w3.org/ns/auth/acl";

    public static String getFullURI(String mode) {
        if (mode.startsWith(ACL_PREFIX)) {
            return mode;
        }
        return ACL_PREFIX + "#" + mode;
    }
}
