<%@ include file="/WEB-INF/jsp/init.jsp" %>

<div id="<portlet:namespace />nouveau-transfert-doc" class="row page-transfert">
	<div class="col-xs-12">
		<h2 class="titre-bandeau">Importer des avis</h2>
		<portlet:actionURL var="fileUploadURL">
		         <portlet:param name="formAction" value="fileUpload" />      
		</portlet:actionURL>
		<form:form name="fileUploader" commandName="springFileVO" method="post"
                action="${fileUploadURL}"  enctype="multipart/form-data">
			<c:if test="${not empty springFileVO.successMessage}">
            	<div class="alert alert-success"><c:out value="${springFileVO.successMessage}" /></div>
            </c:if>
            <c:if test="${not empty springFileVO.errorMessage}">
            	<div class="alert alert-danger"><c:out value="${springFileVO.errorMessage}" /></div>
            </c:if>
			<div class="col-xs-12">
				<label>Sélectionner le fichier à importer : </label>
 				<form:input path="fileData" type="file"/>
           		<div class="submit-button-group">
					<button type="submit" class="btn_suivant btn btn-submit boutons_valider" style="background-color: rgb(248, 165, 35);color: rgb(255, 255, 255);border-color: transparent;">Importer</button>
				</div>
			</div>
      </form:form>
	</div>
</div>