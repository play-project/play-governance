package org.ow2.play.governance.resources;

import java.util.Map;

import org.ow2.play.governance.permission.api.Constants;

import com.google.common.collect.Maps;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class ModeHelper {

    public static final String ACL_PREFIX = Constants.ACL_PREFIX;
    public static final String NOTIFY = Constants.NOTIFY;
    public static final String SUBSCRIBE = Constants.SUBSCRIBE;
    public static final String READ = Constants.READ;
    public static final String WRITE = Constants.WRITE;

    public static Map<String, String> modes = Maps.newHashMap();

    static {
        modes.put("notify", NOTIFY);
        modes.put("subscribe", SUBSCRIBE);
        modes.put("subcribe", SUBSCRIBE); // compensate for typos elsewhere
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
        if (modes.containsValue(mode)) {
            return mode;
        }

        if (mode.startsWith(ACL_PREFIX)) {
            return mode;
        }
        
        // lookup in the cache if we can find a mode (in lowercase)
        if (null != modes.get(mode.toLowerCase())) {
            return modes.get(mode.toLowerCase());
        }

        // not found in cache, build an ad-hoc URI and send it back
        return ACL_PREFIX + "#" + mode;
    }
}
