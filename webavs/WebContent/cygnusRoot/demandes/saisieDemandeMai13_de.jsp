<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="globaz.jade.client.util.JadeDateUtil" %>
<%@ page import="globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean" %>
<%@ page import="globaz.cygnus.utils.RFGestionnaireHelper" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>
<%@ page import="globaz.framework.bean.FWViewBeanInterface" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Map" %>
<%@ page import="globaz.cygnus.utils.RFUtils" %>
<%@ page import="globaz.cygnus.api.demandes.IRFDemande" %>
<%@ page import="globaz.globall.util.JACalendar" %>
<%@ page import="globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean" %>
<%@ page import="globaz.cygnus.api.demandes.IRFDemande" %>
<%@ page import="globaz.framework.util.FWCurrency" %>
<%@ page import="globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page isELIgnored ="false" %>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_D"
	idEcran = "PRF0009";

	RFSaisieDemandeViewBean viewBean = (RFSaisieDemandeViewBean) session.getAttribute("viewBean");
	String showWarningsUrl = servletContext + "/cygnusRoot/demandes/warningModalDlg.jsp";

	autoShowErrorPopup = true;

	if (viewBean.getIsAfficherDetail()) {
		if (viewBean.getCsEtat().equals(IRFDemande.VALIDE) || 
			viewBean.getCsEtat().equals(IRFDemande.PAYE) || 
			viewBean.getCsEtat().equals(IRFDemande.ANNULE)) {
			bButtonDelete = false;
			bButtonUpdate = false;
			bButtonValidate = false;
		} else {
			bButtonDelete = true;
			bButtonUpdate = true;
			bButtonValidate = true;
		}
		bButtonCancel = true;
		bButtonNew = false;
	} else {
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonValidate = false;
		bButtonCancel = false;
		bButtonNew = false;
	}
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.css" />
<link type="text/css" href="<%=servletContext%>/theme/widget.css" rel="stylesheet" />

<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/cygnusRoot/demandes/saisieDemande.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<style type="text/css">
	.texteCentrerDansSpan {
		width: 100%;
		text-align: center;
	}
</style>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" />
<ct:menuChange displayId="options" menuId="cygnus-optionsdemandes" showTab="menu">
<%
	if (viewBean.getCsEtat().equals(IRFDemande.PAYE)) {
%>	<ct:menuActivateNode active="yes" nodeId="demandes_correction" />
<%
	} else {
%>	<ct:menuActivateNode active="no" nodeId="demandes_correction" />
<%
	}
	if (viewBean.getCsEtat().equals(IRFDemande.ENREGISTRE)) {
%>	<ct:menuActivateNode active="yes" nodeId="demandes_imputerSurQd" />
<%
	} else {
%>	<ct:menuActivateNode active="no" nodeId="demandes_imputerSurQd" />
<%
	}
%></ct:menuChange>
<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>" menuId="cygnus-optionsdemandes" />

<script type="text/javascript">

	<%=viewBean.getMotifsRefusInnerJavascript()%>
	<%=viewBean.getMotifsRefusAssociationInnerJavascript()%>

	var o_periodesCAAI = <%=new Gson().toJson(viewBean.getPeriodesCAAI())%>;
	var s_dateDernierPaiement = '<%=viewBean.getDateDernierPaiement()%>';
	var idAdressePaiementDemande = '<%=viewBean.getIdAdressePaiementDemande()%>'

	function cancel () {
		document.forms[0].elements('userAction').value='<%=IRFActions.ACTION_SAISIE_DEMANDE+ ".afficher"%>';
		return true;
	}

	function validate (){
<%
	if (IRFDemande.CALCULE.equals(viewBean.getCsEtat()) 
		&& !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
%>		if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_ANNULER_PREPARER_DECISION_MESSAGE_INFO'/>")) {
<%
	}
%>			$("[name=idTiers]").val($("input[type=radio][name=membreFamille]:checked").attr('value'));
			return true;
<%
	if (IRFDemande.CALCULE.equals(viewBean.getCsEtat()) 
		&& !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
%>		} else {
			return false;
		}
<%
	}
