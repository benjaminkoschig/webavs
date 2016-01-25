<%@page import="ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleAddCheckMessage"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.List"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.perseus.utils.PFAgenceCommunaleHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.CSVariableMetier"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%@page import="globaz.perseus.vb.demande.PFDemandeViewBean"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>

<%
PFDemandeViewBean viewBean = (PFDemandeViewBean) session.getAttribute("viewBean"); 
idEcran="PPF0311";
autoShowErrorPopup = true;

boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

//Utiliser pour vider quelques champs lors de la copie
boolean afterCopie = !JadeStringUtil.isEmpty(request.getParameter("afterCopie"));
if (afterCopie) {
	viewBean.getDemande().getSimpleDemande().setDateDebut("");
	viewBean.getDemande().getSimpleDemande().setDateFin("");
	viewBean.getDemande().getSimpleDemande().setDateDepot("");
	viewBean.getDemande().getSimpleDemande().setTypeDemande("");
	bButtonCancel = false;
}

PersonneEtendueComplexModel personne = viewBean.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
String affichePersonnne = "";
String idDossier = request.getParameter("idDossier");

affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
bButtonCancel = false;

if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) {
	bButtonUpdate = false;
	bButtonDelete = false;
	
	if (JadeStringUtil.isEmpty(viewBean.getDemande().getSimpleDemande().getDateFin())&& objSession.hasRight("perseus", FWSecureConstants.UPDATE)) {
		bButtonUpdate = true;
	}
}

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

	<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options" />
	<ct:menuChange displayId="options" menuId="perseus-optionsdemandes">
	    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getDemande().getSituationFamiliale().getId()%>"/>
		<ct:menuSetAllParams key="idDemande" value="<%=viewBean.getDemande().getId() %>"/>
		<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getDemande().getDossier().getId() %>"/>
	<% if (!viewBeanIsNew) { %>
		<% if (CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			<ct:menuActivateNode active="no" nodeId="VOIRPCFA"/>
		<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="VOIRPCFA"/>
		<% } %>
		<% if (CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande()) || CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			<ct:menuActivateNode active="no" nodeId="DECISIONS_DEMANDE"/>
		<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="DECISIONS_DEMANDE"/>
		<% } %>
		<% if (CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande()) && !CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) { %>
			<ct:menuActivateNode active="yes" nodeId="DEMANDE_PC_AVS_AI"/>
		<% } else { %>
			<ct:menuActivateNode active="no" nodeId="DEMANDE_PC_AVS_AI"/>
		<% } %>
		<% if ((CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) || viewBean.getDemande().getSimpleDemande().getCalculable()) && !viewBean.getDemande().getSimpleDemande().getRefusForce() && !viewBean.getDemande().getSimpleDemande().getNonEntreeEnMatiere() || CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande()) || CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			<ct:menuActivateNode active="no" nodeId="DECSANSCALCUL"/>
		<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="DECSANSCALCUL"/>
		<% } %>
		<% if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) || !viewBean.getDemande().getSimpleDemande().getCalculable() || viewBean.getDemande().getSimpleDemande().getRefusForce() || viewBean.getDemande().getSimpleDemande().getNonEntreeEnMatiere() || CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			<ct:menuActivateNode active="no" nodeId="CALCULERPCFA"/>
		<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="CALCULERPCFA"/>
		<% } %>
		<% if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) { %>
			<ct:menuActivateNode active="yes" nodeId="OUVERTUREQD"/>
		<% } else { %>
			<ct:menuActivateNode active="no" nodeId="OUVERTUREQD"/>
		<% } %>
		<% if (!viewBean.getDemande().getSimpleDemande().getCalculable() || viewBean.getDemande().getSimpleDemande().getRefusForce() || viewBean.getDemande().getSimpleDemande().getNonEntreeEnMatiere() || CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande()) || CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getTypeDemande())) { %>
			<ct:menuActivateNode active="no" nodeId="CREANCIER"/>
		<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="CREANCIER"/>
		<% } %>
	<% } %>
	</ct:menuChange>


