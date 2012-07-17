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

package au.edu.jcu.fascinator.portal.sso.shibboleth;

import com.googlecode.fascinator.portal.JsonSessionState;

import java.util.List;

/**
 * Fascinator Shibboleth Integration
 *
 * @author Nigel Bajema
 */
public interface ShibbolethRoleManager {
    public String getId();
    public List<String> getRolesList(JsonSessionState session);
}
