<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayPortletMode"%>
<%@page import="com.liferay.portlet.asset.model.AssetCategory" %>
<%@page import="fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO" %>
<%@page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@page import="java.util.List" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<c:set var="portletnamespace">
	<portlet:namespace />
</c:set>

<style>
	.alert {
		top : 60px !important;
	}
	
	.aui#interiale .note-globale-header {
		padding-top: 20px;
	}
</style>