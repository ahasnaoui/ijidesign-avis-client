<%@ include file="/WEB-INF/jsp/init.jsp"%>

<%@ include file="view.jsp"%>

<portlet:actionURL var="modifierAvisURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="action" value="modifierAvis" />
</portlet:actionURL>

<div id="${portletnamespace}editerAvisModal" class="interiale-modal modal fade" role="dialog" style="width:700px !important;">

	<form:form modelAttribute="avisForm" id="${portletnamespace}form-modifierAvis" class="form-horizontal form-modifierAvis" action="${modifierAvisURL}" title="Formulaire d'envoi d'un avis">
	
	<div class="modal-dialog" role="document">

		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><i class="fa fa-close"></i></button>
				<h4 class="modal-title">Modifier l'avis</h4>		
			</div>

			<div class="modal-body" style="padding-top: 0px;">

				
					<div class="cf_form form-1">
						<div class=" content_big">
	
							<div>
								<!-- Identifiant de l'avis -->
								<spring:bind path="id">
									<input type="hidden" name="<portlet:namespace/>id" value="${avisForm.id}" id="<portlet:namespace/>id" />
								</spring:bind>
							
								<!-- Nom -->
								<spring:bind path="nom">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="nom">
											Nom&nbsp;<span class="text-danger">*</span>
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.nom}" name="<portlet:namespace/>nom" id="<portlet:namespace/>nom" class="form-control" placeholder="Votre nom" />
											<span class="bi bi-gray tt tt-nom" data-name="nom" data-type="text" data-text="Merci de renseigner votre nom" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
									</div>
								</spring:bind>	
								
								<!-- Prénom -->
								<spring:bind path="prenom">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="prenom">
											Prénom&nbsp;<span class="text-danger">*</span>
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.prenom}" name="<portlet:namespace/>prenom" id="<portlet:namespace/>prenom" class="form-control" placeholder="Votre prénom" />
											<span class="bi bi-gray tt tt-prenom" data-name="prenom" data-type="text" data-text="Merci de renseigner votre prénom" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
									</div>
								</spring:bind>
								
								<!-- Numéro d'adhérent -->
								<spring:bind path="numeroAdherent">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="numeroAdherent">
											Numéro d'adhérent&nbsp;
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.numeroAdherent}" maxlength="8" name="<portlet:namespace/>numeroAdherent" id="<portlet:namespace/>numeroAdherent" class="form-control uppercase" />
											<span class="bi bi-gray tt tt-numeroAdherent" data-name="numeroAdherent" data-type="text" data-text="Merci de renseigner votre numéro d'adhérent" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
									</div>
								</spring:bind>
								
								<!-- Adresse email -->
								<spring:bind path="email">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="email">
											Email&nbsp;
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.email}" name="<portlet:namespace/>email" id="<portlet:namespace/>email" class="form-control email lowercase" placeholder="Votre email" />
											<span class="bi bi-gray tt tt-email" data-name="email" data-type="text" data-text="Merci de renseigner votre email" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
									</div>
								</spring:bind>
								
								<!-- Notes -->
								<div class="form-group">
									<label class="control-label col-xs-12 col-sm-3 col-md-2" for="notes">
										Produits&nbsp;
									</label>
									<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
										<select id="<portlet:namespace/>noteProduits" name="<portlet:namespace/>noteProduits" class="bigSelect form-control valid">
											<c:forEach begin="0" end="5" varStatus="loop">
											    <option value="${loop.index}" ${avisForm.noteProduits == loop.index ? 'selected': ''}>${loop.index}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-xs-12 col-sm-3 col-md-2" for="notes">
										Relation adhérent&nbsp;
									</label>
									<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
										<select id="<portlet:namespace/>noteRelationAdherent" name="<portlet:namespace/>noteRelationAdherent" class="bigSelect form-control valid">
											<c:forEach begin="0" end="5" varStatus="loop">
											    <option value="${loop.index}" ${avisForm.noteRelationAdherent == loop.index ? 'selected': ''}>${loop.index}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-xs-12 col-sm-3 col-md-2" for="notes">
										Actions de prévention&nbsp;
									</label>
									<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
										<select id="<portlet:namespace/>noteActionPrevention" name="<portlet:namespace/>noteActionPrevention" class="bigSelect form-control valid">
											<c:forEach begin="0" end="5" varStatus="loop">
											    <option value="${loop.index}" ${avisForm.noteActionPrevention == loop.index ? 'selected': ''}>${loop.index}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<!-- Commentaire -->
								<spring:bind path="commentaire">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="commentaire">
											Commentaire&nbsp;<span class="text-danger">*</span>
										</label>
										<div class="col-xs-12 no-padding-left">
											<textarea style="height: 250px;" name="<portlet:namespace/>commentaire" id="<portlet:namespace/>commentaire" class="form-control" placeholder="Votre commentaire" >${avisForm.commentaire}</textarea>
											<span class="bi bi-gray tt tt-commentaire" data-name="commentaire" data-type="text" data-text="Merci de renseigner votre commentaire" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
									</div>
								</spring:bind>

							</div>
						</div>
					</div>
				
			</div>

			<div class="modal-footer">

				<!-- Bouton Valider et Annuler -->
				<div class="row">
					<div class="col-sm-12 text-center">
						<input id="<portlet:namespace/>submit-button" type="submit" class="button btn btn-submit" style="color:#FFF"  value="Enregistrer les modifications"/>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	</form:form>

</div>

<script type="text/javascript">

 	// Execute immediate
	$(function() {
		
		// Initalisation du formulaire
		initTooltipText();
		
		// Référence vers la modal
        var popin = $('#<portlet:namespace/>editerAvisModal');

        // Shows up the popin
        popin.modal("show");
        
     	// Trigger ajax call only when popin is fully hidden and user pushes valid button
        popin.on('hidden.bs.modal', function() {
        	document.location.href = '${renderUrl}';            
        });
		
		// Definition du validateur du formulaire
	    $('#<portlet:namespace/>form-modifierAvis')
	        .validate(
	            {
	                rules : {
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

	});
</script>