<script type="text/javascript">
	
	//var nbAnneesMinimumCanton = <%=objSession.getCode(CSVariableMetier.NB_ANNEES_MIN_DANS_CANTON.getCodeSystem()) %>;
	
	//var warnCopieFamilleEnfant  = '<%= viewBean.getEnfantFamilleWarnMessages()%>';
	//var warnCopieFamilleEnfantFromSession;
	
	
	
	
	var nbAnneesMinimumCanton = 3;
	var ACTION_DEMANDE="<%=IPFActions.ACTION_DEMANDE%>";
	var actionMethod;
	var userAction;
	var globazGloball = {};
	var checkEnfant = false;
	var ajaxCheckEnfantObj = <%= viewBean.toJson()%>;
	var validateMouseDown = false;
	var $infoEnfantCheckmsg;
	var $infoEnfantCheckmsgWrapper;
	var $imgLoaderCheckEnfant = $('<img class="loaderAjax" src="images/ajax-loader.gif" width="20"/>');
	
	
	globazGloball.popup_continuer_button = <%=objSession.getLabel("AJOUT_ENFANT_POPUP_CONTINUER")%>;
	
	$(function(){
		
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
	
		//evenements onblur sur les date d efin et debut --> check de la coherence des enfants sur update uniquement (copie demande)
		$('.datesValues').blur(function () {
			//event.stopPropagation();
			
			if(!validateMouseDown){
				var dateDebut = $('#dateDebut').val();
				var dateFin = $('#dateFin').val();
				
				
				if(actionMethod === 'upd' && dateDebut !== ''){
					ajaxCheckEnfantObj.simpleDemande.dateDebut = "01." + $('#dateDebut').val();
					
					if(dateFin !== ''){
						ajaxCheckEnfantObj.simpleDemande.dateFin = "01." + $('#dateFin').val();
					}
					
					
					hideInfoMsgEnfant();
					
					checkCoherenceEnfantFamille($(this));
					
					
				}
			}else{
				validateMouseDown = true;
			}
			
			
			
		});
		
		
		
		$('#btnValidate').on('click', function () {
			validate();
		});
		
		$('#btnValidate').mousedown(function(event){
			validateMouseDown = true;
		});
		
		
		// attribue une id à tous les champs ayant un nom mais pas encore d'id
		$('*',document.forms[0]).each(function(){
			if(this.name!=null && this.id==""){
				this.id=this.name;
			}
		});
		
		<% if(CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) && "_fail".equals(request.getParameter("valid"))) { %>
			readOnly(true);
			$("#dateFin")[0].disabled = false;
		<% } %>
		
			
		checker();
		$('#nonEntreeEnMatiere').change(function(){
			checker();
		});
		$('#refusForce').change(function(){
			checker();
		});
	});
	
	jsManager.addAfter(function () {
		
		$infoEnfantCheckmsgWrapper = $('#infoEnfantCheckmsgWrapper');
		$infoEnfantCheckmsg = $('#infoEnfantCheckmsg');
		
		
		
		hideInfoMsgEnfant();
		
		
		
		
	}, "execution après l'init de la notation");
	
	
	
	
	//fonction qui cache la zone d'information check enfant
	var hideInfoMsgEnfant = function ()  {
		$infoEnfantCheckmsgWrapper.hide();
	};
	
	//fonction qui affiche le message d'information check enfant
	var showInfoMsgEnfant = function (msg) {
		
		$infoEnfantCheckmsg.children('.contentMessage').children().children().remove();
		$infoEnfantCheckmsg.children('.contentMessage').children().append('<p>'+msg+'</p>');
		$infoEnfantCheckmsgWrapper.show();
	};
	
	
	function validate() {
		
	    state = true;
	    if (actionMethod == "add"){
	    	userAction.value=ACTION_DEMANDE+".ajouter";
	    }else{
	    	userAction.value=ACTION_DEMANDE+".modifier";	
	    	
	    }
	    
	    if(state){
	    	action(COMMIT)
	    }
	    
	}    
	
	function add() {}
	function upd() {
		//Si il faut n'activer que le champ dateFin pour la modif
		<% if (CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande())) { %>
			readOnly(true);
			$("#dateFin")[0].disabled = false;
		<% } %> 
	}
	
	function annuler() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DEM_D_ANNULER_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.demande.annulerDemande.afficher";
	        document.forms[0].elements('_method').value="upd";
	        document.forms[0].submit();
	    }
	}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.demande.demande.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
			document.forms[0].elements('userAction').value="perseus.demande.demande.chercher";
	}
	
	function init() {}
	
	function postInit() {
		<% if(CSEtatDemande.VALIDE.getCodeSystem().equals(viewBean.getDemande().getSimpleDemande().getCsEtatDemande()) && "_fail".equals(request.getParameter("valid"))) { %>
			readOnly(true);
			$("#dateFin")[0].disabled = false;
		<% } %>
	}
	
	function checkArriveeCanton() {
		if ($('#dateArriveeCanton').val()) {
			
		}
	}

	function checker() {
			$refusForce = $('#refusForce');
			$nonEntreeEnMatiere = $('#nonEntreeEnMatiere');
			$sectionNonEntreeEnMatiere = $('#sectionNonEntreeEnMatiere');
			$sectionRefusForce = $('#sectionRefusForce');
			
			if($refusForce.is(':checked')){
				$sectionNonEntreeEnMatiere.attr("disabled", true);
			}else{
				$sectionNonEntreeEnMatiere.attr("disabled", false);
			}
			
			if($nonEntreeEnMatiere.is(':checked')){
				$sectionRefusForce.attr("disabled", true);
			}else{
				$sectionRefusForce.attr("disabled", false);
			}
	}
	
	//Fonction qui effectue un appel ajax vers le checker afin de déterminer la cohérence de la famille
	function checkCoherenceEnfantFamille ($inputTarget) {
		
		
		//loader gif
		$inputTarget.after($imgLoaderCheckEnfant);
		
		var ajaxParams = {
		        userAction: "widget.action.jade.afficher",
		        serviceClassName: "ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService",
		        serviceMethodName: "checkEnfantSituationFamillialeCoherence",
		        initThreadContext: false,
		        cursor: 5,	
		        parametres: ajaxUtils.jsonToString(ajaxCheckEnfantObj),
		        forceParametres : true,
		        noCache: globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds()
		    };
		
		
		$.ajax({
            url: globazNotation.utils.getFromAction(),
            async: true,
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: ajaxParams,
            success: function (data) {

            	$('.loaderAjax').remove();
            	
            	var msg = data.viewBean.returnInfosService.message;
            	
            	if(msg !== 'ok'){
            		showInfoMsgEnfant(msg);
            	}
            	
            	
            	
            },
            error: function (jqXHR, textStatus, errorThrown) {
            	$inputTarget.remove($imgLoaderCheckEnfant);
            	globazNotation.utils.consoleError("Error during ajax checkEnfantFamille: " + textStatus);
            	
            },
            type: "GET"
        });
		
		
	}
	