%>	}

	function del () {
<%
	if (IRFDemande.CALCULE.equals(viewBean.getCsEtat()) 
		&& !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
%>		if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_ANNULER_PREPARER_DECISION_MESSAGE_INFO'/>")) {
<%
	} else {
%>		if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_DELETE_MESSAGE_INFO'/>")) {
<%
	}
%>			document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.supprimer";
			document.forms[0].submit();
		}
	}

	function add (type) {
		document.getElementsByName("typeValidation")[0].value = type;
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.ajouter";

		$("[name=idTiers]").val($("input[type=radio][name=membreFamille]:checked").attr('value'));

		return true;
	}

	function upd () {
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.modifier";
	}

	var hasAddShowErrors = false;
	function showErrors() {
		if (errorObj.text != "") {
			if (notationManager.b_started) {
				showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
			} else {
				if (!hasAddShowErrors) {
					var text = errorObj.text;
					$('html').on(eventConstant.NOTATION_MANAGER_DONE, function () {
						errorObj.text = text;
						showErrors();
					})
					hasAddShowErrors = true
				}
			}
		}
	}

	function init () {
		document.forms[0].elements('_method').value = "add";
<%
	if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
%>		errorObj.text = "<%=viewBean.getMessage()%>";
		showErrors();
		errorObj.text="";
<%
	} else if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
%>		errorObj.text = "<%=viewBean.getMessage()%>";
		var url = "<%=showWarningsUrl%>";
		var valShowModalDialog = showModalDialog(url,errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		errorObj.text="";
		if (valShowModalDialog.isBack != "true") {
			$('[name="warningMode"]').val("true");
<%
		if (!viewBean.getIsAfficherDetail()) {
%>			if (add('<%=viewBean.getTypeValidation()%>')) {
				action(COMMIT);
			}
<%
		} else {
%>			upd();
			if (validate()) {
				action(COMMIT);
			}
<%
		}
%>		}
<%
	}
%>	}

	function typeSousTypeDeSoinListInit () {

		document.getElementsByName("codeTypeDeSoinList")[0].options[parseInt("<%=viewBean.getCodeTypeDeSoinList()%>",10)].selected = true;
		document.getElementsByName("codeTypeDeSoin")[0].value = "<%=viewBean.getCodeTypeDeSoinList()%>";
		document.getElementsByName("imgCodeTypeDeSoin")[0].src = <%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;
		document.getElementsByName("codeSousTypeDeSoin")[0].value = "<%=viewBean.getCodeSousTypeDeSoinList()%>";
		document.getElementsByName("imgCodeSousTypeDeSoin")[0].src = <%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;

		currentSousTypeDeSoinTab = sousTypeDeSoinTab[parseInt(document.getElementsByName("codeTypeDeSoin")[0].value,10)-1];
		currentCodeSousTypeList = currentSousTypeDeSoinTab[0];
		currentLibelleSousTypeList = currentSousTypeDeSoinTab[1];
		codeSousTypeDeSoinList = document.getElementsByName("codeSousTypeDeSoinList")[0];
		codeSousTypeDeSoinList.options.length = 0;
		codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length] = new Option("","");

		for (i = 0; i < currentCodeSousTypeList.length; i++) {
			codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length] = new Option(currentLibelleSousTypeList[i],currentCodeSousTypeList[i]);
		}

		codeSousTypeDeSoinList.options[parseInt("<%=viewBean.getCodeSousTypeDeSoinList()%>",10)].selected = true;
	}

	function postInit () {
<%
	if (!viewBean.getIsAfficherDetail()) {
%>		action('add');
<%
	} else {
		if (viewBean.isFocusMotifsDeRefus() || viewBean.isFocusAdressePaiement()) {
%>		action('add');
<%
		} else {
%>		action('read');
<%
		}
%>		document.getElementById("csEtatDemande").disabled = true;
		document.getElementById("csEtatDemande").readOnly = true;
		document.getElementById("csSource").disabled = true;
		document.getElementById("csSource").readOnly = true;
<%
	}
%>		document.getElementById("codeTypeDeSoinList").disabled = true;
		document.getElementById("codeTypeDeSoinList").readOnly = true;

		document.getElementById("codeSousTypeDeSoinList").disabled = true;
		document.getElementById("codeSousTypeDeSoinList").readOnly = true;

		document.getElementById("codeTypeDeSoin").disabled = true;
		document.getElementById("codeTypeDeSoin").readOnly = true;

		document.getElementById("codeSousTypeDeSoin").disabled = true;
		document.getElementById("codeSousTypeDeSoin").readOnly = true;

<%
	if (!IRFDemande.ENREGISTRE.equals(viewBean.getCsEtat())) {
%>		document.getElementById("idGestionnaire").disabled = true;
		document.getElementById("idGestionnaire").readOnly = true;
<%
	}
	if (!viewBean.isFocusMotifsDeRefus() && !viewBean.isFocusAdressePaiement()) {
%>		document.getElementById("dateReception").select();
<%
	} else {
		if (viewBean.isFocusMotifsDeRefus()) {
%>		document.getElementById("motifsRefusId").focus();
<%
		} else if (viewBean.isFocusAdressePaiement()) {
%>		document.getElementsByName("selecteurTiersAdressePaiement")[0].focus();
<%
		}
	}
%>	}
</script>
<style type="text/css">
	.widgetAdmin {
		width: 320px;
	}
	.trContainerAdresse td{
		 vertical-align: top;
	}
