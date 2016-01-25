<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.corvus.api.avances.IREAvances"%>
<%@page import="globaz.corvus.db.avances.REAvance"%>
<%@ page language="java" import="globaz.globall.http.*"  contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="globaz.externe.IPRConstantesExternes"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="globaz.pegasus.vb.avance.PCAvanceViewBean"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_AVANCE_D"
	idEcran="PPC0111";

	//traitement des paramètres
	String idDemande = request.getParameter("idDemandePc");
	String idTiers = request.getParameter("idTiers");
	
	
	//recup eventuel message d'erreur
	String errrorMsg = request.getParameter("errorMsg");
	
	
	PCAvanceViewBean viewBean = (PCAvanceViewBean) session.getAttribute("viewBean");
	REAvance avance = viewBean.getAvance();
	boolean viewBeanIsNew = true;
	bButtonNew = false;
	bButtonValidate = true; 
	bButtonDelete = true;
	bButtonUpdate = true;
	actionNew = IPCActions.ACTION_AVANCE_DETAIL_NEW;
	
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/avance/avance_de.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/avance/avance_de.css"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
$(function () {
	userAction=$('[name=userAction]',document.forms[0])[0];
	//gestion du choix du bénéficiaire, mise à jour de l'adresse de paiement, et de l'id tiers et du domaine de l'adresse
	//ne sera utilisé que dans le cas d'une nouvelle avance
	$('#choixConjoint').click(function () {
		$('.adresse').html("<%= viewBean.decodeForJs(viewBean.getAdressePaiementConjoint()) %>");
		$('#idTiersBeneficiaire').val(<%= viewBean.getIdTiersConjoint() %>);
		$('#avoirPaiementIdTiers').val(<%= viewBean.getIdTiersConjoint() %>);
		$('#avoirPaiementIdApplication').val(<%= IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE %>);
		   
	});
	$('#choixRequerant').click(function () {
		$('.adresse').html("<%= viewBean.decodeForJs(viewBean.getAdressePaiementRequerant()) %>");
		$('#idTiersBeneficiaire').val(<%= viewBean.getIdTiersRequerant() %>);
		$('#avoirPaiementIdTiers').val(<%= viewBean.getIdTiersRequerant() %>);
		$('#avoirPaiementIdApplication').val(<%= IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE %>);
	});
	
	//Erreurs
	var msgError="<%=errrorMsg%>";
	
	if(msgError !="" && msgError!="null"){
		globazNotation.utils.consoleError(msgError);
	}

	
});


function validate() {
	
	state = true;
	return state;
}   

function add(){
	$('.fieldsEtat').prop('disabled','disabled');
	$('#getDateDebutPmt1erAcompte').prop('disabled','disabled');
	
	userAction.value="<%=IPCActions.ACTION_AVANCE_DETAIL_NEW%>";
}

function init(){
	
};

function upd(){
	//les champs radios restent grisés
	$('.chkBeneficiaire').attr('disabled','disabled');
	//date de paiements
	$('#getDateDebutPmt1erAcompte').prop('disabled','disabled');
	//et etat
	$('.fieldsEtat').attr('disabled','disabled');
	// le montant d'une avance mensuelle en cours aussi
	if(<%=IREAvances.CS_ETAT_ACOMPTE_EN_COURS.equals(viewBean.getAvance().getCsEtatAcomptes())%>){
		$('#valMontantM').attr('disabled','disabled');
	}
	//tous les champs d'une avance unique annulé
	if(<%=IREAvances.CS_ETAT_1ER_ACOMPTE_ANNULE.equals(viewBean.getAvance().getCsEtat1erAcompte())%>){
		$('#valMontantU').attr('disabled','disabled');
		$('#valDateDebutU').attr('disabled','disabled');
	}
	userAction.value="<%=IPCActions.ACTION_AVANCE_DETAIL_UPDATE%>";
}

function cancel(){
	userAction.value="<%= IPCActions.ACTION_AVANCE_LISTE_AVANCES %>";
}

function del(){
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
		  userAction.value="<%= IPCActions.ACTION_AVANCE_DETAIL_DELETE %>";
	        document.forms[0].submit();
	  }
	
}


