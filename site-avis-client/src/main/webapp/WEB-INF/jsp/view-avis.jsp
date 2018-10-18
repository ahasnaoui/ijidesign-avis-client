<%@ include file="init.jsp"%>

<portlet:renderURL var="renderUrl" windowState="<%=LiferayWindowState.NORMAL.toString() %>" copyCurrentRenderParameters="true" portletMode="<%=LiferayPortletMode.VIEW.toString()%>">
<portlet:param name="avisSort" value="avisSortValue"/>
</portlet:renderURL>

<div class="container-fluid liste-avis-interiale liste-site-avis-interiale"> 
	<div class="row"> 
		<div class="leftbar">
		
			<div class="col-xs-12 form-filter no-padding-rl">
				<div class="col-xs-8 no-padding-rl">
					<form action="${renderUrl}" method="POST">
						<select name="avisSort" id="avisSort" onChange="javascript:trierAvis(this.value);" class="avis-sort">
							<option value="" disabled="" ${empty avisSortSelected || avisSortSelected == '' ? 'selected' : ''}>Trier par :</option>
							<option value="dateCreationDesc" ${avisSortSelected == 'dateCreationDesc' ? 'selected' : ''}>Dates de publication récentes</option>
							<option value="dateCreationAsc" ${avisSortSelected == 'dateCreationAsc' ? 'selected' : ''}>Dates de publication anciennes</option>
							<option value="notesDesc" ${avisSortSelected == 'notesDesc' ? 'selected' : ''}>Notes les plus hautes</option>
							<option value="notesAsc" ${avisSortSelected == 'notesAsc' ? 'selected' : ''}>Notes les plus basses</option>
						</select>
					</form>
				</div>
				<div class="col-xs-4 no-padding-rl hidden-xs">
        			<div class="button-box no-padding-rl">
        				<a href="/avis/rediger-un-avis" title="Cliquez ici pour rédiger un avis sur Intériale">Rédiger un avis</a>
        			</div>
    			</div>
				<div class="col-xs-12 no-padding-rl visible-xs">
        			<div class="button-box no-padding-rl" style="width: auto;">
        				<a href="/avis/rediger-un-avis" title="Cliquez ici pour rédiger un avis sur Intériale">Rédiger un avis</a>
        			</div>
    			</div>
			</div>
			
			<c:if test="${empty listeAvis}">
				<div class="col-xs-12">
					Aucun avis disponible.
				</div>
			</c:if>
			
			<liferay-ui:search-container total="${total}" delta="${delta}">
			
				<liferay-ui:search-container-results results="${listeAvis}" />
				<liferay-ui:search-container-row
					className="fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO"
					escapedModel="<%= true %>"
					modelVar="commentaire" indexVar="index">
	
	
					<div class="col-xs-12 col-avis-interiale no-padding-rl"> 
						<div class="col-xs-2 no-padding-rl hidden-xs"> 
							<div class="box2" id="bulle_avis_${commentaire.id}">
								<c:if test="${(index+1) % 3 == 0 }">
									<img class="img hidden-xs" src="<%= request.getContextPath() %>/images/bulle_verte.png" style="padding-left: 20px;padding-top: 30px;">
								</c:if>
								<c:if test="${(index+3) % 3 == 0 }">
									<img class="img hidden-xs" src="<%= request.getContextPath() %>/images/bulle_orange.png" style="padding-left: 20px;padding-top: 30px;">
								</c:if>
								<c:if test="${((index+3) % 3 != 0) &&  ((index+1) % 3 != 0)}">
									<img class="img hidden-xs" src="<%= request.getContextPath() %>/images/bulle_bleue.png" style="padding-left: 20px;padding-top: 30px;">
								</c:if>
							</div> 
						</div>
						
						<div class="col-xs-10 avis-zone" id="zone_avis_${commentaire.id}">  
							<div class="col-xs-12">
								<div class="col-xs-10 col-xs-10-custom">
									<span class="bold">Avis de ${commentaire.identite}</span>
									<br/>
									<span>(${commentaire.formattedDayDateCreationEntier} à ${commentaire.formattedHourDateCreation}h${commentaire.formattedMinutsDateCreation})</span>
								</div> 
								<div class="col-xs-2 col-xs-2-custom hidden-xs">
									<c:forEach begin="1" end="${commentaire.nbEtoilesJaunes}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_jaune2.png">
									</c:forEach>
									<c:forEach begin="1" end="${commentaire.nbEtoilesDemi}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_demi.png">
									</c:forEach>
									<c:forEach begin="1" end="${commentaire.nbEtoilesBlanches}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_blanche.png">
									</c:forEach>
								</div>
								<div class="col-xs-12 visible-xs" style="padding-left: 12px;">
									<c:forEach begin="1" end="${commentaire.nbEtoilesJaunes}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_jaune2.png">
									</c:forEach>
									<c:forEach begin="1" end="${commentaire.nbEtoilesDemi}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_demi.png">
									</c:forEach>
									<c:forEach begin="1" end="${commentaire.nbEtoilesBlanches}" varStatus="loop">
									    <img class="img etoile" src="<%= request.getContextPath() %>/images/etoile_blanche.png">
									</c:forEach>
								</div>
							</div>
							<div class="col-xs-11 paragraph">  
								<div class="avisSynthese"> 
									${commentaire.truncatedCommentaireSiteAvis}
								</div> 
								<div class="avisDetail" style="display:none;"> 
									${commentaire.commentaire}
								</div> 
							</div>
							<c:if test="${fn:length(commentaire.commentaire) > 250 || commentaire.reponses.size() > 0}">
								<div class="read-more"> 
									<a href="#" data-ref="${commentaire.id}" class="showMore ${commentaire.reponses.size() > 0 ? 'hasReponse' : ''} showMore${commentaire.id}" title="Lire la suite">Lire la suite</a>
								</div>
							</c:if>
						</div>
					</div>
					<c:if test="${commentaire.reponses.size() > 0}">
					<div class="col-xs-12 col-avis-interiale col-avis-interiale-reponse col-avis-interiale-${commentaire.id}" style="display: none;"> 
						<div class="col-xs-2 no-padding-rl hidden-xs"> 
							<div class="box2">
								<img class="img" src="<%= request.getContextPath() %>/images/bulle_blanche_logo.png" style="padding-left: 10px;">
							</div> 
						</div>
						
						<div class="col-xs-10 reponse-zone">  
							<div class="col-xs-11 paragraph" style="padding-bottom: 40px;">  
								<div class="reponse-text"> 
									<span class="bold">${commentaire.reponses[0].completeAuthor}</span>
									<br/>
									<span>(${commentaire.reponses[0].formattedDayDateCreationEntier} à ${commentaire.reponses[0].formattedHourDateCreation}h${commentaire.reponses[0].formattedMinutsDateCreation})</span>
									<br/>
									<span>${commentaire.reponses[0].reponse}</span>
								</div>  
							</div>
							<div class="read-reponse"> 
								<a href="#" class="showReponse" data-ref="${commentaire.id}" title="Réduire le volet">Réduire le volet</a>
							</div>
						</div>
					</div>
					</c:if>
				
				</liferay-ui:search-container-row>
				<c:if test="${total > delta }">
					<div class="col-xs-12" style="text-align: center;">
						<liferay-ui:search-iterator type="interiale" />
					</div>
				</c:if>
			</liferay-ui:search-container>
		</div>
	</div>
