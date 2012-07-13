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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Fascinator Shibboleth Integration
 *
 * @author Nigel Bajema
 */
public class TemplateTest {

    @Test
    public void testTemplate() throws Exception {
        Properties props = new Properties();
        props.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "src/main/config/portal");
        Velocity.init(props);
        Template t = Velocity.getTemplate("shibboleth/interface.vm");
        StringWriter sw = new StringWriter();
        VelocityContext vc = new VelocityContext();
        vc.put("shibboleth_url", "http://example.com/idp");
        t.merge(vc, sw);
        Assert.assertEquals("<a href=\"http://example.com/idp\"><img title=\"AAF\" src=\"http://wiki.aaf.edu.au/home/_/rsrc/1316786640042/config/aaf_button.png\" alt=\"AAF Shibboleth Login\"></a>", sw.toString());
    }
}
