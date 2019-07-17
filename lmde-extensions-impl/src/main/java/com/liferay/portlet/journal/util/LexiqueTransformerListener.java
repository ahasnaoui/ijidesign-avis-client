package com.liferay.portlet.journal.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.templateparser.BaseTransformerListener;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalServiceUtil;
import com.orange.oab.lmde.liferay.lienlexique.model.LmdeLienLexique;
import com.orange.oab.lmde.liferay.lienlexique.service.LmdeLienLexiqueLocalServiceUtil;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;
import com.orange.oab.lmde.util.portal.LmdePortalUtil;

/**
 * Ce listener modifie le contenu affiché d'un article en fonction de certains termes qui lui sont associés.
 * Les termes sont entourés d'un lien redirigeant vers une certaine URL du portail.
 * 
 * @author Antoine Delahay (szft8267)
 */
public class LexiqueTransformerListener extends BaseTransformerListener {

    @Override
    public String onXml(String output, String languageId, Map<String, String> tokens) {

        Locale locale = LanguageUtil.getLocale(languageId);
        long contentId = Long.valueOf(tokens.get("article_resource_pk"));
        try {
            // Récupération des liens du lexique
            List<LmdeLienLexique> liensLexique = LmdeLienLexiqueLocalServiceUtil.getByContentId(contentId);
            if (liensLexique != null && !liensLexique.isEmpty()) {

                // Récupération de l'url de la page FAQ
                final Layout lexiqueLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(LmdePortalUtil.getLmdeGroupId(), false,
                        LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_LEXIQUE);
                final String lexiquePageUrl = this.buildLayoutUrl(lexiqueLayout);

                for (LmdeLienLexique lienLexique : liensLexique) {
                    try {
                        DDLRecord ddlRecord = DDLRecordLocalServiceUtil.getRecord(lienLexique.getLexiqueTermeId());
                        String terme = ddlRecord.getField("Terme").getRenderedValue(locale);
                        String description = ddlRecord.getField("Brève_description").getRenderedValue(locale);
                        output = replace(output, terme, description, lexiquePageUrl);
                    } catch (PortalException e) {
                        _log.error(e.getMessage(), e);
                    }
                }
            }
        } catch (SystemException e) {
            _log.error(e.getMessage(), e);
        } catch (PortalException e) {
            _log.error(e.getMessage(), e);
        }

        return output;
    }

    /**
     * Parcourt le contenu et remplace chaque occurence du terme par le même mot entouré d'un lien Html.
     * 
     * @param output le contenu affiché de l'article
     * @param terme le mot à chercher et remplacer
     * @param description la description à afficher en info-bulle
     * @return le contenu modifié
     */
    private String replace(String output, String terme, String description, String url) {

        StringBuilder replaced = new StringBuilder();

        // Construit l'ancre du lien (minuscules, remplacement des caractères spéciaux)
        String anchor = terme.toLowerCase().replace(StringPool.QUOTE, StringPool.BLANK).replace(StringPool.SPACE, StringPool.BLANK)
                .replace(StringPool.OPEN_PARENTHESIS, StringPool.BLANK).replace(StringPool.CLOSE_PARENTHESIS, StringPool.BLANK)
                .replace(StringPool.APOSTROPHE, StringPool.BLANK).replace(StringPool.APOSTROPHE, StringPool.BLANK).replaceAll("à|â", "a").replace('ç', 'c')
                .replaceAll("é|ë|è|ê", "e").replaceAll("î|ï", "i").replaceAll("ô|ö", "o").replaceAll("û|ü|ù", "u").replace("œ", "oe")
                .replace("°", StringPool.BLANK).replace("€", StringPool.BLANK);

        // Construit la partie fixe du lien
        StringBuilder sbLinkStart = new StringBuilder();
        sbLinkStart.append("<a class=\"lexique\" href=\"");
        sbLinkStart.append(url);
        sbLinkStart.append("#");
        sbLinkStart.append(anchor);
        sbLinkStart.append("\" title=\"");
        sbLinkStart.append(description);
        sbLinkStart.append("\">");
        String linkEnd = "</a>";

        // Regex sur le terme à remplacer
        String targetString = Pattern.quote(terme);
        targetString = "(?i)\\b" + targetString + "\\b";
        Matcher m = Pattern.compile(targetString).matcher(output);

        int readerIndex = 0;
        while (m.find()) {
            String originalWord = m.group();
            String link = sbLinkStart.toString() + originalWord + linkEnd;
            replaced.append(output.substring(readerIndex, m.start()) + link);
            readerIndex = m.end();
        }
        replaced.append(output.substring(readerIndex, output.length()));

        return replaced.toString();
    }

    /**
     * Construit une URL compléte à partir d'un Layout
     * 
     * @param layout page Liferay
     * @return URL du layout
     * @throws PortalException
     * @throws SystemException
     */
    private String buildLayoutUrl(Layout layout) throws PortalException, SystemException {

        StringBuilder sb = new StringBuilder();
        Group group = layout.getGroup();
        String virtualHostName = layout.getLayoutSet().getVirtualHostname();
        String portalURL = PortalUtil.getPortalURL(virtualHostName, PortalUtil.getPortalPort(false), false);
        sb.append(portalURL);
        if (layout.isPrivateLayout()) {
            if (group.isUser()) {
                sb.append(PortalUtil.getPathFriendlyURLPrivateUser());
            } else {
                sb.append(PortalUtil.getPathFriendlyURLPrivateGroup());
            }
        } else {
            sb.append(PortalUtil.getPathFriendlyURLPublic());
        }
        sb.append(group.getFriendlyURL());
        sb.append(layout.getFriendlyURL());

        return sb.toString();
    }

    private static Log _log = LogFactoryUtil.getLog(ContentTransformerListener.class);

}