</style>
<script type="text/javascript" src="cygnusRoot/scripts/demande/demande.js"></script>
<script type="text/javascript" src="cygnusRoot/demandes/saisieDemandeMai13_de.js"></script>

<%@ include file="/theme/detail/bodyStart.jspf" %>

<ct:FWLabel key="JSP_RF_DEM_S_MAI13_TITRE"/>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%
	if (viewBean.getIsAfficherDetail()) {
%>						<tr>
							<td width="200px">
								<ct:FWLabel key="JSP_RF_DEM_S_NUMERO" />
							</td>
							<td colspan="5">
								<b>
									<%=viewBean.getIdDemande()%>
								</b>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
<%
	}
%>						<tr>
							<td width="199px">
								<ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE" />
							</td>
							<td colspan="5">
								<ct:FWListSelectTag	name="idGestionnaire" 
													data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
													defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire()) ? viewBean.getSession().getUserId() : viewBean.getIdGestionnaire()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr style="vertical-align:top" >
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_TIERS" />
							</td>
							<td colspan="5">
								<input	type="hidden" 
										id="idTiers" 
										name="idTiers" 
										value="<%=viewBean.getIdTiers()%>" />
								<input	type="hidden" 
										id="nss" 
										name="nss" 
										value="<%=viewBean.getNss()%>" />
								<input	type="hidden" 
										name="provenance" 
										value="<%=viewBean.getProvenance()%>" />
								<input	type="hidden" 
										name="idDemande" 
										value="<%=viewBean.getIdDemande()%>" />
								<input	type="hidden" 
										name="warningMode" 
										value="<%=viewBean.getWarningMode()%>" />
								<input	type="hidden" 
										id="idFournisseurDemande" 
										name="idFournisseurDemande" 
										value="<%=viewBean.getIdFournisseurDemande()%>" />
								<input	type="hidden" 
										id="nom" 
										name="nom" 
										value="<%=viewBean.getNom()%>" />
								<input	type="hidden" 
										id="prenom" 
										name="prenom" 
										value="<%=viewBean.getPrenom()%>" />
								<input	type="hidden" 
										id="dateNaissance" 
										name="dateNaissance" 
										value="<%=viewBean.getDateNaissance()%>" />
								<input	type="hidden" 
										id="csSexe" 
										name="csSexe" 
										value="<%=viewBean.getCsSexe()%>" />
								<input	type="hidden" 
										id="csNationalite" 
										name="csNationalite" 
										value="<%=viewBean.getCsNationalite()%>" />
								<input	type="hidden" 
										name="csCanton" 
										value="<%=viewBean.getCsCanton()%>" />
								<input	type="hidden" 
										name="dateDeces" 
										value="<%=viewBean.getDateDeces()%>" />
								<input	type="hidden" 
										name="isConventionne" 
										value="<%=viewBean.isConventionne()%>" />
								<input	type="hidden" 
										name="typeValidation" 
										value="" />
								<input	type="hidden" 
										name="montantAPayer" 
										value="" />
								<input	type="hidden" 
										id="typeDemande" 
										name="typeDemande" 
										value="<%=RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MAI13%>" />
								<input	type="hidden" 
										name="hasMotifRefusDemande" 
										value="<%=viewBean.isHasMotifRefusDemande()%>" />
								<input	type="hidden" 
										name="isAfficherDetail" 
										value="<%=viewBean.getIsAfficherDetail()%>" />
								<input	type="hidden" 
										name="isAfficherCaseForcerPaiement" 
										value="<%=viewBean.getIsAfficherCaseForcerPaiement()%>" />
								<input	type="hidden" 
										name="isAfficherRemFournisseur" 
										value="<%=viewBean.getIsAfficherRemFournisseur()%>" />
								<input	type="hidden" 
										id="idQdPrincipale" 
										name="idQdPrincipale" 
										value="<%=viewBean.getIdQdPrincipale()%>" />
								<input type="hidden" 
										id="motifsDeRefusPasDeSelection" 
										value="<%=viewBean.getSession().getLabel("JSP_RF_DEM_S_MOTIFS_DE_REFUS_PAS_DE_SELECTION")%>" />
								<input	type="hidden" 
										id="motifsDeRefusSelection" 
										value="<%=viewBean.getSession().getLabel("JSP_RF_DEM_S_MOTIFS_DE_REFUS_SELECTIONNES")%>" />
								<input	type="hidden" 
										id="csEtatEnregistre" 
										value="<%=IRFDemande.ENREGISTRE%>" />

						<!-- si l'état de la demande est différent d'enregistré on ne peut plus modifier le bénéficiaire -->
								<table id="descFamilleCc" cellpadding="2" cellspacing="2" >
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr />
							</td>
						</tr>