</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DEM_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr>
							<td>
						
	<table width="100%">
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_ASUURE"/></td>
			<td>
				<%=affichePersonnne %>
				<%if (viewBean.isAnnulable()) {%>
					<input type="hidden" name="idDemande" value="<%=viewBean.getDemande().getId() %>"/>	
				<%}%>
			</td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_AGENCE_COMMUNALE"/></td>
			<td>
				<ct:FWListSelectTag name="demande.simpleDemande.idAgenceCommunale" data="<%=viewBean.getAgencesList() %>" 
					notation="data-g-select='mandatory:true'" defaut="<%=viewBean.getDemande().getSimpleDemande().getIdAgenceCommunale() %>"/>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_SERVICE_SOCIAL"/></td>
			<td>
				<ct:FWListSelectTag name="demande.simpleDemande.idAgenceRi" data="<%=viewBean.getRiList() %>" 
					notation="data-g-select='mandatory:true'" defaut="<%=viewBean.getDemande().getSimpleDemande().getIdAgenceRi() %>"/>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_DATE_DEPOT"/></td>
			<td><input type="text" name="demande.simpleDemande.dateDepot" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateDepot())%>" data-g-calendar="mandatory:true "/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_DATE_ARRIVEE"/></td>
			<td><input type="text" id="dateArriveeCanton" name="demande.simpleDemande.dateArrivee" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateArrivee())%>" data-g-calendar="mandatory:true, yearRange:¦1980:<%=Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) %>¦"/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_DATE_DEPART"/></td>
			<td><input type="text" id="dateDepartCanton" name="demande.simpleDemande.dateDepart" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateDepart())%>" data-g-calendar="yearRange:¦1980:<%=Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) %>¦"/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_TYPE_DEMANDE"/></td>
			<td>
				<ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_TYPE_DEMANDE%>" name="demande.simpleDemande.typeDemande" wantBlank="true" defaut="<%=viewBean.getDemande().getSimpleDemande().getTypeDemande()%>" notation="data-g-select='mandatory:true'"/>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_L_DATE_DEBUT"/></td>
			<td id="dateDebutCell"><input class="datesValues" type="text" id="dateDebut" name="dateDebutConverter" value="<%=JadeStringUtil.toNotNullString(viewBean.getDateDebutConverter())%>" data-g-calendar="mandatory:true,type:month "/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_L_DATE_FIN"/></td>
			<td><input type="text" class="datesValues" id="dateFin" name="dateFinConverter"  value="<%=JadeStringUtil.toNotNullString(viewBean.getDateFinConverter())%>" data-g-calendar="type:month "/></td>
		</tr>
		<tr id="infoEnfantCheckmsgWrapper">
			<td colspan=2>
				<div id="infoEnfantCheckmsg" data-g-boxmessage="type:WARN"></div>
			</td>
			
		</tr>
		
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_GESTIONNAIRE"/></td>
			<td>
				<% if (viewBeanIsNew) {%>
				<ct:FWListSelectTag name="demande.simpleDemande.idGestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
					notation="data-g-select='mandatory:true'" defaut="<%=objSession.getUserId()%>"/>
				<% } else { %>
				<ct:FWListSelectTag name="demande.simpleDemande.idGestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
					notation="data-g-select='mandatory:true'" defaut="<%=viewBean.getDemande().getSimpleDemande().getIdGestionnaire()%>"/>
				<% } %>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_L_AGENCE"/></td>
			<td>
				<ct:FWCodeSelectTag name="demande.simpleDemande.csCaisse" codeType="<%=IPFConstantes.CSGROUP_CAISSE%>" defaut="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getCsCaisse())%>" notation="data-g-select='mandatory:true'" wantBlank="true"/>
			</td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_VIENT_RI"/></td>
			<td><input type="checkbox" name="fromRI" <%=(viewBean.getDemande().getSimpleDemande().getFromRI() != null && viewBean.getDemande().getSimpleDemande().getFromRI()) ? "checked='checked'" : ""%>/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_MESURE_COACHING"/></td>
			<td><input type="checkbox" name="coaching" <%=(viewBean.getDemande().getSimpleDemande().getCoaching() != null && viewBean.getDemande().getSimpleDemande().getCoaching()) ? "checked='checked'" : ""%>/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_IMPOSE_SOURCE"/></td>
			<td><input type="checkbox" name="permisB" <%=(viewBean.getDemande().getSimpleDemande().getPermisB() != null && viewBean.getDemande().getSimpleDemande().getPermisB()) ? "checked='checked'" : ""%>/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_TYPE_PERMIS"/></td>
			<td>
				<ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_STATUT_SEJOUR%>" name="demande.simpleDemande.statutSejour" wantBlank="true" defaut="<%=viewBean.getDemande().getSimpleDemande().getStatutSejour()%>" notation="data-g-select='mandatory:false'"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<table>
					<tr>
						<td>
							<ct:FWMultiCheckbox cpTitle="JSP_PF_DEM_D_AUTRES_PRESTATIONS" height="130" tableWidth="300" name="demande.listCsAutresPrestations" codeType="<%=IPFConstantes.CSGROUP_AUTRES_PRESTATIONS_RECUES%>" defaultValues="<%=viewBean.getDemande().getListCsAutresPrestations()%>" />
						</td>
						<td width="20px">&nbsp;</td>
						<td>
							<ct:FWMultiCheckbox cpTitle="JSP_PF_DEM_D_AUTRES_DEMANDES" height="130" tableWidth="300" name="demande.listCsAutresDemandes" codeType="<%=IPFConstantes.CSGROUP_AUTRES_DEMANDES_EN_COURS%>" defaultValues="<%=viewBean.getDemande().getListCsAutresDemandes()%>"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_CALCUL_PARTICULIER"/></td>
			<td><input type="checkbox" name="calculParticulier" <%=(viewBean.getDemande().getSimpleDemande().getCalculParticulier() != null && viewBean.getDemande().getSimpleDemande().getCalculParticulier()) ? "checked='checked'" : ""%>/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_CAS_RIGUEUR"/></td>
			<td><input type="checkbox" name="casRigueur" <%=(viewBean.getDemande().getSimpleDemande().getCasRigueur() != null && viewBean.getDemande().getSimpleDemande().getCasRigueur()) ? "checked='checked'" : ""%>/></td>
		</tr>
		<tr>
			<td valign="top"><ct:FWLabel key="JSP_PF_DEM_D_REMARQUES"/></td>
			<td>
				<textarea rows="3" cols="80" name="demande.simpleDemande.remarques"><%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getRemarques()) %></textarea>
			</td>
		</tr>
		<tr id="sectionRefusForce" >
			<td><ct:FWLabel key="JSP_PF_DEM_D_REFUS_FORCE"/></td>
			<td><input type="checkbox" name="refusForce" id="refusForce" <%=(viewBean.getDemande().getSimpleDemande().getRefusForce() != null && viewBean.getDemande().getSimpleDemande().getRefusForce()) ? "checked='checked'" : ""%>/></td>			
		</tr>
		<tr id="sectionNonEntreeEnMatiere">
			<td>
				<ct:FWLabel key="JSP_PF_DEM_D_NON_ENTREE_EN_MATIERE"/>				
			</td>
			<td>
				<input type="checkbox" id="nonEntreeEnMatiere" name="nonEntreeEnMatiere" <%=(viewBean.getDemande().getSimpleDemande().getNonEntreeEnMatiere() != null && viewBean.getDemande().getSimpleDemande().getNonEntreeEnMatiere()) ? "checked='checked'" : ""%>/>
				<ct:FWLabel key="JSP_PF_DEM_DE_RENSEIGNEMENT" />
				<input type="text" style="width: 390px;" id="dateListeNonEntreeEnMatiere" name="demande.simpleDemande.dateListeNonEntreeEnMatiere" value="<%=JadeStringUtil.toNotNullString(viewBean.getDemande().getSimpleDemande().getDateListeNonEntreeEnMatiere()) %>"/>
			</td>
		</tr>
	</table>
						
						<input type="hidden" name="checkEnfant" id="checkEnfant" />	
							</td>
						</tr>
						<%-- /tpl:insert --%>
