<%@ include file="/WEB-INF/jsp/init.jsp"%>

<%@page import="fr.interiale.portlet.siteavisclient.bean.CategorieNoteGlobaleFO" %>

<div class="col-xs-12" style="padding: 0;">
	
		<div class="title-form">
        	<h1>Note globale par item</h1>
        </div>
        
        <div class="col-xs-12" style="">
		    <div class="col-xs-6" style="background-color:#f28b2b;color:#FFF;font-size:15px;">Item</div>
		    <div class="col-xs-6 hidden hidden-xs" style="background-color:#f28b2b;color:#FFF;font-size:15px;">Nombre d'avis</div>
		    <div class="col-xs-6" style="background-color:#f28b2b;color:#FFF;font-size:15px;">Note globale</div>
		</div>
		
		
		<c:forEach var="note" items="${notesByItem}" varStatus="indexNote">
			<div class="col-xs-12">
				<div class="col-xs-6">${note.category.name}</div>
				<div class="col-xs-6 hidden hidden-xs">${note.noteGlobale.nbAvis}</div>
				<div class="col-xs-6">${note.formattedNote}</div>
			</div>
		</c:forEach>
		
</div>