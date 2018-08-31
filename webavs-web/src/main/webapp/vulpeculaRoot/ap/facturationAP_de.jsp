<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT7001"/>
<c:set var="labelTitreEcran" value="JSP_FACTURATION_AP"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="tableHeight" value="auto" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.messageConfirmDialog = "${viewBean.messageConfirmDialog}";

//fonctions de bases à redéfinir
var messageProcessusLance = '${viewBean.messageProcessusLance}';
var messageAnneeManquante = '${viewBean.messageAnneeManquante}';
var messagePassageManquante = '${viewBean.messagePassageManquante}';

function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init() {
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {	
	var $launchFacturaion = $("#launchFacturation");
	$launchFacturaion.click(function () {
		$launchFacturaion.prop('disabled', true);
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.ap.facturationAP.executer";
			document.forms[0].submit();
		} else {
			$launchFacturaion.removeAttr('disabled');
		}

	});
});

// Reset Associations values if employeur get a new value
$(function () {	
	var $idEmployeur = $("#idEmployeur");
	var $widgetEmployeur = $("#widgetEmployeur");
	var $libelleAssociation = $("#libelleAssociation");
	var $idAssociationProfessionnelle = $("#idAssociationProfessionnelle");
	$widgetEmployeur.blur(function () {
		if($idEmployeur.length != 0 && $idEmployeur[0].value.length != 0){
			$('#idAssociationProfessionnelle')[0].value = "";	
			$('#libelleAssociation')[0].value = "";
		}		
	});
});

//Reset employeur values if  Associations get a new value
$(function () {
	var $idEmployeur = $("#idEmployeur");
	var $widgetEmployeur = $("#widgetEmployeur");
	var $libelleAssociation = $("#libelleAssociation");
	var $idAssociationProfessionnelle = $("#idAssociationProfessionnelle");
	$libelleAssociation.blur(function () {
		if($idAssociationProfessionnelle.length != 0 && $idAssociationProfessionnelle[0].value.length != 0){
			$('#idEmployeur')[0].value = "";	
			$('#widgetEmployeur')[0].value = "";
		}
		
	});
});

globazGlobal.checkFields = function () {
	var idEmployeur = $('#idEmployeur').val();
	var idAssociationProfessionnelle = $('#idAssociationProfessionnelle').val();
	var idPassage = $('#idPassage').val();
	
	if($("#annee").val().length <= 0){
		showErrorDialog(messageAnneeManquante);
		return false;
	}
	if(idPassage.length==0){
		showErrorDialog(messagePassageManquante);
		return false;
	}
	
	if(idEmployeur.length==0 && idAssociationProfessionnelle.length==0) {
		if(!window.confirm(globazGlobal.messageConfirmDialog)) {
			return false;
		}
	}
	
	return true;
};

