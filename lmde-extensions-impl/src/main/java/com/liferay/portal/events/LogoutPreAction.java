/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author Brian Wing Shun Chan
 */
public class LogoutPreAction extends Action {

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response) {
        if (_log.isDebugEnabled()) {
            _log.debug("Running " + request.getRemoteUser());
        }

        // CUSTOM LMDE : LMDE-449 - Vider les caches Service et Bean à la déconnexion
        // voir SessionDestroyAction également appelé suite à une deconnexion.
        // FIN CUSTOM LMDE
    }

    private static Log _log = LogFactoryUtil.getLog(LogoutPreAction.class);

}