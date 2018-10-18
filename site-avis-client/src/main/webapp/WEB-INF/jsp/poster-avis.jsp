<%@ include file="init.jsp"%>

<portlet:actionURL var="posterAvisURL">
	<portlet:param name="action" value="posterAvis" />
</portlet:actionURL>

<form:form modelAttribute="avisForm" id="${portletnamespace}form-posterAvis" class="form-horizontal form-posterAvis" action="${posterAvisURL}">

	<div class="form-steps-pages"> 
		<div id="page1" class="page active" data-step="step1"> 
			<!-- Informations personnelles --> 
			<div id="infos-perso-pan" class="pan active"> 
				<div class="pan-title"> 
					<h3 class="avis-title-zone">
						<span class="num num1"></span>
						Vos informations personnelles
					</h3> 
				</div> 
				<div class="pan-content">
				
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
							<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
								<span class="aide">Seule la première lettre de votre nom appaîtra</span>
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
							<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
								<span class="aide">&nbsp;</span>
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
								<input type="text" value="${avisForm.numeroAdherent}" maxlength="8" name="<portlet:namespace/>numeroAdherent" id="<portlet:namespace/>numeroAdherent" class="form-control" placeholder="Votre numéro d'adhérent" />
								<span class="bi bi-gray tt tt-numeroAdherent" data-name="numeroAdherent" data-type="text" data-text="Merci de renseigner votre numéro d'adhérent" style="display: inline;"></span> 
								<span class="glyphicon glyphicon-ok form-control-feedback"></span>
							</div>
							<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
								<span class="aide" style="padding-left:1px;">Facultatif</span>
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
							<div class="col-xs-12 col-md-offset-2 col-sm-6 col-md-7 no-padding-left">
								<span class="aide">Obligatoire si le n° d'adhérent n'est pas rempli</span>
							</div>
						</div>
					</spring:bind>
				
				</div>
			</div>
			
			<!-- Notes --> 
			<div id="notes-pan" class="pan active"> 
				<div class="pan-title"> 
					<h3 class="avis-title-zone"><span class="num num2"></span>Vos notes</h3> 
				</div> 
				<div class="pan-content">
					<div class="form-group has-feedback ">
						<label class="control-label col-xs-12 col-sm-3 col-md-2" for="<portlet:namespace/>noteProduits">
							Produits&nbsp;<span class="text-danger">*</span>
						</label>
						<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left"> 
						
							<label class="etoile etoile-blanche" for="<portlet:namespace/>produits1">
								<input type="radio" title="Produits" value="1"
								class="hidden"
								name="<portlet:namespace/>noteProduits" data-id="<portlet:namespace/>produits" 
								id="<portlet:namespace/>produits1">
							</label> 
							<label class="etoile etoile-blanche" for="<portlet:namespace/>produits2">
								<input type="radio" title="Produits" value="2"
								class="hidden"
								name="<portlet:namespace/>noteProduits" data-id="<portlet:namespace/>produits" 
								id="<portlet:namespace/>produits2">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>produits3">
								<input type="radio" title="Produits" value="3"
								class="hidden"
								name="<portlet:namespace/>noteProduits" data-id="<portlet:namespace/>produits" 
								id="<portlet:namespace/>produits3">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>produits4">
								<input type="radio" title="Produits" value="4"
								class="hidden"
								name="<portlet:namespace/>noteProduits" data-id="<portlet:namespace/>produits" 
								id="<portlet:namespace/>produits4">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>produits5">
								<input type="radio" title="Produits" value="5"
								class="hidden"
								name="<portlet:namespace/>noteProduits" data-id="<portlet:namespace/>produits" 
								id="<portlet:namespace/>produits5">
							</label>
						</div>
					</div>
					
					<div class="form-group has-feedback ">
						<label class="control-label col-xs-12 col-sm-3 col-md-2" for="<portlet:namespace/>noteRelationAdherent">
							Relation adhérent&nbsp;<span class="text-danger">*</span>
						</label>
						<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left"> 
						
							<label class="etoile etoile-blanche" for="<portlet:namespace/>relationAdherent1">
								<input type="radio" title="Relation adhérent" value="1"
								class="hidden"
								name="<portlet:namespace/>noteRelationAdherent" data-id="<portlet:namespace/>relationAdherent" 
								id="<portlet:namespace/>relationAdherent1">
							</label> 
							<label class="etoile etoile-blanche" for="<portlet:namespace/>relationAdherent2">
								<input type="radio" title="Relation adhérent" value="2"
								class="hidden"
								name="<portlet:namespace/>noteRelationAdherent" data-id="<portlet:namespace/>relationAdherent" 
								id="<portlet:namespace/>relationAdherent2">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>relationAdherent3">
								<input type="radio" title="Relation adhérent" value="3"
								class="hidden"
								name="<portlet:namespace/>noteRelationAdherent" data-id="<portlet:namespace/>relationAdherent" 
								id="<portlet:namespace/>relationAdherent3">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>relationAdherent4">
								<input type="radio" title="Relation adhérent" value="4"
								class="hidden"
								name="<portlet:namespace/>noteRelationAdherent" data-id="<portlet:namespace/>relationAdherent" 
								id="<portlet:namespace/>relationAdherent4">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>relationAdherent5">
								<input type="radio" title="Relation adhérent" value="5"
								class="hidden"
								name="<portlet:namespace/>noteRelationAdherent" data-id="<portlet:namespace/>relationAdherent" 
								id="<portlet:namespace/>relationAdherent5">
							</label>
						</div>
					</div>
					
					<div class="form-group has-feedback ">
						<label class="control-label col-xs-12 col-sm-3 col-md-2" for="<portlet:namespace/>noteActionPrevention">
							Actions de prévention&nbsp;<span class="text-danger">*</span>
						</label>
						
						<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left"> 
						
							<label class="etoile etoile-blanche" for="<portlet:namespace/>actionPrevention1">
								<input type="radio" title="Actions de prévention" value="1"
								class="hidden"
								name="<portlet:namespace/>noteActionPrevention" data-id="<portlet:namespace/>actionPrevention" 
								id="<portlet:namespace/>actionPrevention1">
							</label> 
							<label class="etoile etoile-blanche" for="<portlet:namespace/>actionPrevention2">
								<input type="radio" title="Actions de prévention" value="2"
								class="hidden"
								name="<portlet:namespace/>noteActionPrevention" data-id="<portlet:namespace/>actionPrevention" 
								id="<portlet:namespace/>actionPrevention2">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>actionPrevention3">
								<input type="radio" title="Actions de prévention" value="3"
								class="hidden"
								name="<portlet:namespace/>noteActionPrevention" data-id="<portlet:namespace/>actionPrevention" 
								id="<portlet:namespace/>actionPrevention3">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>actionPrevention4">
								<input type="radio" title="Actions de prévention" value="4"
								class="hidden"
								name="<portlet:namespace/>noteActionPrevention" data-id="<portlet:namespace/>actionPrevention" 
								id="<portlet:namespace/>actionPrevention4">
							</label>
							<label class="etoile etoile-blanche" for="<portlet:namespace/>actionPrevention5">
								<input type="radio" title="Actions de prévention" value="5"
								class="hidden"
								name="<portlet:namespace/>noteActionPrevention" data-id="<portlet:namespace/>actionPrevention" 
								id="<portlet:namespace/>actionPrevention5">
							</label>
						</div>

					</div>
				</div>
			</div>
			
			<!-- Avis --> 
			<div id="avis-pan" class="pan active"> 
				<div class="pan-title"> 
					<h3 class="avis-title-zone"><span class="num num3"></span>Votre avis</h3> 
				</div> 
				<div class="pan-content">
					<spring:bind path="commentaire">
					<div class="form-group has-feedback ">
						<label class="control-label col-xs-12 col-sm-3 col-md-2" for="commentaire">
							Commentaire&nbsp;<span class="text-danger">*</span>
						</label>
						<div class="col-xs-12 col-sm-6 col-md-7 no-padding-left">
							<textarea style="height: 279px" name="<portlet:namespace/>commentaire" id="<portlet:namespace/>commentaire" class="form-control" placeholder="Votre commentaire" >${avisForm.commentaire}</textarea>
							<span class="bi bi-gray tt tt-commentaire" data-name="commentaire" data-type="text" data-text="Merci de renseigner votre commentaire" style="display: inline;"></span> 
							<span class="glyphicon glyphicon-ok form-control-feedback"></span>
						</div>
					</div>
					</spring:bind>
				</div>
			</div>
			
			<!--  Bouton de soumission -->
			<div class="col-md-offset-4 submit-button-group">
				<input id="<portlet:namespace/>submit-button" type="button" class="btn_suivant btn btn-submit boutons_valider" value="Poster votre avis"/>
			</div>
			
			<!-- Note de bas de page -->
			<span class="aide"><span class="text-danger">*</span>&nbsp;Champs obligatoires</span>
		</div>
	</div>
