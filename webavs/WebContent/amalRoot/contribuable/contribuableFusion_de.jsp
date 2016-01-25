<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->



<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL FAMILLE INCLUSION -->

<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>

<%-- tpl:put name="zoneInit" --%>
<%
	//
	//Les labels de cette page commencent par le préfix "JSP_AM_DOSSIER_D"
	idEcran="AM0003";

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	boolean contribuableIsNew = false;
	boolean contribReprise = false;

	AMContribuableViewBean viewBean = (AMContribuableViewBean) session.getAttribute("viewBean");
	String idContrib = viewBean.getContribuable().getContribuable().getIdContribuable();
	
	String selectedTabId = request.getParameter("selectedTabId");
	if (JadeStringUtil.isBlankOrZero(selectedTabId)) {
		selectedTabId = "0";
	}
	
	String linkRetourContribuable = "amal?userAction=amal.contribuable.contribuable.afficher&selectedTabId="+selectedTabId+"&selectedId="+idContrib;
		
	String linkRetourContribuableLibelle = "Retour dossier";
	
	String detailLinkFamille = "amal?userAction=amal.famille.famille.afficher&selectedId=";
	String detailLinkDetailFamille = "amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
	
	//bButtonUpdate = false;
	bButtonDelete = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL FAMILLE INCLUSION -->
<!-- ************************************************************************************* -->







<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

var MAIN_URL="<%=formAction%>";
var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";

var actionMethod;
var userAction;


$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
	$('#btnVal').prop('disabled',true);
});


function init(){}

function add() {}

function upd() {			
}
function cancel() {
}
function validate() {
	userAction.value = ACTION_CONTRIBUABLE + ".fusionner";
	return true;
}

</script>
<%-- /tpl:put --%>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_AM_FU_D_TITLE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

<tr><td>
<%-- tpl:put name="zoneMain" --%>
				<table width="100%">
					<tr>
						<td align="left" style="vertical-align:top">
							<div class="conteneurContrib">	
								<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>											
							</div>
						</td>
					</tr>					
				</table>
				<br/>
				<TABLE id="zoneSelectionContrib" width="100%" style="border:1px solid black" style="background-color:#D7E4FF">
					<tr>
						<td>
						<table border="0">
							<tr>
								<td colspan="2">Contribuable à récupérer pour la fusion :
							</tr>
							<tr>
								<td width="220px">
									<input	type="text"
						data-g-autocomplete="	service:ch.globaz.amal.business.services.models.contribuable.ContribuableService,
			                                                method:¦searchFusion¦,
			                                                criterias:¦{
			                                                	forIdContribuable: 'Id contribuable',
			                                                    forNoContribuable: 'No contribuable'
			                                                }¦,			                            
			                                                constCriterias:¦
			                                                	forIdContribuableDifferent='<%=viewBean.getContribuable().getContribuable().getIdContribuable()%>'
			                                                ¦,                    
			                                                lineFormatter:¦#{contribuable.id} #{personneEtendue.tiers.designation1} #{personneEtendue.tiers.designation2} #{personneEtendue.personneEtendue.numAvsActuel} #{personneEtendue.personneEtendue.numContribuableActuel}¦,
			                                                modelReturnVariables:¦contribuable.id,contribuable.idTier,personneEtendue.tiers.designation1,personneEtendue.tiers.designation2,personneEtendue.personneEtendue.numAvsActuel,personneEtendue.personneEtendue.numContribuableActuel¦,
			                                                functionReturn:¦
			                                                    function(element){
			                                                    	$('#idContri').html($(element).attr('contribuable.id'));
			                                                    	$('#numContri').html($(element).attr('personneEtendue.personneEtendue.numContribuableActuel'));
			                                                    	$('#numAVS').html($(element).attr('personneEtendue.personneEtendue.numAvsActuel'));
			                                                    	$('#nom').html($(element).attr('personneEtendue.tiers.designation1'));
			                                                    	$('#prenom').html($(element).attr('personneEtendue.tiers.designation2'));
			                                                    	$('#idDossierAFusionner').val($(element).attr('contribuable.id'));
			                                                    	$('#idTiersDossierAFusionner').val($(element).attr('contribuable.idTier'));
			                                                    	if ($('#idTiersDossierBase').val() != $('#idTiersDossierAFusionner').val()) {
			                                                    		$('#btnVal').prop('disabled',true);
			                                                    		$('#warningIdTiersDifferents').show();
			                                                    	} else {
			                                                    		$('#btnVal').prop('disabled',false);
			                                                    		$('#warningIdTiersDifferents').hide();
			                                                    	}
			                                                    }
			                                                ¦" type="text">	  
			                                                <input type="hidden" name="idDossierAFusionner" id="idDossierAFusionner" value="" />	                          
			                                                <input type="hidden" name="idTiersDossierAFusionner" id="idTiersDossierAFusionner" value="" />
			                              					<input type="hidden" name="idTiersDossierBase" id="idTiersDossierBase" value="<%=viewBean.getContribuable().getFamille().getIdTier()%>" />
			                		</div>
		                		</td>
		                		<td width="150px" style="font-weight: bold;">
			                       Id contribuable			                       
		                		</td>
		                		<td id="idContri">&nbsp;</td>
		                		<td width="20px">&nbsp;</td>
		                		<td rowspan="5">
		                			<span id="warningIdTiersDifferents" style="display: none;">
		                				<img src="<%=request.getContextPath()%>/images/amal/status_unknown.png" />
		                				Les tiers ne sont pas les mêmes. Fusion impossible !
		                			</span>
		                		</td>
	                		</tr>
	                		<tr>
	                			<td>
	                				&nbsp;
	                			</td>
	                			<td style="font-weight: bold;">
	                				N° contribuable
	                			</td>
	                			<td id="numContri">&nbsp;</td>
	                			<td>&nbsp;</td>
	                		</tr>
	                		<tr>
	                			<td>
	                				&nbsp;
	                			</td>
	                			<td style="font-weight: bold;">
	                				N° AVS
	                			</td>
	                			<td id="numAVS">&nbsp;</td>
	                			<td>&nbsp;</td>
	                		</tr>
	                		<tr>
	                			<td>
	                				&nbsp;
	                			</td>
	                			<td style="font-weight: bold;">
	                				Nom
	                			</td>
	                			<td id="nom">&nbsp;</td>
	                			<td>&nbsp;</td>
	                		</tr>
	                		<tr>
	                			<td>
	                				&nbsp;
	                			</td>
	                			<td style="font-weight: bold;">
	                				Prénom
	                			</td>
	                			<td id="prenom">&nbsp;</td>
	                			<td>&nbsp;</td>
	                		</tr>
	                	</table>
						</td>
					</tr>
				</TABLE>
				<br>
				
<%-- /tpl:insert --%>

<!-- FIN ZONE PRINCIPALE TITLE AND BODY -->
<!-- ************************************************************************************* -->


<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />

<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>

<!-- ************************************************************************************* -->
<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->