</div>

<script type="text/javascript">

	function trierAvis(formValue) {
		var resourceUrl = "${renderUrl}";
  		resourceUrl = resourceUrl.replace("avisSortValue", formValue);
  		document.location.href = resourceUrl;
	}
	
	function adaptBulleZone() {
		$(".box2").each(function() {
		    $(this).css('height', $(this).closest('.col-avis-interiale').height());
		});
		/*$(".col-avis-interiale-reponse .box2").each(function() {
		   $(this).css('height', $(this).closest('.col-avis-interiale-reponse').height());
		});*/
	}

	// Execute immediate
	$(function() {
		<c:if test="${not empty avisEnvoye}">
		$.notify({
		    title : "Votre avis a été posté avec succès",
		    message : "Votre avis sera visible sur le site après validation par le webmaster Intériale."
		}, {
		    type : 'success'
		});
		</c:if>
		
		$('.showMore').click(function(e){
			e.preventDefault();
			
			$('.avisSynthese').show();
			$('.avisDetail').hide();
			$('.avis-zone').css('background-color', 'transparent');
		    $('.showMore').show();
		    $('.box2').height('140');
		    
		    $('.col-avis-interiale-reponse').hide();

		    var text = $(this).text();
		    var commentaireId = $(this).attr('data-ref');
		    var hasReponse = $(this).hasClass('hasReponse');
		    
		    $('.showMore').html('Lire la suite');
		    
		    if (hasReponse && text.indexOf("Réduire le volet") == -1 ) {
		    	$(this).hide();
		    }
			
		    $(this).text( text.indexOf("Lire la suite") !== -1 ? "Réduire le volet" : "Lire la suite" );
		    if (text.indexOf("Lire la suite") !== -1) {
		    	$(this).closest('.avis-zone').css('background-color', '#058EC4');
		    	$(this).closest('.avis-zone').find('.avisDetail').show();
			    $(this).closest('.avis-zone').find('.avisSynthese').hide();
			    if (hasReponse) {
			    	$('.col-avis-interiale-' + commentaireId).show();
			    }
			    $(this).closest('.col-avis-interiale').find('.box2').height($(this).closest('.avis-zone').height());
			    $('.col-avis-interiale-' + commentaireId).find('.box2').height($('.col-avis-interiale-' + commentaireId).height());
		    } else {
		    	$(this).closest('.avis-zone').css('background-color', 'transparent');
		    	$(this).closest('.avis-zone').find('.avisDetail').hide();
			    $(this).closest('.avis-zone').find('.avisSynthese').show();
			    $('.col-avis-interiale-' + commentaireId).hide();
			    $(this).closest('.col-avis-interiale').find('.box2').height($(this).closest('.col-avis-interiale').height());
				adaptBulleZone();
		    }

		});
		
		$('.showReponse').click(function(e) {
			e.preventDefault();
			var commentaireId = $(this).attr('data-ref');
			$('.showMore' + commentaireId).trigger('click');
		});
		
	});
	
	adaptBulleZone();

</script>