<spring:message var="detailOffreTexte" code="comparateurOffres.graphique.detailOffre.texte" />

<div class="container-fluid">

	<div class="row">
    	<div class="col-xs-12">
    		<section class="content content-highlight">
             			
				<h3 id="comparer-offres" class="title-lmde"><spring:message code="comparateurOffres.comparer.titre" /></h3>
			
				<!-- Choix mode d'affichage -->
				<div class="margin-bottom-large margin-top-large text-center">
				
					<div class="form-group has-feedback">
					
						<label class="control-label"><spring:message code="comparateurOffres.comparer.affichage.titre" />&nbsp;&nbsp;</label>
						
						<div class="btn-group btn-group-radio" data-toggle="buttons">
							<label class="btn btn-default active">
								<input id="radio-mode-simplifie" name="mode" value="simplifie" required="" checked="" type="radio">
								<spring:message code="comparateurOffres.comparer.affichage.simplifie" />
							</label>
							<label class="btn btn-default"> 
								<input id="radio-mode-detaille" name="mode" value="detaille" type="radio">
								<spring:message code="comparateurOffres.comparer.affichage.detaille" />
							</label>
						</div>
					</div>
				</div>
				
				<c:if test="${codeGammeSelectionne eq codeGammeSurcomplementaire}">
					<%@ include file="./SURCOMP/modeGraphique.jsp"%>
					<%@ include file="./SURCOMP/modeDetaille.jsp"%>
				</c:if>
				
				<c:if test="${codeGammeSelectionne eq codeGammeComplementaire}">
					<%@ include file="./COMP/modeGraphique.jsp"%>
					<%@ include file="./COMP/modeDetaille.jsp"%>
				</c:if>
				
			</section>
		</div>
	</div>
</div>