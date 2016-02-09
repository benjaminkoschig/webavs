<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java"  contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PAP2004"/>
<c:set var="labelTitreEcran" value="JSP_PAP2004_TITRE_VALIDATION_PRESTATIONS_CALCULEES"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="btnValLabel" value="Valider" scope="page" />
<c:set var="btnUpdLabel" value="Corriger" scope="page" />


<c:set var="selectedIdValue" value="${viewBean.idDroit}" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

	<style type="text/css">
	.styleError{
		color: #ED1C24;
	}
	.styleNoError{
		color: #000;
	}
	</style>
	
<script type="text/javascript">
	
	var actionFinaliser = "${viewBean.actionFinalser}";
	var actionCorriger = "${viewBean.actionCorriger}";
	
	function init(){
	}
	
	// Valider
	function validate() {
		document.forms[0].elements('userAction').value = actionFinaliser;
		var breakRules ='';
	    var $inputs = $('.breakRuleCheckBox:checked');
	    var separator = '_';
	    $inputs.each(function() {
	        breakRules += this.name;
        	breakRules += separator;
	    });
		$('#breakRules').val(breakRules);
		return true;
	}   
	
	function cancel() {
	}  

	// Corriger
	function upd(){
		document.forms[0].elements('userAction').value = 'apg.droits.droitAPGP.afficher';
		document.forms[0].elements('_method').value = '';
	}
	
	function add () {
	}

	function del() {
	}
	
	function postInit(){
		$('#btnVal').addClass("inactive");
		var buttonCorriger = $('<button/>');
		$('#boutonValider').prop("disabled", true);
		$('#boutonCorriger').prop("disabled", false);
		$('#boutonSelectAll').prop("disabled", false);
	}
	
	function allCheckBoxSelected(){
		var result = true;
		$('.breakRuleCheckBox:checkbox').each(function () {
			if(!this.checked){
				result = false;
			}
		});
		if(result){
			$('#boutonValider').prop("disabled", false);
		}
		else {
			$('#boutonValider').prop("disabled", true);
		}
	}
	
	
	
	$(document).ready(function(){		
		$('#boutonValider').click(function(){
			document.forms[0].elements('userAction').value = actionFinaliser;
		    var separator = '_';
			
			var breakRules ='';
		    var $inputs = $('.breakRuleCheckBox:checked');
		    $inputs.each(function() {
		        breakRules += this.name;
	        	breakRules += separator;
		    });
			$('#breakRules').val(breakRules);
			
			var breakRulesGlobaz ='';
		    var $inputs2 = $('.breakRuleGlobazCheckBox:checked');
		    $inputs2.each(function() {
		    	breakRulesGlobaz += this.name;
		    	breakRulesGlobaz += separator;
		    });
			$('#breakRulesGlobaz').val(breakRulesGlobaz);
			
			
			
			
			action(COMMIT);
			//document.forms[0].submit();
		});
		$('#boutonCorriger').click(function(){
			document.forms[0].elements('userAction').value = actionCorriger;
			document.forms[0].elements('_method').value = '';
	  		document.forms[0].submit();
		});
		
		$('#boutonSelectAll').click(function(){
			$('.breakRuleCheckBox:checkbox').each(function () {
				if(!this.checked){
					this.checked = true;
				}
			});
			allCheckBoxSelected();
		});
		
		
		// Au départ on désactive le bouton valider
		$(".areaTable").on("click",".breakRuleCheckBox",function(){
			allCheckBoxSelected();
		})
		
	});
	
</script>
<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>

	<table id="plausi" cellspacing="0" cellpadding="0" style="background-color: #B3C4DB; width: 100%; height: 30px; ">
		<tr style="height:10;">
			<td>
				<b>
					<ct:FWLabel key="JSP_DETAIL_REQUERANT"/>
				</b>
			</td>
			<td>
				<input type="text" value="${viewBean.detailRequerant}" size="100" class="disabled" readonly/>
			</td>
		</tr>
		<tr style="height:10;">									
			<td>
				<b>
					<ct:FWLabel key="JSP_DATE_DEBUT"/>
				</b>
			</td>
			<td>
 				<input type="text" name="dateDebutDroit" value="${viewBean.dateDeDebutDroit}" class="date disabled" readonly />
				<input type="hidden" name="forIdDroit" value="${viewBean.idDroit}" />
				<input type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="${viewBean.idDroit}" />
			</td>
		</tr>
	</tbody>
