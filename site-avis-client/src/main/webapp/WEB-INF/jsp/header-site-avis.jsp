<%@ include file="init.jsp"%>

<div class="container-fluid" style="background-color:#0052A0; max-width=">
	<div class="row">
		<div class="row">
			<div class="col-xs-4 hidden-xs">
				<img src="https://www.interiale.fr/documents/2373901/17688247/bandeau-famille-ITE2.png/6e415ce5-7a40-45f1-8982-b49fb63ab37c?t=1513609697539" />
			</div>
			
			<div align="center" class="col-xs-4 note-globale-header hidden-xs"> 
				<h2 style="color:#ffffff;">Note globale</h2>
				<div align="center">
					<c:forEach begin="1" end="${nbEtoilesJaunes}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_jaune2.png" width="20px" /> 
					</c:forEach>
					<c:forEach begin="1" end="${nbEtoilesDemi}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_demi.png" width="20px" />
					</c:forEach>
					<c:forEach begin="1" end="${nbEtoilesBlanches}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_blanche.png" width="20px" />
					</c:forEach>
				</div>
			</div>
			
			<div align="center" class="col-xs-4 hidden-xs">
				<div align="center" itemscope="" itemtype="http://schema.org/Product">
					<span itemprop="name" style="color:#0052A0">Interiale</span>
					<div itemprop="aggregateRating" itemscope="" itemtype="http://schema.org/AggregateRating">
						<h1 style="color:#ffffff;font-size:30px;">
							<span itemprop="ratingValue">${moyenne}/5</span>
						</h1>
						<span itemprop="reviewCount" style="font-size:24px; color:#c4c4c4;">sur ${nbAvisTotal} avis</span>
					</div>
				</div>
			</div>
			
			<div align="center" class="col-lg-4 note-globale-header visible-xs"> 
				<h2 style="color:#ffffff;">Note globale</h2>
				<div align="center">
					<c:forEach begin="1" end="${nbEtoilesJaunes}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_jaune2.png" width="20px" /> 
					</c:forEach>
					<c:forEach begin="1" end="${nbEtoilesDemi}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_demi.png" width="20px" />
					</c:forEach>
					<c:forEach begin="1" end="${nbEtoilesBlanches}" varStatus="loop">
						<img src="<%= request.getContextPath() %>/images/etoile_blanche.png" width="20px" />
					</c:forEach>
				</div>
			</div>
			
			<div align="center" class="col-lg-4 visible-xs">
				<div align="center" itemscope="" itemtype="http://schema.org/Product">
					<span itemprop="name" style="color:#0052A0">Interiale</span>
					<div itemprop="aggregateRating" itemscope="" itemtype="http://schema.org/AggregateRating">
						<h1 style="color:#ffffff;font-size:30px;">
							<span itemprop="ratingValue">${moyenne}/5</span>
						</h1>
						<span itemprop="reviewCount" style="font-size:24px; color:#c4c4c4;">sur ${nbAvisTotal} avis</span>
					</div>
				</div>
			</div>
			
			<div class="visible-xs" style="height:18px;">&nbsp;</div>
			
		</div>
	</div>
</div>