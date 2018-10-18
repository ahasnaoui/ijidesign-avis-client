<%@ include file="/WEB-INF/jsp/init.jsp"%>

<portlet:renderURL var="renderUrl" windowState="<%=LiferayWindowState.NORMAL.toString() %>" copyCurrentRenderParameters="true" portletMode="<%=LiferayPortletMode.VIEW.toString()%>">
</portlet:renderURL>

<portlet:actionURL var="preparerModifierAvisURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="action" value="preparerModifierAvis" />
	<portlet:param name="commentaireId" value="commentaireIdValue"/>
</portlet:actionURL>

<portlet:actionURL var="preparerRepondreAvisURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="action" value="preparerRepondreAvis" />
	<portlet:param name="commentaireId" value="commentaireIdValue"/>
</portlet:actionURL>

<portlet:actionURL var="publierAvisURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>" copyCurrentRenderParameters="true" portletMode="<%=LiferayPortletMode.VIEW.toString()%>">
	<portlet:param name="action" value="publierAvis" />
	<portlet:param name="commentaireId" value="commentaireIdValue"/>
</portlet:actionURL>

<portlet:actionURL var="depublierAvisURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>" copyCurrentRenderParameters="true" portletMode="<%=LiferayPortletMode.VIEW.toString()%>">
	<portlet:param name="action" value="depublierAvis" />
	<portlet:param name="commentaireId" value="commentaireIdValue"/>
</portlet:actionURL>

<%
	final List<InterialeAvisCommentaireFO> beans = (List<InterialeAvisCommentaireFO>) request.getAttribute("listeAvis");
	final int delta = (Integer) request.getAttribute("delta");
%>

<div id="mainContainer" class="notie avis-administration">

	<div class="gnrl_forms_container cf_form_content form-1 form-liste-avis" style="padding: 0;">
	
		<div class="title-form" style="margin-bottom: 0px;">
        	<title>Administration des avis des clients Intériale</title>
        </div>

        <liferay-ui:search-container delta="<%=delta%>" emptyResultsMessage="no-entries">
		    <liferay-ui:search-container-results
		        results="<%= ListUtil.subList(beans, searchContainer.getStart(), searchContainer.getEnd())%>"
		        total="<%=beans.size()%>" />
		        
			    <liferay-ui:search-container-row className="fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO" modelVar="item">
		
			        <liferay-ui:search-container-column-text name="nom" property="nom" />
			
			        <liferay-ui:search-container-column-text name="prenom" property="prenom" />
			        
			        <liferay-ui:search-container-column-text name="Moyenne" property="formattedNote" />
			        
			        <liferay-ui:search-container-column-text name="Avis" property="truncatedCommentaire" />
			        
			        <liferay-ui:search-container-column-text name="Réponse" property="reponse" />
			        
			        <liferay-ui:search-container-column-text name="Date de dépôt" property="formattedDayDateCreation" />
			        
			        <liferay-ui:search-container-column-text name="Date de publication" property="formattedDayDatePublication" />
			         
			        <liferay-ui:search-container-column-text name="Actions">
						<input id="<portlet:namespace/>edit-avis${item.id}" type="button" commentaireId="${item.id}" class="editAvis admin-button"  value="Modifier"/><br/>
						<input id="<portlet:namespace/>answer-avis${item.id}" type="button" commentaireId="${item.id}" class="answerAvis admin-button"  value="Répondre"/><br/>
						<c:if test="${empty item.dateDebutPublication}">
							<input id="<portlet:namespace/>publish-avis${item.id}" type="button" commentaireId="${item.id}" class="publishAvis admin-button"  value="Publier"/><br/>
						</c:if>
						<c:if test="${not empty item.dateDebutPublication && empty item.dateFinPublication}">
							<input id="<portlet:namespace/>unpublish-avis${item.id}" type="button" commentaireId="${item.id}" class="unpublishAvis admin-button"  value="Dépublier"/>
						</c:if>
					</liferay-ui:search-container-column-text>
		
		    	</liferay-ui:search-container-row>
		    	
		    <liferay-ui:search-iterator />
		</liferay-ui:search-container>
    </div>
    
</div>