</table>
<div style="height: 550px; overflow: auto; margin-top:5px;">
	<table class="areaTable" style="border: thin; border: blue;">
		<thead>
			<tr>
				<th style="width: 120px; font-size: 12px;"><strong>Id prestation</strong></th>
				<th style="width: 120px; font-size: 12px;"><strong>Date de début</strong></th>
				<th style="width: 120px; font-size: 12px;"><strong>Date de fin</strong></th>
				<th style="width: 80px; font-size: 12px;"><strong>Code</strong></th>
				<th style="width: 200px; font-size: 12px;"><strong>Plausibilité</strong></th>
				<th style="width: 120px; font-size: 12px;"><strong>Quittancer</strong></th>
			</tr>
		</thead>
		
		<c:forEach var="container" items="${viewBean.prestationValidees}">
		
			<tr style="background-color: #A3B8D3">
				<td ><strong>${container.prestation.id}</strong></td> 
				<td ><strong>${container.prestation.dateDebut}</strong></td> 
				<td ><strong>${container.prestation.dateFin}</strong></td> 
				<td></td>
				<td></td>
				<td></td>
			</tr>
				<c:forEach var="violatedRule" items="${container.validationErrors}">
				
					<c:choose>
						<c:when test="${violatedRule.fatalErrorThrown}">
							<c:set var="style" value="styleError" scope="page"></c:set>
						</c:when>
						<c:otherwise>
								<c:set var="style" value="styleNoError"></c:set>
						</c:otherwise>
					</c:choose>
				
				
					<tr class="${style}" >
						<td></td>
						<td></td>
						<td></td>
						<td>${violatedRule.errorCode}</td>
						<td>
							${violatedRule.errorMessage}
						</td>	
						<c:set var="cbId" scope="page" value="${container.prestation.id}.${violatedRule.errorCode}"/>
						<td style="text-align: center; ">
						<c:choose>
							<c:when test="${violatedRule.breakable}">
								<input id="${cbId}" name="${cbId}" type="checkbox" class="breakRuleCheckBox"/>&nbsp;
							</c:when>
							<c:otherwise>
								<input id="${cbId}" name="${cbId}" type="checkbox" class="breakRuleCheckBox" />*
							</c:otherwise>
						</c:choose>
						</td>
					</tr>
				</c:forEach>
				
			</c:forEach>	
				
			<c:forEach var="erreursValidationPeriode" items="${viewBean.erreursValidationPeriodes}">

				<c:set var="style" value="styleError" scope="page"></c:set>

				<c:set var="cbId" scope="page" value="${erreursValidationPeriode.periode.idPeriode}.1000"/>
			
				<tr class="${style}" >
					<td></td>
					<td>${erreursValidationPeriode.periode.dateDebutPeriode }</td>
					<td>${erreursValidationPeriode.periode.dateFinPeriode }</td>
					<td></td>
					<td>
						${erreursValidationPeriode.messageErreur}
					</td>	
					<td style="text-align: center; ">
						<span data-g-bubble="text:¦<ct:FWLabel key="JSP_PAP2004_MANQUE_JOURS_SOLDES"/>¦">
							<strong>&nbsp;</strong>
						</span>			
					</td>
				</tr>
			</c:forEach>
						

			
	</table>
	</div>	
	<input type="hidden" id="breakRules" name="breakRules" value="">
	<input type="hidden" id="breakRulesGlobaz" name="breakRulesGlobaz" value="">
	<span>
	* <ct:FWLabel key="JSP_PAP2004_ANNONCE_REVIENDRA"/>
	<input id="boutonSelectAll" name="boutonSelectAll" style="float: right; width: 150px" type="button" value='<ct:FWLabel key="JSP_PAP2004_SELECTIONNER_TOUT"/>' />
	</span>
	<span style="float: right;margin-top:50px;">
		<input id="boutonCorriger" name="boutonCorriger" type="button" value='<ct:FWLabel key="JSP_PAP2004_CORRIGER"/>' />
		<input id="boutonValider" name="boutonValider" type="button" value='<ct:FWLabel key="JSP_PAP2004_VALIDER"/>' />
	</span>


<%@ include file="/theme/detail_el/bodyButtons.jspf" %>			
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>
<%@ include file="/theme/detail_el/footer.jspf" %>