<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
						<tr>
							<td colspan="6">
								<input	type="hidden" 
										name="isSaisieDemande" 
										value="true" />
							</td>
						</tr>
						<tr style="height: 10px;" class="messageAvertissementCAAI">
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr class="messageAvertissementCAAI">
							<td colspan="6">
								<div data-g-boxmessage="type:WARN">
									<span id="messageAvertissementSansDate" class="texteCentrerDansSpan">
										<strong>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_EN_COURS" />
										</strong>
									</span>
									<span id="messageAvertissementEchue" class="texteCentrerDansSpan">
										<strong>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_ECHUE" />
										</strong>
									</span>
									<span id="messageAvertissementFutrue" class="texteCentrerDansSpan">
										<strong>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_FUTURE" />
										</strong>
									</span>
									<span id="messageAvertissementDateFacture" class="texteCentrerDansSpan">
										<strong>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_DATE_FACTURATION_PART_1" />
											<span id="spanDateFacturation">
											</span>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_DATE_FACTURATION_PART_2" />
										</strong>
									</span>
									<span id="messageAvertissementDateTraitement" class="texteCentrerDansSpan">
										<strong>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_DATE_TRAITEMENT_PART_1" />
											<span id="spanDateTraitement">
											</span>
											<ct:FWLabel key="WARNING_RF_DEM_S_CONTRIBUTION_ASSISTANCE_AI_DATE_TRAITEMENT_PART_2" />
										</strong>
									</span>
									<br />
									<span class="texteCentrerDansSpan">
										<i>
											<ct:FWLabel key="WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_PERIODE" />
											&nbsp;
											<strong>
												<span id="spanDateDebutPeriode">
												</span>
												&nbsp;-&nbsp;
												<span id="spanDateFinPeriode">
												</span>
											</strong>
											&nbsp;
											<ct:FWLabel key="WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_MONTANT" />
											&nbsp;
											<strong>
												<span id="spanMontantCAAI">
												</span>
											</strong>
											&nbsp;
											<ct:FWLabel key="WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_API" />
											&nbsp;
											<strong>
												<span id="spanCodeAPI">
												</span>
												<span id="spanDetailCodeAPI">
												</span>
											</strong>
											&nbsp;
											<ct:FWLabel key="WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_MONTANT_API" />
											&nbsp;
											<strong>
												<span id="spanMontantAPI">
												</span>
											</strong>
										</i>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr id="descQd">
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<hr />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_DATE_RECEPTION" />
							</td>
							<td>
								<input	data-g-calendar=" " 
										name="dateReception" 
										value="<%=JadeStringUtil.isEmpty(viewBean.getDateReception()) ? JACalendar.todayJJsMMsAAAA() : viewBean.getDateReception()%>" />
							</td>
							
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_DATE_FACTURE" />
							</td>
							<td>
								<input	data-g-calendar=" " 
										id="dateFacture" 
										name="dateFacture" 
										value="<%=viewBean.getDateFacture()%>" />
							</td>
							
							<!--  is retroactif -->
							<TD><ct:FWLabel key="JSP_RF_DEM_S_IS_RETRO"/>
								<input type="checkbox"  value="on" id="isRetro" name="isRetro" <%=viewBean.getIsRetro().booleanValue()?"CHECKED":""%>/>
						
							</TD>
							
						</tr>
						<tr>
							
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_DATE_DEBUT_TRAITEMENT" />
							</td>
							<td>
								<input	data-g-calendar=" " 
										id="dateDebutTraitement" 
										name="dateDebutTraitement" 
										value="<%=viewBean.getDateDebutTraitement()%>" />
							</td>
							
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_DATE_FIN_TRAITEMENT" />
							</td>
							<td colspan="3">
								<input	data-g-calendar=" " 
										id="dateFinTraitement" 
										name="dateFinTraitement" 
										value="<%=viewBean.getDateFinTraitement()%>" />
							</td>
							
							
						</tr>
						<tr><td>&nbsp</td></tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_NO_FACTURE" />
							</td>
							<td colspan="2">
								<input	type="text" 
										name="numeroFacture" 
										value="<%=viewBean.getNumeroFacture()%>" 
										style="width: 80%;" />
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_NOMBRE_HEURES" />
							</td>
							<td colspan="5">
								<input	type="text" 
										name="nombreHeure" 
										value="<%=viewBean.getNombreHeure()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
