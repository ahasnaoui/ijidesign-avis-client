<div id="graphic-mode" class="formula-list formula-graphicmode">
	<div class="row">
		<div class="col-xs-3 col-sm-2">
			<div class="formula formula-nude">
				<div class="formula-garanties">
					<div class="garantie garantie-titre">
                        <span>Mes garanties</span>
                    </div>
					<c:forTokens items="${codesGarantiesComplementairesGraphicMode}" delims="," var="codeGarantie">			

						<div class="garantie">
							<span class="hidden-xs besoin-xsmall besoin-garantie <spring:message code="comparateurOffres.garantieOuService.${codeGarantie}.icone.css" />"></span>
							<span> <spring:message code="comparateurOffres.garantie.${codeGarantie}" /> </span>
						</div>
					</c:forTokens>
					
					<div class="garantie garantie-titre">
						<span>Mes assurances et mes services</span>
                    </div>
					
					<c:forTokens items="${codesServicesComplementaires}" delims="," var="codeService">			

						<div class="garantie">
							<span class="hidden-xs besoin-xsmall <spring:message code="comparateurOffres.garantieOuService.${codeService}.icone.css" />"></span>
							<span> <spring:message code="comparateurOffres.service.${codeService}" /> </span>
						</div>
					</c:forTokens>
				</div>
				<!-- /.formula-garanties -->
			</div>
		</div>

		<div class="col-xs-9 col-sm-8 no-padding" style="display: block;">
			<div class="carousel carousel-showmanymoveone slide" id="carousel-modegraphic" data-interval="false">
				<div class="carousel-inner" data-interval="false">
						
					<c:forEach var="offre" items="${listeOffres.offre}" varStatus="status">
						
						<!--  Affichage d'une offre en mode graphique -->
						<div class="coffer item-hidden" id="offer-${offre.codeOffre}">					
							<div class="col-xs-12 col-sm-4 col-md-3">							
								<div class="formula" id="comp-${offre.codeOffre}">
								
									<div class="formula-remove">
										<a href="#" title="Retirer cette offre"><i class="fa fa-times-circle"></i></a>
									</div>
									<h4>
										<span>${offre.libelle}</span>
									</h4>
									<div class="price-ribbon">
										<div class="wrapper">
											<span class="amount">${fn:split(offre.getTarifMoisString(), ",")[0]}<small>, ${fn:split(offre.getTarifMoisString(), ",")[1]}</small></span>
											<span class="yearly">${offre.getTarifAnneeString()} &euro;<spring:message code="comparateurOffres.graphique.deviseAnnee" /></span> 
											<span class="currency">&euro;<spring:message code="comparateurOffres.graphique.deviseMois" /></span>
										</div>
									</div>

									<div class="formula-garanties">
									
										<c:forTokens items="${codesGarantiesComplementairesGraphicMode}" delims="," var="codeGarantieOuService">			
											<c:set var="jaugeVar" value="${offre.garanties[codeGarantieOuService].jauge}"/>
											<!-- Affichage d'une garantie ou d'un service -->																										 
											<div class="garantie">
												<div class="garantie-niveau" data-niveau="${jaugeVar}">
													<div class="niveau ${jaugeVar >= 1 ? 'on' :'' }"></div>
													<div class="niveau ${jaugeVar >= 2 ? 'on' :'' }"></div>
													<div class="niveau ${jaugeVar >= 3 ? 'on' :'' }"></div>
												</div>
											</div>
										</c:forTokens>
										
										<div class="garantie garantie-titre"></div>
										
										<c:forTokens items="${codesServicesComplementaires}" delims="," var="codeGarantieOuService">			
											<c:set var="jaugeVar" value="${offre.services[codeGarantieOuService].jauge}"/>
											<!-- Affichage d'une garantie ou d'un service -->																										 
											<div class="garantie">
												<div class="garantie-niveau" data-niveau="${jaugeVar}">
													<div class="niveau ${jaugeVar >= 1 ? 'on' :'' }"></div>
													<div class="niveau ${jaugeVar >= 2 ? 'on' :'' }"></div>
													<div class="niveau ${jaugeVar >= 3 ? 'on' :'' }"></div>
												</div>
											</div>
										</c:forTokens>
																				
									</div>
									<!-- /.formula-garanties -->
									<div class="formula-link">
										<a href="${offre.lienDetails}" title="${detailOffreTexte}">${detailOffreTexte}</a>
									</div>
									<a class="button button-action button-compare" id="button-compare1" href="${offre.lienDevis}${offre.devisObligatoire ? '':'?code='.concat(offre.codeOffre)}">
										<span class="arrow"><i class="fa fa-chevron-right"></i><i class="fa fa-chevron-right"></i></span> 
										<span class="text"><spring:message code="comparateurOffres.adherer" /></span>
									</a>
								</div>
							</div>
						</div>
					</c:forEach>

				</div>
				<a class="left carousel-control" href="#carousel-modegraphic" data-slide="prev" style="display: none;"><i class="glyphicon glyphicon-chevron-left"></i></a>
				<a class="right carousel-control" href="#carousel-modegraphic" data-slide="next" style="display: none;"><i class="glyphicon glyphicon-chevron-right"></i></a>
			</div>
		</div>
						
		<div class="hidden-xs col-sm-2">
			<a class="formula-add" href="#" title="<spring:message code="comparateurOffres.comparer.ajouterUneOffre" />">
				<spring:message code="comparateurOffres.comparer.ajouterUneOffreAComparer" /><i class="fa fa-plus-circle"></i>
			</a>
		</div>
	</div>
</div>
