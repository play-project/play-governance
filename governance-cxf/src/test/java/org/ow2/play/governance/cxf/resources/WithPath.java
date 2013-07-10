package org.ow2.play.governance.cxf.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
@Path("/foo/bar")
public interface WithPath {

    @Path("/")
    @GET
    Response hello();
}
