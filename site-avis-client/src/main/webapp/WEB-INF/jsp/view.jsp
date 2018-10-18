<%@ include file="init.jsp"%>

<div class="container-fluid liste-avis-interiale"> 
	<div class="row"> 
		<div class="leftbar">

			<liferay-ui:search-container total="${total}" delta="${delta}">
			
				<liferay-ui:search-container-results results="${listeAvis}" />
				<liferay-ui:search-container-row
					className="fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO"
					escapedModel="<%= true %>"
					modelVar="commentaire" indexVar="index">
	
	
					<div class="col-xs-12 col-avis-interiale"> 
						<div class="col-xs-2 no-padding-rl"> 
							<div class="box2">
								<c:if test="${(index+1) % 3 == 0 }">
									<img class="img" src="<%= request.getContextPath() %>/images/bulle_verte.png" style="padding-left: 30px;padding-top: 30px;">
								</c:if>
								<c:if test="${(index+3) % 3 == 0 }">
									<img class="img" src="<%= request.getContextPath() %>/images/bulle_orange.png" style="padding-left: 30px;padding-top: 30px;">
								</c:if>
								<c:if test="${((index+3) % 3 != 0) &&  ((index+1) % 3 != 0)}">
									<img class="img" src="<%= request.getContextPath() %>/images/bulle_bleue.png" style="padding-left: 30px;padding-top: 30px;">
								</c:if>
							</div> 
						</div>
						
						<div class="col-xs-10 avis-zone">  
							<div class="opened-quotes"> 
								<img class="img" src="<%= request.getContextPath() %>/images/u5.png"> 
							</div>
							<div class="paragraph">  
								<div> 
									<p>
										<span>
											${commentaire.commentaire}
										</span>
									</p> 
								</div> 
							</div>
							<div class="closed-quotes"> 
								<img class="img" src="<%= request.getContextPath() %>/images/u6.png"> 
							</div>
						</div>
					</div>
				
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
