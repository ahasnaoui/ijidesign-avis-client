<%@ include file="/WEB-INF/jsp/init.jsp"%>

<%@ include file="view.jsp"%>

<portlet:actionURL var="repondreAvisURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="action" value="repondre" />
</portlet:actionURL>

<div id="${portletnamespace}repondreAvisModal" class="interiale-modal modal fade" role="dialog" style="width:700px !important;">

	<form:form modelAttribute="avisForm" id="${portletnamespace}form-repondreAvis" class="form-horizontal form-repondreAvis" action="${repondreAvisURL}" title="Formulaire de réponse à un avis">
	
	<div class="modal-dialog" role="document">

		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><i class="fa fa-close"></i></button>
				<h4 class="modal-title">Répondre à l'avis</h4>		
			</div>

			<div class="modal-body" style="padding-top: 0px;">

				
					<div class="cf_form form-1">
						<div class=" content_big">
	
							<div>
								<!-- Identifiant de l'avis -->
								<spring:bind path="commentaireId">
									<input type="hidden" name="<portlet:namespace/>commentaireId" value="${avisForm.commentaireId}" id="<portlet:namespace/>commentaireId" />
								</spring:bind>
								<!-- Identifiant de la réponse -->
								<spring:bind path="reponseId">
									<input type="hidden" name="<portlet:namespace/>reponseId" value="${avisForm.reponseId}" id="<portlet:namespace/>reponseId" />
								</spring:bind>
								
								<div class="form-group">
									<label class="control-label col-xs-12 col-sm-3 col-md-2" for="commentaire">
											Commentaire&nbsp;<span class="text-danger">*</span>
										</label>
										<div class="col-xs-12">
											${avisForm.commentaire}
										</div>
								</div>
								
								<!-- Nom -->
								<spring:bind path="nom">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="nom">
											Nom&nbsp;
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.nom}" name="<portlet:namespace/>nom" id="<portlet:namespace/>nom" class="form-control" placeholder="Votre nom" />
											<span class="bi bi-gray tt tt-nom" data-name="nom" data-type="text" data-text="Merci de renseigner votre nom" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
										<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
											<span class="aide">Si vide alors l'auteur sera <i>Intériale</i></span>
										</div>
									</div>
								</spring:bind>	
								
								<!-- Prénom -->
								<spring:bind path="prenom">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="prenom">
											Prénom&nbsp;
										</label>
										<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
											<input type="text" value="${avisForm.prenom}" name="<portlet:namespace/>prenom" id="<portlet:namespace/>prenom" class="form-control" placeholder="Votre prénom" />
											<span class="bi bi-gray tt tt-prenom" data-name="prenom" data-type="text" data-text="Merci de renseigner votre prénom" style="display: inline;"></span> 
											<span class="glyphicon glyphicon-ok form-control-feedback"></span>
										</div>
										<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
											<span class="aide">Si vide alors l'auteur sera <i>Intériale</i></span>
										</div>
									</div>
								</spring:bind>

								<!-- Réponse -->
								<spring:bind path="reponse">
									<div class="form-group has-feedback ">
										<label class="control-label col-xs-12 col-sm-3 col-md-2" for="reponse">
											Réponse&nbsp;<span class="text-danger">*</span>
										</label>
										<div class="col-xs-12">
											<textarea style="height: 250px;" name="<portlet:namespace/>reponse" id="<portlet:namespace/>reponse" class="form-control" placeholder="Votre réponse" >${avisForm.reponse}</textarea>
											<span class="bi bi-gray tt tt-reponse" data-name="reponse" data-type="text" data-text="Merci de renseigner votre réponse" style="display: inline;"></span> 
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
						<input id="<portlet:namespace/>submit-button" type="submit" class="button btn btn-submit" style="color:#FFF"  value="Répondre"/>
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
		
		// Référence vers la modal
        var popin = $('#<portlet:namespace/>repondreAvisModal');

        // Shows up the popin
        popin.modal("show");
        
     	// Trigger ajax call only when popin is fully hidden and user pushes valid button
        popin.on('hidden.bs.modal', function() {
        	document.location.href = '${renderUrl}';            
        });
		
		// Definition du validateur du formulaire
	    $('#<portlet:namespace/>form-repondreAvis')
	        .validate(
	            {
	                rules : {
		                "<portlet:namespace/>reponse" : {
		                    required : true,
		                    maxlength : 4000,
		                    minlength : 4
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