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

import javax.servlet.http.HttpSession;

import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.orange.oab.lmde.service.common.LmdePortalCacheUtil;
import com.orange.oab.lmde.util.constant.LMDEConstants;

/**
 * @author Brian Wing Shun Chan
 */
public class SessionDestroyAction extends SessionAction {

    @Override
    public void run(HttpSession session) {
        if (_log.isDebugEnabled()) {
            _log.debug(session.getId());
        }
        // CUSTOM LMDE : LMDE-449 - Vider les caches Service et Bean à la déconnexion
        final String personneId = (String) session.getAttribute(LMDEConstants.USER_SCREENNAME);
        if (Validator.isNotNull(personneId)) {
            LmdePortalCacheUtil.invalidateUserCache(StringUtil.toUpperCase(personneId));
        }
        // LMDE-892 : Réinitialiser l'affichage de la popin
        session.removeAttribute("lmdeShowPopinEmail");
        // FIN CUSTOM LMDE
    }

    private static Log _log = LogFactoryUtil.getLog(SessionDestroyAction.class);

}