<%
	if (viewBean.getIsAfficherRemFournisseur()) {
%>						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_REM_FOURNISSEUR" />
							</td>
							<td colspan="2">
								<input	type="text" 
										name="remarqueFournisseur" 
										value="<%=viewBean.getRemarqueFournisseur()%>" 
										style="width: 80%;" />
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
<%
	}
%>						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_DESC_FOURNISSEUR" />
							</td>
							<td colspan="2">
								<jsp:include page="widgetFournisseur_include.jsp">
									<jsp:param value="${viewBean.getDescFournisseur()}" name="descFournisseur"/>
								</jsp:include>
							</td>
							<td colspan="3">
								<ct:FWLabel key="JSP_RF_DEM_S_IS_CONVENTIONNE" />
								<img	src="<%=request.getContextPath()+viewBean.getImageIsConventionne()%>" 
										alt="" 
										id="isConventionneImg" 
										name="isConventionneImg" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_MOTIFS_DE_REFUS_PAS_DE_SELECTION" />
							</td>
							<td colspan="2">
								<select id="motifsRefusId" multiple="multiple" style="width: 400px;">
<%
	Vector<String[]> MotifsRefusParSousType = viewBean.getMotifsRefusParSousType();
	for (int i = 0; i < MotifsRefusParSousType.size(); i++) {
		if (MotifsRefusParSousType.get(i)[3].equals("false")) {
%>									<option value="<%=MotifsRefusParSousType.get(i)[0]%>" <%=viewBean.isChecked(MotifsRefusParSousType.get(i)[0])?"SELECTED":""%>>
										<%=MotifsRefusParSousType.get(i)[1]%>
									</option>
<%
		}
	}
