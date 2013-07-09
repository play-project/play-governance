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

    @Test
    public void testGetReadMode() {
        Assert.assertEquals("http://www.w3.org/ns/auth/acl#Read", ModeHelper.getFullURI("read"));
        Assert.assertEquals("http://www.w3.org/ns/auth/acl#Read", ModeHelper.getFullURI("Read"));
    }

    @Test
    public void testGetWriteMode() {
        Assert.assertEquals("http://www.w3.org/ns/auth/acl#Write", ModeHelper.getFullURI("write"));
        Assert.assertEquals("http://www.w3.org/ns/auth/acl#Write", ModeHelper.getFullURI("Write"));
    }

    @Test
    public void testGetSubscribeMode() {
        Assert.assertEquals("http://docs.oasis-open.org/wsn/b-2/Subscribe", ModeHelper.getFullURI("subscribe"));
        Assert.assertEquals("http://docs.oasis-open.org/wsn/b-2/Subscribe", ModeHelper.getFullURI("Subscribe"));
    }

    @Test
    public void testNotifyMode() {
        Assert.assertEquals("http://docs.oasis-open.org/wsn/b-2/Notify", ModeHelper.getFullURI("notify"));
        Assert.assertEquals("http://docs.oasis-open.org/wsn/b-2/Notify", ModeHelper.getFullURI("Notify"));
    }
}
