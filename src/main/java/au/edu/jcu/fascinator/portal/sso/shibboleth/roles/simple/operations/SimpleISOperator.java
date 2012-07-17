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

package au.edu.jcu.fascinator.portal.sso.shibboleth.roles.simple.operations;

import au.edu.jcu.fascinator.portal.sso.shibboleth.roles.simple.ShibSimpleRoleOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fascinator Shibboleth Integration
 *
 * @author Nigel Bajema
 */
public class SimpleISOperator implements ShibSimpleRoleOperator {
    private static Logger logger = LoggerFactory.getLogger(SimpleISOperator.class);
    @Override
    public String getOperator() {
        return "is";
    }

    @Override
    public boolean doOperation(String fromRule, String fromSession) {
        logger.trace(String.format("Rule's value: %s Session's value: %s, Result: %s", fromRule, fromSession, fromRule.equals(fromSession)));
        return fromRule.equals(fromSession);
    }
}
