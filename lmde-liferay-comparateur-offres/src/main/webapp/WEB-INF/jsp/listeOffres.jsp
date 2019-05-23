<div class="container-fluid">

	<div class="row">
    	<div class="col-xs-12">
    		<section class="content content-highlight">
             
              	<h3 id="selectionner-offres" class="title-lmde"><spring:message code="comparateurOffres.selection.titre" /></h3>             
             
            	<!-- Affichage des offres (complémentaires ou assurances) -->   
            	<c:if test="${codeGammeSelectionne ne codeGammeSurcomplementaire}"> 		
	              	<div class="col-xs-12 selector">					
						
						<div class="selector-head"><spring:message code="comparateurOffres.selection.selection1.titre" /></div>
	              			
	              		<c:forEach var="offre" items="${listeOffres.offre}" varStatus="status">
	              		     
	              		    <c:if test="${offre.codeGamme ne codeGammeSurcomplementaire}">
								<!-- Affichage d'une offre (complémentaire ou assurance) -->    
								<label>
				              		<span class="selector-title"><span>${offre.libelle}</span></span>
				              		<span class="selector-subtitle">${offre.description}</span>
				                 	<span class="selector-from"><spring:message code="comparateurOffres.graphique.aPartirDe" /></span>
				                 	<span class="selector-price">${offre.getTarifMoisString()} &euro;<span class="selector-month"><spring:message code="comparateurOffres.graphique.deviseMois" /></span></span>
				              		<input class="check-lmde type-${offre.codeGamme}" data-offer="offer-${offre.codeOffre}" type="checkbox">
				              	</label>
	             			</c:if>
						</c:forEach>
	            	</div> 
            	</c:if>
            	
            	<!-- Affichage des surcomplémentaires -->
            	<c:if test="${codeGammeSelectionne eq codeGammeSurcomplementaire}"> 	 	
	              	<div class="col-xs-12 selector selector-bis">					
						
						<div class="selector-head"><spring:message code="comparateurOffres.selection.selection2.titre" /></div>
	              			
	              		<c:forEach var="offre" items="${listeOffres.offre}" varStatus="status">
	              		     
	              			<c:if test="${offre.codeGamme == codeGammeSurcomplementaire}">
		            			<!-- Affichage d'une surcomplémentaire -->    	
		             		 	<label>
				              		<span class="selector-title"><span>${offre.libelle}</span></span>
				              		<span class="selector-subtitle">${offre.description}</span>
				                 	<span class="selector-from"><spring:message code="comparateurOffres.graphique.aPartirDe" /></span>
				                 	<span class="selector-price">${offre.getTarifMoisString()} &euro;<span class="selector-month"><spring:message code="comparateurOffres.graphique.deviseMois" /></span></span>
				              		<input class="check-lmde type-${offre.codeGamme}" data-offer="offer-${offre.codeOffre}" type="checkbox">
				              	</label>
			              	</c:if>
	             		
						</c:forEach>
	            	</div> 
            	</c:if>
            	
            </section>
          </div> <!-- /.col-xs-12 -->
	</div> <!-- /.row -->
</div>


<div class="text-center">
	<button id="button-compare" class="button button-action button-compare" type="submit">
		<span class="arrow arrow-ff"><i class="fa fa-chevron-right"></i><i class="fa fa-chevron-right"></i></span>
		<span class="text"><spring:message code="comparateurOffres.comparer.bouton.label" /></span>
	</button>
</div>