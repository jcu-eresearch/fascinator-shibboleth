/*
 * The Fascinator - Shibboleth SSO Plugin
 * Copyright (C) 2012 James Cook University
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package au.edu.jcu.fascinator.portal.sso;

import au.edu.jcu.fascinator.portal.sso.shibboleth.Shibboleth;
import com.googlecode.fascinator.api.authentication.User;
import com.googlecode.fascinator.common.FascinatorHome;
import com.googlecode.fascinator.common.authentication.GenericUser;
import com.googlecode.fascinator.portal.JsonSessionState;
import org.apache.velocity.app.Velocity;
import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Fascinator Shibboleth Integration
 *
 * @author Nigel Bajema
 */
public class SSOTest {
    private Shibboleth s;
    private JsonSessionState session;

    @Before
    public void setup() throws Exception {
        File cwd = new File(System.getProperty("user.dir"));
        System.setProperty(FascinatorHome.SYSTEM_KEY,new File(cwd, "src/test/resources").getAbsolutePath());
        Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, new File(cwd, "src/main/config/portal").getAbsolutePath());
        s = new Shibboleth();
        MocHTTPRequest req = new MocHTTPRequest();

        req.setAttribute("unscoped-affiliation", "staff");
        req.setAttribute("Shib-Session-ID", "_srd34f5f5tg3wd5yt6hfddsgj7htgtfg");
        req.setAttribute("auEduPersonSharedToken", "ddsdsf678hgH878786G67F7Fg");
        req.setAttribute("Shib-Authentication-Instant", "2012-09-21T01:44:47.771Z");
        req.setAttribute("RequestURI", "default/sso/shibboleth");
        req.setAttribute("Shib-AuthnContext-Class", "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
        req.setAttribute("Shib-Session-Index", "1fb5ad842649a09e3f6c18c3ff28fa96ff9022af82fb0b06fbd6fe671effa7b7");
        req.setAttribute("Shib-Authentication-Method", "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
        req.setAttribute("Shib-Application-ID", "default");
        req.setAttribute("cn", "Billy Blogs");
        req.setAttribute("transient-name", "_f45tgfde3fdsa46hf4tfcd3g5rr45ttg778ikkjgt6");
        req.setAttribute("javax.servlet.request.cipher_suite", "DHE-RSA-AES256-SHA");
        req.setAttribute("Shib-Identity-Provider", "https://idp.example.com/idp/shibboleth");
        req.setAttribute("displayName", "Billy Blogs");


        session = new JsonSessionState();
        s.ssoInit(session, req);
        s.ssoPrepareLogin(session, "https://sp.example.com/sp/default/home", "https://sp.example.com/sp");
        s.ssoCheckUserDetails(session);
    }

    @Test
    public void testUser() {

        GenericUser user = (GenericUser)s.getUserObject(session);
        Assert.assertEquals(user.getUsername(), "ddsdsf678hgH878786G67F7Fg");
        Assert.assertEquals(user.realName(), "Billy Blogs");

    }

    @Test
    public void testRoles()
    {
        List<String> roles = s.getRolesList(session);
        Assert.assertTrue("The role list did not contain: institution", roles.contains("institution"));
    }

    @Test
    public void testInterface()
    {
        Assert.assertEquals("<a href=\"https://sp.example.com/sp/default/sso/shibboleth?ssoId=Shibboleth\">Shibboleth Login</a>",
                s.getInterface("https://sp.example.com/sp/default/sso?ssoId=Shibboleth"));
    }

}
