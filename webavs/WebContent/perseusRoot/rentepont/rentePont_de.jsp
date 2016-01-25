<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatRentePont"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.rentepont.PFRentePontViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax_hybride/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	
	PFRentePontViewBean viewBean = (PFRentePontViewBean) session.getAttribute("viewBean"); 
	idEcran="PPF1211";
	autoShowErrorPopup = true;
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	if(viewBeanIsNew){
		btnValLabel = objSession.getLabel("JSP_PF_DECISION_A_BOUTON_PREVALIDER");
	}
	
	
	boolean bBtnValiderRentePont = true;
	if (JadeStringUtil.isEmpty(viewBean.getRentePont().getSimpleRentePont().getCsEtat()) || !objSession.hasRight("perseus.decision.validation", FWSecureConstants.UPDATE)) {
		bBtnValiderRentePont = false;
	}
	
	if(!PerseusServiceLocator.getPmtMensuelRentePontService().isValidationDecisionAuthorise()){
		bBtnValiderRentePont = false;
	}
	
	if (viewBeanIsNew) {
		bButtonDelete = false;
	}
	if (CSEtatRentePont.VALIDE.getCodeSystem().equals(viewBean.getRentePont().getSimpleRentePont().getCsEtat())) {
		bBtnValiderRentePont = false;
		bButtonDelete = false;
		bButtonUpdate = false;
		if (JadeStringUtil.isEmpty(viewBean.getRentePont().getSimpleRentePont().getDateFin())) {
			if(PerseusServiceLocator.getPmtMensuelRentePontService().isValidationDecisionAuthorise()
					&& objSession.hasRight("perseus", FWSecureConstants.ADD)){
				bButtonUpdate = true;
			}else{
				bButtonUpdate = false;				
			}
			bButtonDelete = false;
		}
	}

	PersonneEtendueComplexModel personne = viewBean.getRentePont().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";
	String idDossier = request.getParameter("idDossier");

	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="perseus-optionsrentepont">
	    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getRentePont().getSimpleRentePont().getIdSituationFamiliale()%>"/>
		<ct:menuSetAllParams key="idRentePont" value="<%=viewBean.getRentePont().getId() %>"/>
		<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getRentePont().getSimpleRentePont().getIdDossier() %>"/>
		<% if (CSEtatRentePont.VALIDE.getCodeSystem().equals(viewBean.getRentePont().getSimpleRentePont().getCsEtat())) { %>
			<ct:menuActivateNode active="yes" nodeId="FACTURERENTEPONT"/>
		<% } else { %>
			<ct:menuActivateNode active="no" nodeId="FACTURERENTEPONT"/>
		<% } %>
</ct:menuChange>

