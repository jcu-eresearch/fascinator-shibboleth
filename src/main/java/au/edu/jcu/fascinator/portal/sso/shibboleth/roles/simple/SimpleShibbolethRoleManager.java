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

package au.edu.jcu.fascinator.portal.sso.shibboleth.roles.simple;

import au.edu.jcu.fascinator.portal.sso.shibboleth.ShibbolethRoleManager;
import com.googlecode.fascinator.common.JsonObject;
import com.googlecode.fascinator.common.JsonSimpleConfig;
import com.googlecode.fascinator.portal.JsonSessionState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ServiceLoader;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static au.edu.jcu.fascinator.portal.sso.shibboleth.Constants.SHIBBOLETH_DELIMITER;
import static au.edu.jcu.fascinator.portal.sso.shibboleth.Constants.SHIBBOLETH_PLUGIN_ID;

/**
 * Fascinator Shibboleth Integration
 *
 * @author Nigel Bajema
 */
public class SimpleShibbolethRoleManager implements ShibbolethRoleManager {
    private static final Logger logger = LoggerFactory.getLogger(SimpleShibbolethRoleManager.class);
    private JsonObject cfg;
    private final Hashtable<String, ShibSimpleRoleOperator> operations = new Hashtable<String, ShibSimpleRoleOperator>();

    public static final int ATTR_POS = 0;
    public static final int OP_POS = 1;
    public static final int VALUE_POS = 2;

    public SimpleShibbolethRoleManager() {
        try {
            JsonSimpleConfig config = new JsonSimpleConfig();
            cfg = config.getObject(SHIBBOLETH_PLUGIN_ID, SimpleShibbolethRoleManager.class.getSimpleName());
            ServiceLoader<ShibSimpleRoleOperator> providers = ServiceLoader.load(ShibSimpleRoleOperator.class);
            for (ShibSimpleRoleOperator provider : providers) {
                operations.put(provider.getOperator(), provider);
                logger.trace(String.format("Loaded Simple Shibboleth Role Operation: %s as %s", provider.getOperator(), provider.getClass()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getId() {
        return "SimpleShibbolethRoleManager";
    }

    @Override
    public List<String> getRolesList(JsonSessionState session) {
        List<String> toRet = new ArrayList<String>();
        if (cfg == null) {
            return toRet;
        }
        JSONArray tmp, _tmp;
        JSONArray rule;
        ArrayList _attr;
        String attr, op;
        ShibSimpleRoleOperator operation;
        for (Object role : cfg.keySet()) {
            _tmp = (JSONArray) cfg.get(role);
            for (Object o : _tmp) {
                tmp = (JSONArray) o;

                logger.info(String.format("%s Array: %s", role, tmp.toJSONString()));
                int entryCount = 0;
                for (Object _rule : tmp) {
                    if (_rule instanceof JSONArray) {
                        rule = (JSONArray) _rule;
                        _attr = (ArrayList) session.get(rule.get(ATTR_POS).toString());
                        if (_attr != null) {
                            logger.trace("Found attr: " + rule.get(ATTR_POS) + " in session.");
                            operation = operations.get(op = rule.get(OP_POS).toString());
                            if (operation != null) {
                                for (Object s : _attr) {
                                    attr = (String) s;
                                    if (operation.doOperation(rule.get(VALUE_POS).toString(), attr)) {
                                        entryCount++;
                                    }
                                }
                            } else {
                                logger.error(String.format("The operation: %s is unknown, skipping.", op));
                            }
                        }
                    }
                    else
                    {
                        logger.error(String.format("The %s role is not correctly configured fo the %s role manager.",
                                role, SimpleShibbolethRoleManager.class.getName()));
                    }
                }
                logger.trace(String.format("Entry Count: %d Size: %d", entryCount, tmp.size()));
                if (entryCount == tmp.size()) {
                    toRet.add(role.toString());
                }
            }
        }
        return toRet;
    }
}
