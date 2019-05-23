<%@ include file="init.jsp"%>

<portlet:actionURL var="submitEditURL" name="submitEdit">
</portlet:actionURL>

<spring:message var="gammeLabel" code="comparateurOffres.edit.gamme.label" />
<spring:message var="gammeTitre" code="comparateurOffres.edit.gamme.titre" />
<spring:message var="gammePlaceholder" code="comparateurOffres.edit.gamme.placeholder" />
<spring:message var="gammeTexte" code="comparateurOffres.edit.gamme.texte" />
<spring:message var="titreErrorView" code="comparateurOffres.edit.view.error.titre" />
<spring:message var="texteErrorView" code="comparateurOffres.edit.view.error.texte" />
<spring:message var="titreSave" code="comparateurOffres.edit.save.titre" />
<spring:message var="texteSave" code="comparateurOffres.edit.save.texte" />
<spring:message var="titreErrorSave" code="comparateurOffres.edit.save.error.titre" />
<spring:message var="texteErrorSave" code="comparateurOffres.edit.save.error.texte" />

<spring:message var="compEtSurcompLabel" code="comparateurOffres.gamme.compEtSurcomp.label" />
<c:set var="compEtSurcompCodeGamme" value="<%=CodeGammeConstants.COMPLEMENTAIRE_ET_SURCOMPLEMENTAIRE%>"/>

<h3>
	<spring:message code="comparateurOffres.edit.titre" />
</h3>

<form:form action="${submitEditURL}" modelAttribute="editForm"
	method="POST" class="form-horizontal" id="${portletnamespace}form-edit"
	novalidate="novalidate">

	<fieldset>
		<div class="row">
			<div class="col-xs-12 col-sm-6">
				<spring:bind path="codeGamme">
					<div class="form-group row">
						<label class="control-label col-xs-12 col-sm-5"
							for="<portlet:namespace/>codeGamme">${gammeLabel}&nbsp;<span
							class="text-danger">*</span>&nbsp;&nbsp;
						</label>
						<div class="col-xs-12 col-sm-7">
							<select id="<portlet:namespace/>codeGamme"
								name="<portlet:namespace/>codeGamme" title="${gammeTitre}"
								class="form-control valid">
								<option value="">${gammePlaceholder}</option>
								<c:forEach var="gamme" items="${gammes}">
									<option value="${gamme.codeGamme}"
										${gamme.codeGamme eq editForm.codeGamme ? 'selected' : '' }>${gamme.libelleGamme}</option>
								</c:forEach>
								<option value="${compEtSurcompCodeGamme}" ${compEtSurcompCodeGamme eq editForm.codeGamme ? 'selected' : '' }>${compEtSurcompLabel}</option>
							</select>
						</div>
					</div>
				</spring:bind>
			</div>
		</div>
	</fieldset>
	<div class="row">
		<div class="col-xs-12 text-right">
			<button disabled id="<portlet:namespace/>button-validate-edit"
				class="button button-action" type="submit">
				<span class="arrow arrow-ff"><i class="fa fa-chevron-right"></i><i
					class="fa fa-chevron-right"></i></span> <span class="text"><spring:message
						code="comparateurOffres.edit.submit" /></span>
			</button>
		</div>
	</div>

</form:form>
<script type="text/javascript">
    // Execute immediate
    (function() {

        $('#<portlet:namespace/>form-edit').validate(
                {
                    rules : {
                        "<portlet:namespace/>codeGamme" : {
                            required : true
                        }
                    },
                    onfocusout : function(element) {
                        $(element).valid();
                    },
                    highlight : function(element) {
                        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                        $(element).closest('.form-group').find('.glyphicon').removeClass('glyphicon-ok').addClass(
                                'glyphicon-remove');
                        $(element).closest('.form-group').find('.tt').show().addClass('tt-error');
                    },
                    success : function(element) {
                        $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                        $(element).closest('.form-group').find('.glyphicon').removeClass('glyphicon-remove').addClass(
                                'glyphicon-ok');
                        $(element).closest('.form-group').find('.tt').show().removeClass('tt-error');
                    }
                });

        if ($('#<portlet:namespace/>form-edit').validate().checkForm()) {
            $('#<portlet:namespace/>form-edit').valid();
            $('#<portlet:namespace/>button-validate-edit').prop("disabled", false);
        } else
            $('#<portlet:namespace/>button-validate-edit').prop("disabled", true);

        $('#<portlet:namespace/>form-edit :input').on('change', function() {
            if ($('#<portlet:namespace/>form-edit').validate().checkForm())
                $('#<portlet:namespace/>button-validate-edit').prop("disabled", false);
            else
                $('#<portlet:namespace/>button-validate-edit').prop("disabled", true);
        });

        <c:if test="${not empty param['afterSave']}">
        $.notify({
            title : "${titreSave}",
            message : "${texteSave}"
        }, {
            type : 'success'
        });
        </c:if>

        <c:if test="${not empty param['editFormSaveError']}">
        $.notify({
            title : "${titreErrorSave}",
            message : "${texteErrorSave}"
        }, {
            type : 'error'
        });
        </c:if>

        <c:if test="${not empty editFormError && editFormError eq 'true'}">
        $.notify({
            title : "${titreErrorView}",
            message : "${texteErrorView}"
        }, {
            type : 'error'
        });
        </c:if>

    })();
</script>