</TBODY>
				</TABLE>
				<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
				<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
				<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
				<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
				<INPUT type="hidden" name="_sl" value="">
				<INPUT type="hidden" name="selectorName" value="">
			</FORM>
			</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		<TR>
			<TD colspan="3" height="13"><%=(autoShowErrorPopup || !vBeanHasErrors) ? "&nbsp;" : "[ <a id=\"showErrorLink\" href=\"javascript:showErrors();\">visualiser les erreurs</a> ]"%></TD>
		</TR>
	 	<TR valign ="bottom">
		<TD colspan="3" align="left" style="font-family:verdana;font-size:9;">
		<div class="lastModification">
			<%=creationSpy == null ? "" : creationSpy%>Update: <%=lastModification%> 
		</div>	
		</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18" id="btnCtrlJade">
				<%if (bButtonNew) {%><input class="btnCtrl" type="button" id="btnNew" value="<%=btnNewLabel%>" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='<%=actionNew%>'"/><%}%>
				<%if (bButtonUpdate) {%><input class="btnCtrl" id="btnUpd" type="button" value="<%=btnUpdLabel%>" onclick="action(UPDATE);upd();"/><%}%>
				<%if (bButtonDelete) {%><input class="btnCtrl" id="btnDel" type="button" value="<%=btnDelLabel%>" onclick="del();"/><%}%>
				<%if (bButtonValidate) {%><input  id="btnValidate" type="button" value="<%=btnValLabel%>" /><%}%>
				<%if (bButtonCancel) {%><input class="btnCtrl inactive" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);"/><%}%>

				<%-- tpl:insert attribute="zoneButtons" --%>

<%if (viewBean.isAnnulable() && objSession.hasRight("perseus.demande.annulerDemande", FWSecureConstants.UPDATE)) {%>
	<input class="btnCtrl" id="btnAnnuler" type="button" value="Annuler la demande" onclick="annuler()"/>
<%}%>
				
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
