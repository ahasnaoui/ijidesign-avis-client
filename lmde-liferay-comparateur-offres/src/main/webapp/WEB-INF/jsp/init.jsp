<%@ page import="javax.portlet.ActionRequest"%>
<%@ page import="javax.portlet.PortletURL"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.orange.oab.lmde.liferay.comparateuroffres.util.CodeGammeConstants"%>
<%@page import="com.orange.oab.lmde.liferay.comparateuroffres.util.ComparateurOffresConstants"%>


<liferay-theme:defineObjects/>
<portlet:defineObjects />

<c:set var="portletnamespace"><portlet:namespace /></c:set>

<c:set var="codeGammeComplementaireEtSurcomplementaire" value="<%=CodeGammeConstants.COMPLEMENTAIRE_ET_SURCOMPLEMENTAIRE %>"/>
<c:set var="codeGammeSurcomplementaire" value="<%=CodeGammeConstants.SURCOMPLEMENTAIRE %>"/>
<c:set var="codeGammeComplementaire" value="<%=CodeGammeConstants.COMPLEMENTAIRE %>" />
<c:set var="codeGammeSelectionne"><%= renderRequest.getPreferences().getValue(ComparateurOffresConstants.PREFERENCE_CODE_GAMME, "") %></c:set>

<spring:message var="codesGarantiesEtServices" code="comparateurOffres.servicesEtGaranties.codes" />
<spring:message var="codesGaranties" code="comparateurOffres.garanties.codes" />
<spring:message var="codesServices" code="comparateurOffres.services.codes" />


<spring:message var="codesGarantiesComplementairesGraphicMode" code="comparateurOffres.complementaires.graphicmode.garanties.codes" />
<spring:message var="codesGarantiesComplementairesDetailMode" code="comparateurOffres.complementaires.detailmode.garanties.codes" />
<spring:message var="codesServicesComplementaires" code="comparateurOffres.complementaires.services.codes" />


<spring:message var="codesGarantiesSurComplementairesGraphicMode" code="comparateurOffres.surcomplementaires.graphicmode.garanties.codes" />
<spring:message var="codesGarantiesSurComplementairesDetailMode" code="comparateurOffres.surcomplementaires.detailmode.garanties.codes" />
<spring:message var="codesServicesSurComplementaires" code="comparateurOffres.surcomplementaires.services.codes"/>
