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

import com.googlecode.fascinator.api.authentication.User;
import com.googlecode.fascinator.common.JsonSimpleConfig;
import com.googlecode.fascinator.portal.JsonSessionState;
import com.googlecode.fascinator.portal.sso.SSOInterface;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Fascinator Shibboleth Integration
 * @author Nigel Bajema
 */
public class Shibboleth implements SSOInterface{
    private static Logger logger = LoggerFactory.getLogger(Shibboleth.class);
    private Template t;
    private String portalUrl;
    private PythonInterpreter py = new PythonInterpreter();

    private static final String HOME = "shib-go-home";


    private static String SHIB_SESSION_ID;
    private static String SHIB_IDP;
    private static String SHIB_COMMON_NAME;
    private static String SHIB_USER_NAME;

    private static final String SHIBBOLETH_PLUGIN_ID = "Shibboleth";

    {
        try {
            logger.debug(String.format("Resource Loader Path: %s", Velocity.getProperty(Velocity.FILE_RESOURCE_LOADER_PATH).toString()));
            t = Velocity.getTemplate("shibboleth/interface.vm");
            JsonSimpleConfig d = new JsonSimpleConfig();
            String shib_path = SHIBBOLETH_PLUGIN_ID.toLowerCase();
            SHIB_SESSION_ID = d.getString("Shib_Session_ID", shib_path, "shib_session_attribute");
            SHIB_IDP = d.getString("Shib_Identity_Provide", shib_path, "ship_idp_attribute");
            SHIB_COMMON_NAME = d.getString("cn", shib_path, "cn_attribute");
            SHIB_USER_NAME = d.getString("eppn", shib_path, "username_attribute");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


    }
    @Override
    public String getId() {
        return SHIBBOLETH_PLUGIN_ID;
    }

    @Override
    public String getLabel() {
        return "Shibboleth";
    }

    @Override
    public String getInterface(String ssoUrl) {
        logger.trace(String.format("ssoGetInterface: %s", ssoUrl));
        StringWriter sw = new StringWriter();
        VelocityContext vc = new VelocityContext();
        try {
            vc.put("shibboleth_url",ssoUrl);
            t.merge(vc, sw);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return sw.toString();
    }

    @Override
    public List<String> getRolesList(JsonSessionState jsonSessionState) {
        logger.trace("getRolesList");
        return new ArrayList<String>();
    }

    @Override
    public User getUserObject(JsonSessionState session) {
        logger.trace("getUserObject");
        String un = (String) session.get(SHIB_USER_NAME);
        String rn = (String) session.get(SHIB_COMMON_NAME);
        if(un != null)
        {
            ShibbolethUser user = new ShibbolethUser(un, rn);
            user.setSource(SHIBBOLETH_PLUGIN_ID);
            return user;
        }
        return null;
    }

    @Override
    public void logout(JsonSessionState session) {
        logger.trace("logout");
        session.remove(SHIB_USER_NAME);
        session.remove(SHIB_COMMON_NAME);
        session.remove(HOME);
    }

    @Override
    public void ssoInit(JsonSessionState session, HttpServletRequest request) throws Exception {
        logger.trace(String.format("ssoInit, URL: %s", portalUrl = (String) session.get("ssoPortalUrl")));
        Enumeration<String> attrs =  request.getAttributeNames();


        logger.trace("Request Attributes: ");
        while (attrs.hasMoreElements()) {
            String s = attrs.nextElement();
            logger.trace("\t"+s+" : "+request.getAttribute(s));
        }

        attrs = request.getHeaderNames();
        logger.trace("Request Headers: ");
        while (attrs.hasMoreElements()) {
            String s = attrs.nextElement();
            logger.trace("\t"+s+" : "+request.getHeader(s));

        }
        logger.debug("Portal URL"+portalUrl);

//        String tmpString = (String) request.getAttribute("transient-name");
        String tmpString = (String) request.getAttribute(SHIB_USER_NAME);
        if(tmpString != null){
            session.set(SHIB_USER_NAME, tmpString);
        }

        tmpString = (String) request.getAttribute(SHIB_COMMON_NAME);
        if(tmpString != null){
            session.set(SHIB_COMMON_NAME, tmpString);
        }

    }

    @Override
    public void ssoCheckUserDetails(JsonSessionState jsonSessionState) {
        logger.trace("ssoCheckUserDetails");
    }

    @Override
    public String ssoGetRemoteLogonURL(JsonSessionState session) {
        logger.trace("ssoGetRemoteLogonURL");
//        return "https://emu.hpc.jcu.edu.au";
        return (String) session.get(HOME);
    }

    @Override
    public void ssoPrepareLogin(JsonSessionState session, String returnAddress, String server) throws Exception {
        logger.trace(String.format("ssoPrepareLogin, Return Address: %s Server: %s", returnAddress, server));
        session.set(HOME, returnAddress);
    }
}