</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<%= viewBean.getDetailTitle() %>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<TR>
		<TD colspan="6" >
			
			
			<!--  ************************* Zone infos requerant de la demande *************************  -->
			<div id="infos_requerant" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_INFOS_REQUERANT_DEMANDE_TITRE_D"/></h1>
				<%=PCDroitHandler.getInfoHtmlRequerantForDemande(viewBean.getIdDemande())%>
			</div>	
			
			<!--  ************************* Zone bénéficiaire et adresse de paiement *************************  -->
			<div  class="row-fluid">
				
				
				<div class="span6">
					<!--  zone adresse de paiement -->
				<div id="adresse_paiement" class="titre">
					<h1 class="ui-widget-header"><ct:FWLabel key="JSP_PC_AVANCE_INFOS_ADR_PAIEMENT_D"/></h1>
				    	<div id="" data-g-adresse="service:findAdressePaiement, defaultvalue:¦<%= viewBean.getAdressePaiementBeneficiaire() %>¦">
				    		<input class="avoirPaiement.idTiers" id="avoirPaiementIdTiers" name="avance.idTiersAdrPmt" value="<%=(viewBean.isAdressePaiementFindOnLoad())? viewBean.getIdTiersAdressePaiement():"0" %> " type="hidden" />
			   	    		<input class="avoirPaiement.idApplication" id="avoirPaiementIdApplication" name="avance.csDomaine" value="<%= viewBean.getCsDomaineAdressePaiement() %>" type="hidden" />
					    </div>
				</div>
				</div>
				
				<div class="span6">
					<!-- zone bénéficiaire -->
					<div id="infos_beneficiaire" class="titre">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_INFOS_BENEFICIAIRE_TITRE_D"/></h1>
					<table class="areaTable" style="width:100%">
						<tr>
							<th class="" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_INFOS_PERSONNE_D"/></th>
							<th class="chkBeneficiaireTh" nowrap="nowrap"><ct:FWLabel key="JSP_PC_AVANCE_INFOS_BENEFICIAIRE_D"/></th>
						</tr>
						<tr>
							<td><%= viewBean.getInfoRequerantAsBeneficiaire() %></td>
							<td  class="chkBeneficiaire"><input id="choixRequerant" class="" value="" type="radio" name="choixB" <%= viewBean.isForRequerant()?"checked":"" %> /></td>
						</tr>
						<% if(viewBean.getInfoConjointAsBeneficiaire()!=null){%>
							<td><%= viewBean.getInfoConjointAsBeneficiaire() %></td>
							<td class="chkBeneficiaire"><input  id="choixConjoint" class=""  value="" type="radio" name="choixB" <%= viewBean.isForRequerant()?"":"checked" %>/></td>
						<%} %>
						
					</table>
					<input id="idTiersBeneficiaire" type="hidden" name="avance.idTiersBeneficiaire" value="<%= viewBean.getIdTiersBeneficiaire() %>"/>
				</div>	
				
				</div>
			
			</div>
			
			
				
			
			
			<!--  zone accompte unique -->
			<div id="acompte_unique" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_ACOMPTE_UNIQUE_TITRE_D"/></h1>
				
				<!--  ligne 1  -->
				<div style="padding-top:5px;" class="row-fluid">
					<div class="span2">
						<span id="lblDateDebut"><ct:FWLabel key="JSP_PC_AVANCE_UNIQUE_DATE_DEBUT_D"/></span>
					</div>
					<div class="span2">
						<input id="valDateDebutU" data-g-calendar="mandatory: true"  name="avance.dateDebutPmt1erAcompte" value="<%= viewBean.getAvance().getDateDebutPmt1erAcompte()%>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
					
					<div class="span2">
						<span id="lblDatePaiement"><ct:FWLabel key="JSP_PC_AVANCE_UNIQUE_DATE_PAIEMENT_D"/></span>
					</div>
					<div class="span3">
						<input id="getDateDebutPmt1erAcompte" notation='data-g-calendar=" "' name="avance.datePmt1erAcompte" value="<%=viewBean.getAvance().getDatePmt1erAcompte() %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
				</div>
				
				<!--  ligne 2 -->
				<div class="row-fluid">
					<div class="span2">
						<span id="lblMontant"><ct:FWLabel key="JSP_PC_AVANCE_UNIQUE_MONTANT_D"/></span>
					</div>
					<div class="span2">
						<ct:inputText id="valMontantU"  name="avance.montant1erAcompte" defaultValue="<%=viewBean.getAvance().getMontant1erAcompte() %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
					
					<div class="span2">
						<span id="lblEtat"><ct:FWLabel key="JSP_PC_AVANCE_UNIQUE_ETAT_D"/></span>
					</div>
					<div class="span2">
						<input class="fieldsEtat" id="valEtatM" name="csEtatAvanceUnique" value="<%= viewBean.getLibelle(viewBean.getAvance().getCsEtat1erAcompte()) %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
					
				</div>
			</div>	
			
			<!--  zone accompte mensuel -->
			<div id="acompte_mensuel" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCE_ACOMPTE_MENSUEL_TITRE_D"/></h1>
				<div  style="padding-top:5px;" class="row-fluid">
					<div class="span2">
						<span id="lblDateDebut"><ct:FWLabel key="JSP_PC_AVANCE_MENSUEL_DATE_DEBUT_D"/></span>
					</div>
					<div class="span2">
						<input type="text" id="valDateDebutM" data-g-calendar="mandatory: true" name="avance.dateDebutAcompte" value="<%=viewBean.getAvance().getDateDebutAcompte() %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
					
					<div class="span2">
						<span id="lblDatePaiement"><ct:FWLabel key="JSP_PC_AVANCE_MENSUEL_DATE_FIN_D"/></span>
					</div>
					<div class="span2">
						<ct:inputText id="valDatePaiement" notation='data-g-calendar=" "'  name="avance.dateFinAcompte" defaultValue="<%=viewBean.getAvance().getDateFinAcompte() %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
				</div>
				
				<div class="row-fluid">
					
					<div class="span2">
						<span id="lblMontant"><ct:FWLabel key="JSP_PC_AVANCE_MENSUEL_MONTANT_D"/></span>
					</div>
					<div class="span2">
						<input type="text" id="valMontantM" name="avance.montantMensuel" value="<%= viewBean.getAvance().getMontantMensuel() %>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
					
					<div class="span2">
						<span id="lblEtat"><ct:FWLabel key="JSP_PC_AVANCE_MENSUEL_ETAT_D"/></span>
					</div>
					<div class="span2">
						<input id="valEtatM" class="fieldsEtat" type="text" name="csEtatMensuel" value="<%= viewBean.getLibelle(viewBean.getAvance().getCsEtatAcomptes())%>"/>
					</div>
					
					<!--  espacement -->
					<div class="span1">
					</div>
				</div>
			</div>	
			
		</TD>
		
		<input type="hidden" name="idTiers" value="<%= viewBean.getIdTiersDemande() %>"/>
		<input type="hidden" name="idDemandePc" value="<%=viewBean.getIdDemande()  %>"/>
		
		
	</TR>
				
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>