</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="position: absolute; right:0px; width: 30%">
</div>
<div style="width: 100%;text-align: center;">
	<div>
		<table>
			<tr>
				<td>
					<label for="email">
						<ct:FWLabel key="JSP_EMAIL"/>
					</label>
				</td>
				<td>
					<input id="email" style="width:200px;" type="text" data-g-string="mandatory:true" name="email" value="${viewBean.email}" />
				</td>
			</tr>	
			<tr>
				<td>
					<label for="employeur">
						<ct:FWLabel key="JSP_EMPLOYEUR"/>
					</label>
				</td>
				<td>
					<input type="hidden" id="idEmployeur" name="idEmployeur" value="${viewBean.idEmployeur}" />
					<ct:widget id='widgetEmployeur'
		   				name = 'designationEmployeur'
		   				style="width:600px;"
		   				styleClass="widgetAdmin"
		   				notation="data-g-string='mandatory:false' value='${viewBean.designationEmployeur}'">
						<ct:widgetService defaultLaunchSize="1" methodName="search" className="${viewBean.employeurViewService}">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="JSP_NO_AFFILIE"/>
							<ct:widgetCriteria criteria="likeDesignationTiersUpper" label="JSP_RAISON_SOCIALE"/>
							<ct:widgetLineFormatter format="#{affiliationTiersComplexModel.affiliation.affilieNumero} - #{affiliationTiersComplexModel.affiliation.raisonSociale}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										this.value=$(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero')+', '+$(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
										$('#idEmployeur').val($(element).attr('affiliationTiersComplexModel.affiliation.affiliationId'));
									}
								</script>
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				</td>				
			</tr>
			
			<tr>
				<td>
					<ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE_CPP"/>
				</td>
				<td>
					<!-- GenreAdministration
						68900004 : Association Professionnelle
						68900007 : Association CPP -->
					<input type="hidden" name="idAssociationProfessionnelle" id="idAssociationProfessionnelle" value="${viewBean.idAssociationProfessionnelle}" />
					<input id="libelleAssociation"
							name="designationAssociationProfessionnelle"
							class="jadeAutocompleteAjax"
							value = "${viewBean.designationAssociationProfessionnelle}"
							type="text"
							style="width:600px;"
							data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.administration.AdministrationService¦,
												 method:¦find¦,
												 criterias:¦{'forCodeAdministrationLike':'Code','forDesignationLike':'Désignation'}¦,
												 constCriterias:¦forGenreAdministration1=68900004,forGenreAdministration2=68900007¦,
												 lineFormatter:¦<b>#{admin.codeAdministration}</b><br />#{tiers.designation1} #{tiers.designation2}¦,
												 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,admin.codeAdministration¦,nbReturn:¦20¦,
												 functionReturn:¦
												 	function(element){
												 		this.value=$(element).attr('admin.codeAdministration') + ' - ' + $(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												 		$('#idAssociationProfessionnelle').val($(element).attr('tiers.id'))
												 	}¦
												 ,nbOfCharBeforeLaunch:¦1¦"/>
				</td>
			</tr>
			<tr>
				<td>
					<ct:FWLabel key="JSP_ANNEE"/>
				</td>
				<td>
					<input name="annee" value="${viewBean.annee}" id="annee" data-g-integer="mandatory:true, sizeMax:5, autoFit:true" maxlength="4" />
				</td>
			</tr>
		 	<tr> 
	            <td>
	            	<ct:FWLabel key="JSP_PASSAGE"/>
            	</td>
	            <td> 
	              <input type="hidden" value="902001" name="status" />
	              <input type="hidden" value="11" name="plan" />
	              
	              <input type="text" id="idPassage" data-g-string="mandatory:true" name="idPassage" maxlength="15" size="15" style="text-align:right;" value="${viewBean.idPassage}">
	              <%
	              	Object[] psgMethodsName = new Object[]{
						new String[]{"setIdPassage","getIdPassage"},
						new String[]{"setLibellePassage","getLibelle"},
						};
					Object[] psgParams= new Object[]{
					        new String[]{"status","statusToFilter"},
					        new String[]{"plan","planToFilter"}
					};
					String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/ap/facturationAP_de.jsp";	
				%>
				<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
		            <ct:FWSelectorTag name="passageSelector" 
						methods="<%=psgMethodsName%>"
						providerPrefix="FA"			
						providerApplication ="musca"			
						providerAction ="musca.facturation.passage.chercher"			
						providerActionParams ="<%=psgParams%>"
						redirectUrl="<%=redirectUrl%>"/>
				</ct:ifhasright>
				<input type="hidden" name="selectorName" value="">
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
			 		<input type="text" name="libellePassage" class="libelleLongDisabled" value="${viewBean.libellePassage}" readonly>
		 		</td>
			<tr>
				<td>
					<ct:FWLabel key="JSP_AP_ECRASER_FACTURES_EXISTANTES"/>
				</td>
				<td>
				<c:if test="${viewBean.replaceFacture}">
					<input name="replaceFacture" checked id="replaceFacture" type="checkbox" />
				</c:if>
				<c:if test="${!viewBean.replaceFacture}">
					<input name="replaceFacture" id="replaceFacture" type="checkbox" />
				</c:if>
					
				</td>
			</tr>			
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<c:if test="${!processLaunched}">
						<input id="launchFacturation" type="button" name="launchFacturation" value='<ct:FWLabel key="JSP_LANCER"/>'/>
					</c:if>
				</td>
			</tr>
		</table>
	</div>
</div>

<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED"/>
	</div>
</c:if>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>
<%@ include file="/theme/detail_el/footer.jspf" %>