%>								</select>
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_CONTRAT_DE_TAVAIL" />
								<input	type="checkbox" 
										value="on" 
										name="isContratDeTravail" 
										<%=viewBean.getIsContratDeTravail().booleanValue()?"CHECKED":""%> />
							</td>
						</tr>
<%
	if (viewBean.getIsAfficherCaseForcerPaiement()) {
%>					<ct:ifhasright element="<%=IRFActions.ACTION_FORCER_PAIEMENT%>" crud="crud">
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_FORCER_PAIEMENT" />
								&nbsp;&nbsp;
								<input	type="checkbox" 
										value="on" 
										name="isForcerPaiement" 
										<%=viewBean.getIsForcerPaiement().booleanValue()?"CHECKED":""%> />
							</td>
						</tr>
					</ct:ifhasright>
<%
	}
%>						<tr>
							<td>
								&nbsp;
							</td>
							<td colspan="5">
								<div id="resumeMotifsRefusId" style="height: 4em; overflow: auto; width: 421px;">
									<ul>
<%
	for (int i = 0; i <MotifsRefusParSousType.size(); i++) {
		if (viewBean.isChecked(((String[]) MotifsRefusParSousType.get(i))[0]) 
			&& ((String[]) MotifsRefusParSousType.get(i))[2].equals("false")) {
%>										<li>
											<%=((String[]) MotifsRefusParSousType.get(i))[1]%>
										</li>
<%
		}
	}
%>									</ul>
								</div>
								<input	type="hidden" 
										name="listMotifsRefusInput" 
										value="<%=viewBean.getListMotifsRefusInput()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr class="trContainerAdresse">
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_MONTANT_FACTURE" />
							</td>
							<td>
								<input	type="text" 
										name="montantFacture" 
										value="<%=viewBean.getMontantFacture()%>" 
										class="montant" 
										onkeypress="return filterCharForFloat(window.event);" />
							</td>
							<td align="right">
								<ct:FWLabel key="JSP_RF_DEM_S_ADRESSE_DE_PAIEMENT" />
							</td>
							<td colspan="3" rowspan="6">
<%
	if (!viewBean.getCodeSousTypeDeSoinList().equals(IRFCodeTypesDeSoins.SOUS_TYPE_13_6_AIDE_AU_MENAGE_AVANCE)) {
%>								<div data-g-adresse="service:findAdressePaiement, 
													initThreadContext: true">
									<input	id="idAdressePaiementDemande" 
											class="avoirPaiement.idTiers" 
											name="idAdressePaiementDemande" 
											value="<%=viewBean.getIdAdressePaiementDemande()%>" 
											type="hidden" />
									<input	id="idDomaineApplicatif" 
											class="avoirPaiement.idApplication" 
											name="idDomaineApplicatif" 
											value="<%=viewBean.getIdDomaineApplicatif()%> " 
											type="hidden" />
									<input	id="idAffilieAdressePaiment" 
											class="avoirPaiement.idExterne" 
											name="idAffilieAdressePaiment" 
											value="<%=viewBean.getIdAffilieAdressePaiment()%>" 
											type="hidden" />
								</div>
<%
	} else {
%>								<div class="adresseZone">
									<div class="adresseAffichee">
										<div class="adresse">	 
										</div>
									</div>	
								</div>
<%
	}