<script type="text/javascript">
	$(function() {
		
		<c:if test="${not empty avisPublie}">
		$.notify({
		    title : "Votre demande a été traitée avec succès",
		    message : "L'avis a été publié avec succès et est maintenant visible sur la page publique."
		}, {
		    type : 'success'
		});
		</c:if>
		
		<c:if test="${not empty avisDepublie}">
		$.notify({
		    title : "Votre demande a été traitée avec succès",
		    message : "L'avis a été dé-publié avec succès et n'est plus visible sur la page publique."
		}, {
		    type : 'success'
		});
		</c:if>
		
		// Publier un avis
		$(".publishAvis").click(function(e){
			// Prevent the form being submitted
   		    e.preventDefault();
			e.stopPropagation();
			
			var url = "${publierAvisURL}";
			url = url.replace("commentaireIdValue", $(this).attr('commentaireId'));
			
			document.location.href = url;
		});
		
		// Dé-publier un avis
		$(".unpublishAvis").click(function(e){
			// Prevent the form being submitted
   		    e.preventDefault();
			e.stopPropagation();
			
			var url = "${depublierAvisURL}";
			url = url.replace("commentaireIdValue", $(this).attr('commentaireId'));
			
			document.location.href = url;
		});
		
		// Modifier
		$(".editAvis").click(function(e){
			// Prevent the form being submitted
   		    e.preventDefault();
			e.stopPropagation();
			
			
			$('#<portlet:namespace/>editerAvisModal').remove();
			$('#<portlet:namespace/>repondreAvisModal').remove();
			
			var url = "${preparerModifierAvisURL}";
			url = url.replace("commentaireIdValue", $(this).attr('commentaireId'));
			
			$.ajax({
		       type : "POST",
		       url : url,
		       success : function(data) {    
		           $('.avis-administration').html(data);    
		           
		        	// Référence vers la modal
		           var popin = $('#<portlet:namespace/>editerAvisModal');

		           // Shows up the popin
		           popin.modal("show");
		           
		        	// Trigger ajax call only when popin is fully hidden and user pushes valid button
		           popin.on('hidden.bs.modal', function() {
		           		document.location.reload();            
		           });
		   		
		   		// Definition du validateur du formulaire
		   	    $('#<portlet:namespace/>form-modifierAvis')
		   	        .validate(
		   	            {
		   	                rules : {
		   		                "<portlet:namespace/>nom" : {
		   		                    required : true,
		   		                    minlength : 1
		   		                },
		   	                    "<portlet:namespace/>prenom": {
		   	                        required : true,
		   		                    minlength : 1
		   	                    },
		   	                    "<portlet:namespace/>numeroAdherent" : {
		   	                    	required : false,
		   	                        maxlength : 8
		   	                    },
		   	                    "<portlet:namespace/>email" : {
		   	                    	required : function(element) {
		   	                            return $('input[name="<portlet:namespace/>numeroAdherent"]').val() == "";
		   	                        },
		   	                        emailcustom : true
		   	                    },
		   	                    "<portlet:namespace/>categoryId" : {
		   		                    required : true
		   		                },
		   		                "<portlet:namespace/>commentaire" : {
		   		                    required : true,
		   		                    maxlength : 4000,
		   		                    minlength : 5
		   		                },
		   	                },
		   	                onfocusout : function(element) {
		   	                    $(element).valid();
		   	                },
		   	                highlight : function(element) {
		   	                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
		   	                    $(element).closest('.form-group').find('.fa').removeClass('fa-check').addClass('fa-remove');
		   	                    $(element).closest('.form-group').find('.tt').show().addClass('tt-error');
		   	                },
		   	                success : function(element) {
		   	                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
		   	                    $(element).closest('.form-group').find('.fa').removeClass('fa-remove').addClass('fa-check');
		   	                    $(element).closest('.form-group').find('.tt').show().removeClass('tt-error');
		   	                }
		   	            });
		           
		       },
		       error : function(e) {
		           $.notify({
						title : "Une erreur technique est survenue",
		            	message : "Veuillez nous excuser pour la gêne occasionnée."
		           }, {
		               	type : 'error'
		           });
		       }
			}); 
		});
	
		// Répondre
		$(".answerAvis").click(function(e){
			// Prevent the form being submitted
   		    e.preventDefault();
			e.stopPropagation();
			
			
			$('#<portlet:namespace/>editerAvisModal').remove();
			$('#<portlet:namespace/>repondreAvisModal').remove();
			
			var url = "${preparerRepondreAvisURL}";
			url = url.replace("commentaireIdValue", $(this).attr('commentaireId'));
			
			$.ajax({
		       type : "POST",
		       url : url,
		       success : function(data) {    
		           $('.avis-administration').html(data);    
		           
		           
		        	// Référence vers la modal
		           var popin = $('#<portlet:namespace/>repondreAvisModal');

		           // Shows up the popin
		           popin.modal("show");
		           
		           // Trigger ajax call only when popin is fully hidden and user pushes valid button
		           popin.on('hidden.bs.modal', function() {
		           		document.location.reload();            
		           });
		   		
			   		// Definition du validateur du formulaire
			   	    $('#<portlet:namespace/>form-repondreAvis')
			   	        .validate(
			   	            {
			   	            	rules : {
					                "<portlet:namespace/>reponse" : {
					                    required : true,
					                    maxlength : 4000,
					                    minlength : 5
					                },
				                },
			   	                onfocusout : function(element) {
			   	                    $(element).valid();
			   	                },
			   	                highlight : function(element) {
			   	                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
			   	                    $(element).closest('.form-group').find('.fa').removeClass('fa-check').addClass('fa-remove');
			   	                    $(element).closest('.form-group').find('.tt').show().addClass('tt-error');
			   	                },
			   	                success : function(element) {
			   	                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
			   	                    $(element).closest('.form-group').find('.fa').removeClass('fa-remove').addClass('fa-check');
			   	                    $(element).closest('.form-group').find('.tt').show().removeClass('tt-error');
			   	                }
			   	            });
			           
			       },
			       error : function(e) {
			           $.notify({
							title : "Une erreur technique est survenue",
			            	message : "Veuillez nous excuser pour la gêne occasionnée."
			           }, {
			               	type : 'error'
			           });
			       }
				}); 
		});
		
	});
</script>