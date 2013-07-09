package org.ow2.play.governance.resources;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class ModeHelper {

    public static final String ACL_PREFIX = "http://www.w3.org/ns/auth/acl";
    public static final String NOTIFY = "http://docs.oasis-open.org/wsn/b-2/Notify";
    public static final String SUBSCRIBE = "http://docs.oasis-open.org/wsn/b-2/Subscribe";
    public static final String READ = "http://www.w3.org/ns/auth/acl#Read";
    public static final String WRITE = "http://www.w3.org/ns/auth/acl#Write";

    public static Map<String, String> modes = Maps.newHashMap();

    static {
        modes.put("notify", NOTIFY);
        modes.put("subscribe", SUBSCRIBE);
        modes.put("read", READ);
        modes.put("write", WRITE);
    }

    /**
     * Return a full URI for the mode based on the modes cache.
     *
     * @param mode the mode to get URI from
     *
     * @return
     */
    public static String getFullURI(String mode) {
        if (mode.startsWith(ACL_PREFIX)) {
            return mode;
        }

        // lookup in the cache if we can find a mode (in lowercase)
        if (null != modes.get(mode.toLowerCase())) {
            return modes.get(mode.toLowerCase());
        }

        // not found in cache, build an URI and send it back
        return ACL_PREFIX + "#" + mode;
    }
}