%>							</td>
						</tr>
						<tr>
							<td colspan="3">
								<table	border="0" 
										cellspacing="0" 
										cellpadding="0" 
										id="montantsMotifsRefusTabId">
<%
	for (int j = 0; j < MotifsRefusParSousType.size(); j++) {
		if (viewBean.isChecked(((String[]) MotifsRefusParSousType.get(j))[0]) 
			&& ((String[]) MotifsRefusParSousType.get(j))[2].equals("true")) {
%>									<tr>
										<td colspan="3">
											<%="*" + ((String[]) MotifsRefusParSousType.get(j))[1]%>
										</td>
									</tr>
									<tr>
										<td width="198px">
											&nbsp;
										</td>
										<td>
											<input	type="text" 
													name="champMontantMotifRefus_<%=((String[]) MotifsRefusParSousType.get(j))[0]%>" 
													value="<%=new FWCurrency((String) viewBean.getMontantsMotifsRefus().get(((String[]) MotifsRefusParSousType.get(j))[0])[0]).toStringFormat()%>" 
													class="montant" 
													onkeypress="return filterCharForFloat(window.event);" 
													onChange="demandeUtils.ajoutMontantCurrentMotifsRefus(this);" />
											<b style="color:#ff3300;">
												-
											</b>
										</td>
										<td>
											&nbsp;
										</td>
									</tr>
<%
		}
	}
%>								</table>
							</td>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td align="right" width="149px" colspan="5">
								<hr />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RF_DEM_S_MONTANT_A_PAYER" />
							</td>
							<td align="right" width="149px">
								<div id="idMontantAPayer">
								</div>
							</td>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<%if(viewBean.isGestionTexteRedirection()) { %>
						<TR>
							<TD colspan="2">&nbsp;</TD>
							<TD colspan="4">
			                	<ct:FWLabel key="JSP_RF_DEM_S_TEXTE_REDIRECTION"/>
			                	<INPUT type="checkbox" value="on" id="isTexteRedirection" name="isTexteRedirection" <%=viewBean.getIsTexteRedirection().booleanValue()?"CHECKED":""%>/>
			                </TD>
						</TR>
						<%} else {%>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<%}%>
<%
	if (viewBean.getIsAfficherDetail()) {
%>						<tr>
							<td>
								<label for="csEtatDemande">
									<ct:FWLabel key="JSP_RF_DEM_S_ETAT" />
								</label>
							</td>
							<td colspan="2">
								<ct:FWListSelectTag	name="csEtatDemande" 
													data="<%=viewBean.getCsEtatData()%>" 
													defaut="<%=viewBean.getCsEtat()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="csSource">
									<ct:FWLabel key="JSP_RF_DEM_S_SOURCE" />
								</label>
							</td>
							<td colspan="2">
								<ct:FWListSelectTag	name="csSource" 
													data="<%=viewBean.getCsSourceData()%>" 
													defaut="<%=viewBean.getCsSource()%>" />
							</td>
						</tr>
<%
	}
%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%
	if (!viewBean.getIsAfficherDetail()) {
%>	<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
		<tr>
			<td bgcolor="#FFFFFF" align="right" height="18">
				<input style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN_ACCESSKEY"/>)"                   onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_FIN_DE_SAISIE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN_ACCESSKEY"/>"/>
				<input style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU_ACCESSKEY"/>)"           onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_NOUVELLE_SAISIE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU_ACCESSKEY"/>"/>
				<input style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE_ACCESSKEY"/>)"   onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE_ACCESSKEY"/>"/>
				<input style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE_ACCESSKEY"/>)"       onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_TYPE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE_ACCESSKEY"/>"/>
				<input style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE_ACCESSKEY"/>)" onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_FACTURE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE_ACCESSKEY"/>"/>
				<input style="background-color:#FF8B8B;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE_ACCESSKEY"/>)"       onclick="if(cancel()) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE_ACCESSKEY"/>"/>
			</td>
		</tr>
	</table>
<%
	}
%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
