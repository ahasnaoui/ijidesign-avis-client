<div id="detail-mode" class="formula-list formula-detailmode" style="display: none;">
	<div class="row">
		<div class="col-xs-5 col-sm-3 no-padding-right">
			<div class="formula formula-nude">
				<div class="formula-garanties">
				
					<div class="garantie garantie-titre">
                        <span>Mes garanties</span>
                    </div>

					<!-- Garanties -->
					<c:forTokens items="${codesGarantiesSurComplementairesGraphicMode}" delims="," var="codeGarantie">
						<div class="garantie">
							<span class="hidden-xs besoin-xsmall besoin-garantie <spring:message code="comparateurOffres.garantieOuService.${codeGarantie}.icone.css" />"></span>
							<span> <spring:message code="comparateurOffres.garantie.${codeGarantie}" /> </span>
						</div>
						
						<!-- Sous garanties -->
						<spring:message var="codesSousGaranties" code="comparateurOffres.${codeGarantie}.codes" />
						<c:if test="${not empty codesSousGaranties}">
							<c:forTokens items="${codesSousGaranties}" delims="," var="codeSousGarantie">
								<div class="garantie garantie-detail">
									<span> <spring:message code="comparateurOffres.sousgarantie.${codeSousGarantie}" /> </span>
								</div>
							</c:forTokens>
						</c:if>
						
					</c:forTokens>
					
					<div class="garantie garantie-titre">
                        <span>Mes assurances et mes services</span>
                    </div>
					
					<!-- Services -->
					<c:forTokens items="${codesServicesSurComplementaires}" delims="," var="codeService">
						<div class="garantie garantie-detail">
							<span> <spring:message code="comparateurOffres.service.${codeService}" /> </span>
						</div>
					</c:forTokens>
					
					
				</div>
				<!-- /.formula-garanties -->
			</div>
		</div>
			
		<div class="col-xs-7 col-sm-7 no-padding" style="display: block;">
			<div class="carousel carousel-showmanymoveone slide" id="carousel-modedetail" data-interval="false">
				<div class="carousel-inner border-left" data-interval="false">
												
					<c:forEach var="offre" items="${listeOffres.offre}" varStatus="status">
					
						<!--  Affichage d'une offre en mode détaillé -->
						<div class="coffer item-hidden" id="detail-offer-${offre.codeOffre}">						
							<div class="col-xs-12 col-sm-4 col-md-3 border-right">
								
								<div class="formula" id="detail-${offre.codeOffre}">
									<div class="formula-remove">
										<a href="#" title="Retirer cette offre"><i class="fa fa-times-circle"></i></a>
									</div>
									<h4>
										<span>${offre.libelle}</span>
									</h4>
									<div class="formula-detail-price">
										<span class="selector-price">${offre.getTarifMoisString()} &euro;<span class="selector-month"><spring:message code="comparateurOffres.graphique.deviseMois" /></span></span>
									</div>							

									<div class="formula-garanties">

										<!-- Garanties -->
										<c:set var="estPremiereGarantie" value="1" />
										<c:forTokens items="${codesGarantiesSurComplementairesGraphicMode}" delims="," var="codeGarantie">
											<c:if test="${estPremiereGarantie == 0}">
											<div class="garantie">&nbsp;</div>
											</c:if>
											<c:set var="estPremiereGarantie" value="0" />
											
											<!-- Sous garanties -->
											<spring:message var="codesSousGaranties" code="comparateurOffres.${codeGarantie}.codes" />
											<c:if test="${not empty codesSousGaranties }">
												<c:forTokens items="${codesSousGaranties}" delims="," var="codeSousGarantie">
													<div class="garantie garantie-detail">
														<c:set var="detailTrouve" value="0" />
														<c:forEach items="${offre.garanties[codeGarantie].garantieDetails}" var="garantieDetail">
															<c:if test="${garantieDetail.titre == codeSousGarantie}">
																<c:choose>
																	<c:when test="${not empty garantieDetail.textePriseEnCharge}">
																		${garantieDetail.textePriseEnCharge}&nbsp;
																		<c:if test="${not empty garantieDetail.textePriseEnChargeAide}">
																			<span class="bi bi-gray tt tt-aide-${codeGarantie}" data-name="aide-${codeGarantie}" data-type="text" data-text="${garantieDetail.textePriseEnChargeAide}"></span>
																		</c:if>
																	</c:when>
																	<c:otherwise>
																		<spring:message code="comparateurOffres.garantieOuService.na" />
																	</c:otherwise>
																</c:choose>
																<c:set var="detailTrouve" value="1" />
															</c:if>
														</c:forEach>
														<c:if test="${detailTrouve == 0}">
															<spring:message code="comparateurOffres.garantieOuService.na" />
														</c:if>
													</div>
												</c:forTokens>
											</c:if>
										</c:forTokens>
										
										<div class="garantie garantie-titre"></div>
										
										<!-- Services -->
										<c:forTokens items="${codesServicesSurComplementaires}" delims="," var="codeService">
											<div class="garantie garantie-detail">
												<c:choose>
													<c:when test="${not empty offre.services[codeService].textePriseEnCharge}">
														${offre.services[codeService].textePriseEnCharge}&nbsp;
														<c:if test="${not empty offre.services[codeService].textePriseEnChargeAide}">
															<span class="bi bi-gray tt tt-aide-${codeService}" data-name="aide-${codeService}" data-type="text" data-text="${offre.services[codeService].textePriseEnChargeAide}"></span>
														</c:if>
													</c:when>
													<c:otherwise>
														<spring:message code="comparateurOffres.garantieOuService.na" />
													</c:otherwise>
												</c:choose>
																							
											</div>
										</c:forTokens>

									</div>
									<!-- /.formula-garanties -->
									<div class="formula-link">
										<a href="${offre.lienDetails}" title="${detailOffreTexte}">${detailOffreTexte}</a>
									</div>
									<a class="button button-action button-compare" id="button-compare1" href="${offre.lienDevis}${offre.devisObligatoire ? '':'?code='.concat(offre.codeOffre)}">
										<span class="arrow"><i class="fa fa-chevron-right"></i><i class="fa fa-chevron-right"></i></span> <span class="text"><spring:message code="comparateurOffres.adherer" /></span>
									</a>
								</div>
							</div>
						</div>
					</c:forEach>										

				</div>
				<a class="left carousel-control" href="#carousel-modedetail" data-slide="prev" style="display: none;"><i class="glyphicon glyphicon-chevron-left"></i></a>
				<a class="right carousel-control" href="#carousel-modedetail" data-slide="next" style="display: none;"><i class="glyphicon glyphicon-chevron-right"></i></a>
			</div>
		</div>			
		
		<div class="hidden-xs col-sm-2">
			<a class="formula-add" href="#" title="<spring:message code="comparateurOffres.comparer.ajouterUneOffre" />"> <spring:message
					code="comparateurOffres.comparer.ajouterUneOffreAComparer" /><i class="fa fa-plus-circle"></i>
			</a>
		</div>
	</div>
</div>