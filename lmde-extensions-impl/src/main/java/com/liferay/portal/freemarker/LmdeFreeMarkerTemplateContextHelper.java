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

package com.liferay.portal.freemarker;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;
import com.orange.oab.lmde.util.portal.LmdePortalUtil;

/**
 * Surcharge de la preparation du contexte Freemarker (pour les templates d'affichage des contenus web)
 * 
 * @author hkxz3449
 */
public class LmdeFreeMarkerTemplateContextHelper extends FreeMarkerTemplateContextHelper {

    private static final Logger LOGGER = Logger.getLogger(LmdeFreeMarkerTemplateContextHelper.class);

    /*
     * (non-Javadoc)
     * @see
     * com.liferay.portal.freemarker.FreeMarkerTemplateContextHelper#prepare(com.liferay.portal.kernel.template.Template
     * , javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void prepare(Template template, HttpServletRequest request) {
        super.prepare(template, request);

        LOGGER.info("Initialize templates");

        try {
            final Long lmdeGroupId = LmdePortalUtil.getLmdeGroupId();

            try {
                // La page de faq
                final Layout faqLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false, LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_FAQ);
                template.put("lmdePageFaqUrl", faqLayout.getRegularURL(request));
            } catch (NoSuchLayoutException e) {
                LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_FAQ + " non existante");
            }

        } catch (SystemException | PortalException e) {
            LOGGER.error("Erreur lors de l'intialisation du contexte velocity : " + e.getMessage());
        }
    }

}