</form:form>

<script type="text/javascript">
	
	// Execute immediate
	$(function() {
		
		// Initalisation du formulaire
		initTooltipText();
		
		// Definition du validateur du formulaire
	    $('#<portlet:namespace/>form-posterAvis')
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
	                        email : true
	                    },
		                "<portlet:namespace/>commentaire" : {
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

		$('.etoile-blanche').click(function (e) {
			$(this).closest('.form-group').find('.etoile-jaune').removeClass('etoile-jaune');
			$(this).addClass('etoile-jaune');
			var etoileSelectionee = $(this).find("input").val();
			for (i = 1; i < etoileSelectionee; i++) {
				$('#' + $(this).find("input").attr("data-id") + i).closest('label').addClass('etoile-jaune');
			}
		});
		
		$('#<portlet:namespace/>submit-button').click(function (e) {
			e.preventDefault;
			
			if ($('#<portlet:namespace/>form-posterAvis').valid() && $('#<portlet:namespace/>commentaire').val().length >= 4) {
				$('#<portlet:namespace/>form-posterAvis').submit();
			}
		});
		
		$('#<portlet:namespace/>submit-button').mouseover(function (e) {
			$('#<portlet:namespace/>submit-button').css('background-color', '#FFF');
			$('#<portlet:namespace/>submit-button').css('color', 'rgba(248, 165, 35, 1)');
			$('#<portlet:namespace/>submit-button').css('border-color', 'rgba(248, 165, 35, 1)');
		});
		
		$('#<portlet:namespace/>submit-button').mouseout(function (e) {
			$('#<portlet:namespace/>submit-button').css('background-color', 'rgba(248, 165, 35, 1)');
			$('#<portlet:namespace/>submit-button').css('color', '#FFF');
			$('#<portlet:namespace/>submit-button').css('border-color', 'transparent');
		});
		
	});
</script>