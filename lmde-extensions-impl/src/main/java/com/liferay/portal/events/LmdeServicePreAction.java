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
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.orange.oab.lmde.liferay.service.service.LmdeUserInfoLocalServiceUtil;
import com.orange.oab.lmde.util.user.LMDEUserUtil;
import com.orange.oab.lmde.ws.personne.exception.LmdeWebServicePersonneException;
import com.orange.oab.lmde.ws.personne.service.PersonneClientWebServiceUtil;

/**
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class LmdeServicePreAction extends Action {

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactoryUtil.getLog(LmdeServicePreAction.class);

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {

        updateUserMetadatas(request);
    }

    /**
     * LMDE JIRA 549: Portail - Webservices - Passer des métadonnées dans le header de la requête SOAP.
     * Authentification reussie, on mémorise:
     * - le bpu de l'utilisateur si celui-ci n'est pas interne Lmde.
     * Ces valeurs seront stockés dans la table lmde_user_info, ces valeurs seront à envoyer
     * dans tous les headers des requetes SOAP des web services LMDE.
     */
    private void updateUserMetadatas(HttpServletRequest request) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        if (Validator.isNotNull(themeDisplay)) {

            try {
                User user = themeDisplay.getUser();

                if (!user.isDefaultUser() && !LMDEUserUtil.isInterne(PortalUtil.getDefaultCompanyId(), user.getUserId())) {
                    String bpu = PersonneClientWebServiceUtil.getLightPersonneBean(user.getScreenName()).getBpu();
                    LmdeUserInfoLocalServiceUtil.updateBpu(user.getUserId(), bpu);
                }

            } catch (SystemException | LmdeWebServicePersonneException e) {
                LOGGER.warn("Impossible de renseigner les meta informations de l'utilisateur.", e);
            }
        }
    }
}