<script type="text/javascript">
	var url = "perseus.rentepont.rentePont";
	var actionMethod;
	var userAction;
	var ETAT_VALIDE = <%=CSEtatRentePont.VALIDE.getCodeSystem()%>;

	$(function(){
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
		<%if (viewBeanIsNew) {%>
    	jscss("remove", document.getElementById("btnValiderRentePont"), "inactive", null);
		<%}%>
		<% if(CSEtatRentePont.VALIDE.getCodeSystem().equals(viewBean.getRentePont().getSimpleRentePont().getCsEtat()) && "_fail".equals(request.getParameter("valid"))) { %>
		readOnly(true);
		$("#dateFin")[0].disabled = false;
		<% } %>
	});
	
	
	function validate() {
	    state = true;
	    if (actionMethod == "add"){
	    	document.forms[0].elements('userAction').value="perseus.rentepont.rentePont.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="perseus.rentepont.rentePont.modifier";
	    }
	    return state;
	}  
	
	function validerRentePont() {
		$('#etatRentePont').val(ETAT_VALIDE);
		action(COMMIT);
	}
	
	function add() {}
	function upd() {
		<% if(CSEtatRentePont.VALIDE.getCodeSystem().equals(viewBean.getRentePont().getSimpleRentePont().getCsEtat())) { %>
		readOnly(true);
		$("#dateFin")[0].disabled = false;
		<% } %>
		<%if (bBtnValiderRentePont) {%>
    	jscss("add", document.getElementById("btnValiderRentePont"), "inactive", null);
		<% } %>
	}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.rentepont.rentePont.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
			document.forms[0].elements('userAction').value="perseus.rentepont.rentePont.chercher";
	}
	
	function init() {}
	
	function postInit() {
	}
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_RENTEPONT_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
	<tr>
		<td>
			<input type="hidden" name="etatRentePont" id="etatRentePont" />
			<table width="100%">
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_ASSURE"/></td>
					<td><%=affichePersonnne %></td>
				</tr>
				<tr>
					<td valign="top"><label><ct:FWLabel key="JSP_PF_RENTEPONT_D_ADRESSE_PAIEMENT" />&nbsp;</label></td>
					<td valign="top">
						<div data-g-adresse="mandatory:true,service:findAdressePaiement,defaultvalue:¦<%=viewBean.getAdressePaiement()%>¦">
    						<input type="hidden" id="idTiersAdressePaiement" class="avoirPaiement.idTiers" name="rentePont.simpleRentePont.idTiersAdressePaiement" value="<%=viewBean.getRentePont().getSimpleRentePont().getIdTiersAdressePaiement() %>"> 
							<input type="hidden" id="idDomaineApplicatifAdressePaiement" class="avoirPaiement.idApplication" name="rentePont.simpleRentePont.idDomaineApplicatifAdressePaiement" value="<%=viewBean.getRentePont().getSimpleRentePont().getIdDomaineApplicatifAdressePaiement() %>">
						</div>
					</td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_DATE_DEPOT"/></td>
					<td><input type="text" name="rentePont.simpleRentePont.dateDepot" value="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getDateDepot())%>" data-g-calendar="mandatory:true "/></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_DATE_DEBUT"/></td>
					<td><input type="text" name="dateDebutConverter" value="<%=JadeStringUtil.toNotNullString(viewBean.getDateDebutConverter())%>" data-g-calendar="mandatory:true,type:month "/></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_DATE_FIN"/></td>
					<td><input type="text" name="dateFinConverter" id="dateFin" value="<%=JadeStringUtil.toNotNullString(viewBean.getDateFinConverter())%>" data-g-calendar="type:month "/></td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_GESTIONNAIRE"/></td>
					<td>
						<% if (viewBeanIsNew) {%>
						<ct:FWListSelectTag name="rentePont.simpleRentePont.idGestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
							notation="data-g-select='mandatory:true'" defaut="<%=objSession.getUserId()%>"/>
						<% } else { %>
						<ct:FWListSelectTag name="rentePont.simpleRentePont.idGestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
							notation="data-g-select='mandatory:true'" defaut="<%=viewBean.getRentePont().getSimpleRentePont().getIdGestionnaire()%>"/>
						<% } %>
					</td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_AGENCE"/></td>
					<td>
						<ct:FWCodeSelectTag name="rentePont.simpleRentePont.csCaisse" codeType="<%=IPFConstantes.CSGROUP_CAISSE%>" defaut="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getCsCaisse())%>" notation="data-g-select='mandatory:true'" wantBlank="true"/>
					</td>
				</tr>
				<tr><td colspan="2"><hr></td></tr><!--
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_ETAT"/></td>
					<td>
						<ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_ETAT_RENTEPONT %>" name="rentePont.simpleRentePont.csEtat" wantBlank="true" defaut="<%=viewBean.getRentePont().getSimpleRentePont().getCsEtat()%>" notation="data-g-select='mandatory:true'"/>
					</td>
				</tr>
				--><tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_MONTANT"/></td>
					<td><input type="text" name="rentePont.simpleRentePont.montant" id="montant" value="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getMontant())%>" data-g-amount=" "/></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_EXCEDANTREVENU"/></td>
					<td><input type="text" name="rentePont.simpleRentePont.excedantRevenu" id="excedantRevenu" value="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getExcedantRevenu())%>" data-g-amount=" "/></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_MONTANTRETROACTIF"/></td>
					<td><input type="text" name="rentePont.simpleRentePont.montantRetroactif" id="montantRetroactif" value="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getMontantRetroactif())%>" data-g-amount=" "/></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PF_RENTEPONT_D_IMPOTSOURCE"/></td>
					<td><input type="text" name="rentePont.simpleRentePont.montantImpotSource" id="montantImpotSource" value="<%=JadeStringUtil.toNotNullString(viewBean.getRentePont().getSimpleRentePont().getMontantImpotSource())%>" data-g-amount=" "/></td>
				</tr>
			</table>
		</td>
	</tr>

						<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%if (bBtnValiderRentePont) {%><input class="btnCtrl" id="btnValiderRentePont" type="button" value="<%=objSession.getLabel("JSP_PF_RENTEPONT_D_VALIDER")%>" onclick="if(validate()) validerRentePont();"><%}%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax_hybride/footer.jspf" %>
