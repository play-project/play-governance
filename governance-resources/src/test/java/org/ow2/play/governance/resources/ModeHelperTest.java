package org.ow2.play.governance.resources;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class ModeHelperTest {

    @Test
    public void testModeURIFromSuffix() {
        String mode = "foo";
        Assert.assertEquals("http://www.w3.org/ns/auth/acl#foo", ModeHelper.getFullURI(mode));
    }

    @Test
    public void testModeURIFromFullURI() {
        String mode = "http://www.w3.org/ns/auth/acl#bar";
        Assert.assertEquals(mode, ModeHelper.getFullURI(mode));
    }
}
