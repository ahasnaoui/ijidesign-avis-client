package com.liferay.portal.velocity;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.exception.SystemException;
import com.orange.oab.lmde.liferay.contact.NoSuchLienContactException;
import com.orange.oab.lmde.liferay.contact.NoSuchLienReseauSocialException;
import com.orange.oab.lmde.liferay.contact.model.LienContact;
import com.orange.oab.lmde.liferay.contact.model.LienReseauSocial;
import com.orange.oab.lmde.liferay.contact.service.LienContactLocalServiceUtil;
import com.orange.oab.lmde.liferay.contact.service.LienReseauSocialLocalServiceUtil;

public class LienContactHelper {

    private static final Logger LOGGER = Logger.getLogger(LienContactHelper.class);

    private static final LienContactHelper inner = new LienContactHelper();

    public static LienContactVO getLienContactVO(long groupId, String name) throws SystemException {
        LienContactVO lienContactVO = inner.new LienContactVO();
        lienContactVO.setName(name);
        try {
            lienContactVO = buildLienContactVO(LienContactLocalServiceUtil.findByGroupIdAndName(groupId, name));
        } catch (NoSuchLienContactException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        return lienContactVO;
    }

    public static LienReseauSocialVO getLienReseauSocialVO(long groupId, String name) throws SystemException {
        LienReseauSocialVO lienReseauSocialVO = inner.new LienReseauSocialVO();
        lienReseauSocialVO.setName(name);
        try {
            lienReseauSocialVO = buildLienReseauSocialVO(LienReseauSocialLocalServiceUtil.findByGroupIdAndName(groupId, name));
        } catch (NoSuchLienReseauSocialException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        return lienReseauSocialVO;
    }

    private static LienContactVO buildLienContactVO(LienContact lienContact) {

        LienContactVO lienContactVO = inner.new LienContactVO();
        lienContactVO.setDisplay(lienContact.getDisplay());
        lienContactVO.setName(lienContact.getName());
        lienContactVO.setUrl(lienContact.getUrl());
        lienContactVO.setLabel(lienContact.getLabel());
        lienContactVO.setTitle(lienContact.getTitle());

        return lienContactVO;
    }

    private static LienReseauSocialVO buildLienReseauSocialVO(LienReseauSocial lienReseauSocial) {

        LienReseauSocialVO lienReseauSocialVO = inner.new LienReseauSocialVO();
        lienReseauSocialVO.setDisplay(lienReseauSocial.getDisplay());
        lienReseauSocialVO.setName(lienReseauSocial.getName());
        lienReseauSocialVO.setUrl(lienReseauSocial.getUrl());
        lienReseauSocialVO.setLabel(lienReseauSocial.getLabel());
        lienReseauSocialVO.setTitle(lienReseauSocial.getTitle());

        return lienReseauSocialVO;
    }

    public class LienContactVO {

        private String name;
        private boolean display;
        private String url;
        private String label;
        private String title;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the display
         */
        public boolean isDisplay() {
            return display;
        }

        /**
         * @param display the display to set
         */
        public void setDisplay(boolean display) {
            this.display = display;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

    }

    public class LienReseauSocialVO {

        private String name;
        private boolean display;
        private long fanCount;
        private String url;
        private String label;
        private String title;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the display
         */
        public boolean isDisplay() {
            return display;
        }

        /**
         * @param display the display to set
         */
        public void setDisplay(boolean display) {
            this.display = display;
        }

        /**
         * @return the fanCount
         */
        public long getFanCount() {
            return fanCount;
        }

        /**
         * @param fanCount the fanCount to set
         */
        public void setFanCount(long fanCount) {
            this.fanCount = fanCount;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }
    }
}
