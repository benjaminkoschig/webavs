<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1014" />
<c:set var="labelTitreEcran" value="JSP_REFERENCE_RUBRIQUE" />

<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	globazGlobal.referencesRubriquesViewService = '${viewBean.referencesRubriquesViewService}';
	globazGlobal.validationMessageErreur = '${viewBean.validationMessageErreur}';
	
	var zoneAjax;
	var etat = "init";
	
	function findReferencesRubriques(idCodeReference) {
		function callReferencesRubriques(idCodeReference) {
			var options = {
					serviceClassName:globazGlobal.referencesRubriquesViewService,
					serviceMethodName:'getForIdCodeReferenceSelectBlock',
					parametres:idCodeReference,
					callBack:function (data) {
						callBack(data);
					}
			};
			vulpeculaUtils.lancementService(options);
		}
		
		function callReferencesRubriquesWithoutCodeReference() {
				var options = {
						serviceClassName:globazGlobal.referencesRubriquesViewService,
						serviceMethodName:'getForIdCodeReferenceSelectBlockWithoutCodeReference',
						parametres:'unused',
						callBack:function (data) {
							callBack(data);
						}
				};
				vulpeculaUtils.lancementService(options);			
		}
		
		function callBack(data) {
			var $zoneCodeReference = $('#zoneCodeReference');
			$zoneCodeReference.empty();
			$zoneCodeReference.append(data);
			if(etat=="init"){
				$zoneCodeReference.find('select').attr('disabled','disabled');
			}
		}
		
		if(idCodeReference) {
			callReferencesRubriques(idCodeReference);
		} else {
			callReferencesRubriquesWithoutCodeReference();
		}
	}
	
	$(function () {
		defaultTableAjax.init({
			s_actionAjax: globazGlobal.ACTION_AJAX,
			s_spy : 'currentEntity.referenceRubriqueSimpleModel',
			init : function() {
				var that = this;
				zoneAjax = this;
				this.capage(20, [5, 10, 20, 30, 50, 100]);
				this.addSearch();
			},
			afterRetrieve : function(data) {
				this.t_element = this.defaultLoadData(data, '#'); 
				findReferencesRubriques(data.currentEntity.codeSystemSimpleModel.idCodeSystem);
			},
			getParametresForFind : function() {
				m_map = ajaxUtils.createMapForSendData(defaultTableAjax.optionsDefinit.$search, '#');
				m_map['currentEntity.referenceRubriqueSimpleModel.idCodeReference'] = $('#idCodeReference').val();
				return m_map;
			}
		});
		
		$('.areaSearch :input').change(function() {
			zoneAjax.ajaxFind();
		});
		
		$('.btnAjaxAdd').click(function() {
			etat = "add";
			findReferencesRubriques();
			$(".btnAjaxValidate2").show();
		});
		
		$('.btnAjaxUpdate').click(function() {
			etat = "upd";
			findReferencesRubriques();
			$(".btnAjaxValidate2").show();
		});
		
		$('.btnAjaxCancel').click(function() {
			etat = "init";
			findReferencesRubriques();
			$(".btnAjaxValidate2").hide();
		});
		
		$('.btnAjaxValidate2').click(function() {
			etat = "init";
			$('#zoneCodeReference').find('select').attr('disabled','disabled');
			if(validationInputs()){
				zoneAjax.validateEdition();	
			}
		});		
		
		function validationInputs(){
			var idRubrique = $('#currentEntity\\.referenceRubriqueSimpleModel\\.idRubrique').val();
			var codeReference = ""; 

			$('#zoneCodeReference').find('select').each(function() {
				codeReference = $(this).val();
		    });
				
			if(codeReference == '0' || codeReference.length<=0 || idRubrique === undefined || idRubrique.length<=0){
				showErrorDialog(globazGlobal.validationMessageErreur);
				
				etat = "erreur";
				$('#zoneCodeReference').find('select').attr('disabled','disabled');
				
				$('.btnAjaxCancel').trigger('click');
				
				return false;
			}else{
				return true;
			}
		};
		
		function showErrorDialog(errorStr) {
			var errorHTML = "";
			if(errorStr instanceof Array) {
				for(var i=0;i<errorStr.length;i++) {
					errorHTML += errorStr[i];
					errorHTML += '<br />';
				}
			} else {
				errorHTML = errorStr;
			}
			var errorObj = {text : errorHTML};
			showModalDialog('${pageContext.request.contextPath}/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
		}
		
	});
</script>

<%@ include file="/theme/detail_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/detail_ajax_el/bodyStart2.jspf"%>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
				<table>
					<tr>
						<td><ct:FWLabel key="JSP_RUBRIQUE"/></td>
						<td><input id="searchModel.likeIdExterne" type="text" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_REFERENCE_RUBRIQUE"/></td>
						<td><input id="searchModel.likeLibelle" type="text" /></td>
						
						<td><ct:FWLabel key="JSP_CODE_SYSTEM_REFERENCE_RUBRIQUE"/></td>
						<td><input id="searchModel.forReferenceRubrique" type="text" /></td>
					</tr>
				</table>
			</div>
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<th><ct:FWLabel key="JSP_RUBRIQUE"/></th>
						<th><ct:FWLabel key="JSP_REFERENCE_RUBRIQUE"/></th>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
					<table>
						<tr>
							<td><ct:FWLabel key="JSP_RUBRIQUE"/></td>
							<td>
							<input 
							id="currentEntity.rubriqueSimpleModel.idExterne"
							class="jadeAutocompleteAjax"
							type="text"
							data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.rubrique.RubriqueService¦,
												 method:¦find¦,
												 criterias:¦{'likeIdExterne':'Id externe'}¦,
												 lineFormatter:¦#{idExterne}¦,
												 modelReturnVariables:¦idRubrique,idExterne¦,nbReturn:¦20¦,
												 functionReturn:¦
												 	function(element){
												 		this.value=$(element).attr('idExterne');
												 		$('#currentEntity\\.referenceRubriqueSimpleModel\\.idRubrique').val($(element).attr('idRubrique'));	
												 	}¦
												 ,nbOfCharBeforeLaunch:¦3¦"
							/></td>
							<input type="hidden"  value="" id="currentEntity.referenceRubriqueSimpleModel.idRubrique"/>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_REFERENCE_RUBRIQUE"/></td>
							<td id="zoneCodeReference">
							</td>
						</tr>
					</table>
				</div>
				<div class="buttons">
					<div class="btnAjax">
						<ct:ifhasright element="${partialUserAction}" crud="u">
							<input class="btnAjaxUpdate" type="button" value="${btnUpdLabel}">
						</ct:ifhasright>
						<ct:ifhasright element="${partialUserAction}" crud="c">
							<input class="btnAjaxAdd" type="button" value="${btnNewLabel}">
						</ct:ifhasright>
						<input class="btnAjaxValidate2" type="button" value="${btnValLabel}">
						<input class="btnAjaxCancel" type="button" value="${btnCanLabel}">
					</div>
				</div>
			</div>
		</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>