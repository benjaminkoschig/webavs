<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page isELIgnored ="false" %>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean"%>
<%@page import="globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_D"
	idEcran="PRF0007";
	
	RFSaisieDemandeViewBean viewBean = (RFSaisieDemandeViewBean) session.getAttribute("viewBean");
	String showWarningsUrl = servletContext + "/cygnusRoot/demandes/warningModalDlg.jsp";
	
	autoShowErrorPopup = true;
	
	if (viewBean.getIsAfficherDetail()) {
		if (viewBean.getCsEtat().equals(IRFDemande.VALIDE) || 
			viewBean.getCsEtat().equals(IRFDemande.PAYE) || 
			viewBean.getCsEtat().equals(IRFDemande.ANNULE)){
			bButtonDelete = false;
			bButtonUpdate = false;
			bButtonValidate = false;
		}else{
			bButtonDelete = true;
			bButtonUpdate = true;
			bButtonValidate = true;
		}
		bButtonCancel = true;
		bButtonNew = false;
	}else{
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonValidate = false;
		bButtonCancel = false;
		bButtonNew = false;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<LINK rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/cygnusRoot/demandes/saisieDemande.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link type="text/css" href="<%=servletContext%>/theme/widget.css" rel="stylesheet" />
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>

<ct:menuChange displayId="options" menuId="cygnus-optionsdemandes" showTab="menu">
<% if (viewBean.getCsEtat().equals(IRFDemande.PAYE) || 
		(viewBean.getCsEtat().equals(IRFDemande.VALIDE) &&  viewBean.getIsPP() && 
			viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS) &&
				viewBean.getCodeSousTypeDeSoinList().equals(IRFCodeTypesDeSoins.SOUS_TYPE_17_1_FRANCHISE_ET_QUOTEPARTS))) {%>
	<ct:menuActivateNode active="yes" nodeId="demandes_correction"/>
<%}else{%>
	<ct:menuActivateNode active="no" nodeId="demandes_correction"/>
<%}%> 		
<% if (viewBean.getCsEtat().equals(IRFDemande.ENREGISTRE)) {%>
	<ct:menuActivateNode active="yes" nodeId="demandes_imputerSurQd"/>
<%}else{%>
	<ct:menuActivateNode active="no" nodeId="demandes_imputerSurQd"/>
<%}%> 
</ct:menuChange>
<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>" menuId="cygnus-optionsdemandes"/>
<ct:menuSetAllParams key="idDemande" value="<%=viewBean.getIdDemande()%>" menuId="cygnus-optionsdemandes"/>
<ct:menuSetAllParams key="codeTypeDeSoin" value="<%=viewBean.getCodeTypeDeSoinList()%>" menuId="cygnus-optionsdemandes"/>

<script language="JavaScript">

	<%=viewBean.getMotifsRefusInnerJavascript()%>
	<%=viewBean.getMotifsRefusAssociationInnerJavascript()%>
	var idAdressePaiementDemande = '<%=viewBean.getIdAdressePaiementDemande()%>'
	
	function cancel() {
		//document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS%>.chercher";   	
    	document.forms[0].elements('userAction').value='<%=IRFActions.ACTION_SAISIE_DEMANDE+ ".afficher"%>';
    	
    	return true;
	}  
	
	function validate(){
		<%if(IRFDemande.CALCULE.equals(viewBean.getCsEtat()) && !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
			if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_ANNULER_PREPARER_DECISION_MESSAGE_INFO'/>")){
		<%}%>
			$("[name=idTiers]").val($("input[type=radio][name=membreFamille]:checked").attr('value'));
			return true;
		<%if(IRFDemande.CALCULE.equals(viewBean.getCsEtat()) && !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
			}else{
				return false;
			}
		<%}%>
	}
	
	function del() {
		<%if(IRFDemande.CALCULE.equals(viewBean.getCsEtat()) && !FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
			if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_ANNULER_PREPARER_DECISION_MESSAGE_INFO'/>")){
		<%}else{%>
			if (window.confirm("<ct:FWLabel key='WARNING_RF_DEM_S_JSP_DELETE_MESSAGE_INFO'/>")){
		<%}%>
			document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.supprimer";
			document.forms[0].submit();
		}
	}

	function add(type) {
		document.getElementsByName("typeValidation")[0].value=type;
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.ajouter";
		
		$("[name=idTiers]").val($("input[type=radio][name=membreFamille]:checked").attr('value'));
		
		return true;
	}

	function upd() {
		document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE%>.modifier";
	}
	
	var hasAddShowErrors = false;
	function showErrors() {
		if (errorObj.text != "") {
			if(notationManager.b_started) {
				showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
			} else {
				if(!hasAddShowErrors) {
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
	
	function init(){
		document.forms[0].elements('_method').value = "add";
		
		<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
			onChangeIsPP();
			$('[name="isPP"]').change(function() {
				onChangeIsPP();
			});
			
			//Suite au sprint de décembre, la création de FRQP par PP est gérée uniquement par le batch des adaptations annuelles
			$('[name="isPP"]').attr('disabled', true);
			$('[name="isPP"]').attr('readOnly', true);
			
		<%}%>
		
		<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
			errorObj.text="<%=viewBean.getMessage()%>";
			showErrors();
			errorObj.text="";
		<%}else if(FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
			errorObj.text="<%=viewBean.getMessage()%>";
			var url="<%=showWarningsUrl%>";
			var valShowModalDialog;
			valShowModalDialog = showModalDialog(url,errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
			errorObj.text="";
			if (valShowModalDialog.isBack != "true"){
				$('[name="warningMode"]').val("true");
				<%if (!viewBean.getIsAfficherDetail()){%>
					if(add('<%=viewBean.getTypeValidation()%>')){
						 action(COMMIT);
					}
				<%}else{%>
					upd();
					if(validate()){
						 action(COMMIT);
					}
				<%}%>
			}
		<%}%>
	}
	
	<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
		function onChangeIsPP(){
			
			if ($("#idIsPP").prop("checked")){
				$('[class="frqp"]').hide();
				$('[class="frqp_pp"]').show();
			}else{
				$('[class="frqp"]').show();
				$('[class="frqp_pp"]').hide();
			}

		}
	<%}%>

	function typeSousTypeDeSoinListInit(){

		document.getElementsByName("codeTypeDeSoinList")[0].options[parseInt("<%=viewBean.getCodeTypeDeSoinList()%>",10)].selected = true;
		document.getElementsByName("codeTypeDeSoin")[0].value="<%=viewBean.getCodeTypeDeSoinList()%>";
		document.getElementsByName("imgCodeTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;
		document.getElementsByName("codeSousTypeDeSoin")[0].value="<%=viewBean.getCodeSousTypeDeSoinList()%>";
		document.getElementsByName("imgCodeSousTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;


		currentSousTypeDeSoinTab=sousTypeDeSoinTab[parseInt(document.getElementsByName("codeTypeDeSoin")[0].value,10)-1];
		currentCodeSousTypeList=currentSousTypeDeSoinTab[0];
		currentLibelleSousTypeList=currentSousTypeDeSoinTab[1];
		codeSousTypeDeSoinList=document.getElementsByName("codeSousTypeDeSoinList")[0];
		codeSousTypeDeSoinList.options.length=0;
		codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option("","");
				
		for (i=0; i<currentCodeSousTypeList.length; i++){
			codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option(currentLibelleSousTypeList[i],currentCodeSousTypeList[i]);
		}

		codeSousTypeDeSoinList.options[parseInt("<%=viewBean.getCodeSousTypeDeSoinList()%>",10)].selected = true;
	}
	
	function postInit(){

		<%if (!viewBean.getIsAfficherDetail()) {%>
			action('add');
		<%}else{%>
			
			<%if (viewBean.isFocusMotifsDeRefus() || viewBean.isFocusAdressePaiement()){%>
				action('add');
			<%}else{%>
				action('read');
			<%}%>
			
			document.getElementById("csEtatDemande").disabled=true;
			document.getElementById("csEtatDemande").readOnly=true;
			document.getElementById("csSource").disabled=true;
			document.getElementById("csSource").readOnly=true;
		<%}%>

		document.getElementById("codeTypeDeSoinList").disabled=true;
		document.getElementById("codeTypeDeSoinList").readOnly=true;
		
		document.getElementById("codeSousTypeDeSoinList").disabled=true;
		document.getElementById("codeSousTypeDeSoinList").readOnly=true;
				
		document.getElementById("codeTypeDeSoin").disabled=true;
		document.getElementById("codeTypeDeSoin").readOnly=true;

		document.getElementById("codeSousTypeDeSoin").disabled=true;
		document.getElementById("codeSousTypeDeSoin").readOnly=true;
		
		<%if (!IRFDemande.ENREGISTRE.equals(viewBean.getCsEtat())) {%>
			document.getElementById("idGestionnaire").disabled=true;
			document.getElementById("idGestionnaire").readOnly=true;
		<%}%>
		
		<%if (!viewBean.isFocusMotifsDeRefus() && !viewBean.isFocusAdressePaiement()) {%>
			document.getElementById("dateReception").select();
		<%}else{%>
			<%if (viewBean.isFocusMotifsDeRefus()){%>
				document.getElementById("motifsRefusId").focus();
			<%}else if (viewBean.isFocusAdressePaiement()){%>
				document.getElementsByName("selecteurTiersAdressePaiement")[0].focus();
			<%}%>
		<%}%>
	}
	
	<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
		var dynamicCalendar = {
				
				$calendrierDateDebutTraitementMMAAAA: {},  
				$calendrierDateDebutTraitementJJMMAAAA: {},
				$calendrierDateFinTraitementMMAAAA: {},  
				$calendrierDateFinTraitementJJMMAAAA: {},
				$isPP: {},
				
				init: function () {	
					this.fillAttribut();
					this.addEvent();
					this.display(this.$isPP.get(0).checked);
				},
				
				fillAttribut: function () {
					this.$calendrierDateDebutTraitementJJMMAAAA = $("#dateDebutTraitementIdJJMMAAAA");
					this.$calendrierDateDebutTraitementMMAAAA = $("#dateDebutTraitementIdMMAAAA");
					this.$calendrierDateFinTraitementJJMMAAAA = $("#dateFinTraitementIdJJMMAAAA");
					this.$calendrierDateFinTraitementMMAAAA = $("#dateFinTraitementIdMMAAAA");
					this.$isPP = $("#idIsPP");
				},
				
				addEvent: function () {
					var that = this;
					this.$isPP.change(function () {
						that.display(this.checked);
					});
				},
				
				display: function (b_checked) {
					if (b_checked) {
						
						//Date de début de traitement
						this.$calendrierDateDebutTraitementJJMMAAAA.hide();
						this.$calendrierDateDebutTraitementJJMMAAAA.next().hide();
						
						var s_name_debut = this.$calendrierDateDebutTraitementJJMMAAAA.attr("name");
						this.$calendrierDateDebutTraitementJJMMAAAA.removeAttr("name");
						
						this.$calendrierDateDebutTraitementMMAAAA.show();
						this.$calendrierDateDebutTraitementMMAAAA.next().show();
						
						this.$calendrierDateDebutTraitementMMAAAA.attr("name",s_name_debut);
						
						//Date de fin de traitement
						this.$calendrierDateFinTraitementJJMMAAAA.hide();
						this.$calendrierDateFinTraitementJJMMAAAA.next().hide();
						
						var s_name_fin = this.$calendrierDateFinTraitementJJMMAAAA.attr("name");
						this.$calendrierDateFinTraitementJJMMAAAA.removeAttr("name");
						
						this.$calendrierDateFinTraitementMMAAAA.show();
						this.$calendrierDateFinTraitementMMAAAA.next().show();
						
						this.$calendrierDateFinTraitementMMAAAA.attr("name",s_name_fin);
					} else {
						
						//Date de début de traitement
						this.$calendrierDateDebutTraitementJJMMAAAA.show();
						this.$calendrierDateDebutTraitementJJMMAAAA.next().show();
						
						var s_name_debut = this.$calendrierDateDebutTraitementMMAAAA.attr("name");
						this.$calendrierDateDebutTraitementMMAAAA.removeAttr("name");
						
						this.$calendrierDateDebutTraitementMMAAAA.hide();
						this.$calendrierDateDebutTraitementMMAAAA.next().hide();
						
						this.$calendrierDateDebutTraitementJJMMAAAA.attr("name",s_name_debut);
						
						//Date de fin de traitement
						this.$calendrierDateFinTraitementJJMMAAAA.show();
						this.$calendrierDateFinTraitementJJMMAAAA.next().show();
						
						var s_name_fin = this.$calendrierDateFinTraitementMMAAAA.attr("name");
						this.$calendrierDateFinTraitementMMAAAA.removeAttr("name");
						
						this.$calendrierDateFinTraitementMMAAAA.hide();
						this.$calendrierDateFinTraitementMMAAAA.next().hide();
						
						this.$calendrierDateFinTraitementJJMMAAAA.attr("name",s_name_fin);
					}
				}
			};
		
		
		jsManager.addAfter(function () {
			
			function asyc() {
				var  i = 0, that = this;
				while (!$.ui.datepicker && i < 1000) {
					i++;
				}
				if (!$.ui.datepicker) {
					setTimeout(function () {
						asyc();
					}, 10);
				} else {
					dynamicCalendar.init();
				}
			}
		
			asyc();
		}, "execution après l'init de la notation");
	
	<%}%>
	
</script>
<style>
.widgetAdmin {
width: 320px;
}
.trContainerAdresse td{
 vertical-align: top;
}
</style>
<script type="text/javascript" src="cygnusRoot/scripts/demande/demande.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_DEM_S_DEF_TITRE"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				<%if (viewBean.getIsAfficherDetail()){%>
					<TR>
						<TD width="200px"><ct:FWLabel key="JSP_RF_DEM_S_NUMERO"/></TD>
						<TD colspan="5">
							 <B><%=viewBean.getIdDemande()%></B>
						</TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
				<%}%>
				<TR>
					<TD width="199px"><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
					<TD colspan="5">
						<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?																														viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR style="vertical-align:top" >
					<TD><ct:FWLabel key="JSP_RF_DEM_S_TIERS"/></TD>
					<TD colspan="5">
						<INPUT type="hidden" id="idTiers" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
						<INPUT type="hidden" id="nss" name="nss" value="<%=viewBean.getNss()%>" />
						<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>" />
						<INPUT type="hidden" name="idDemande" value="<%=viewBean.getIdDemande()%>" />
						<INPUT type="hidden" name="warningMode" value="<%=viewBean.getWarningMode()%>" />
						<INPUT type="hidden" id="idFournisseurDemande" name="idFournisseurDemande" value="<%=viewBean.getIdFournisseurDemande()%>" />
						<INPUT type="hidden" id="nom" name="nom" value="<%=viewBean.getNom()%>" />
						<INPUT type="hidden" id="prenom" name="prenom" value="<%=viewBean.getPrenom()%>" />
						<INPUT type="hidden" id="dateNaissance" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>" />
						<INPUT type="hidden" id="csSexe" name="csSexe" value="<%=viewBean.getCsSexe()%>" />
						<INPUT type="hidden" id="csNationalite" name="csNationalite" value="<%=viewBean.getCsNationalite()%>" />
						<INPUT type="hidden" name="csCanton" value="<%=viewBean.getCsCanton()%>" />
						<INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>" />
						<INPUT type="hidden" name="isConventionne" value="<%=viewBean.isConventionne()%>" />
						<INPUT type="hidden" name="typeValidation" value="" />
						<INPUT type="hidden" name="montantAPayer" value="" />
						<INPUT type="hidden" id="typeDemande" name="typeDemande" value="<%=RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FQP17_FRE18%>" />
						<INPUT type="hidden" name="hasMotifRefusDemande" value="<%=viewBean.isHasMotifRefusDemande()%>" />
						<INPUT type="hidden" name="isAfficherDetail" value="<%=viewBean.getIsAfficherDetail()%>" />
						<INPUT type="hidden" name="isAfficherCaseForcerPaiement" value="<%=viewBean.getIsAfficherCaseForcerPaiement()%>" />
						<INPUT type="hidden" name="isAfficherRemFournisseur" value="<%=viewBean.getIsAfficherRemFournisseur()%>" />
						<INPUT type="hidden" id="idQdPrincipale" name="idQdPrincipale" value="<%=viewBean.getIdQdPrincipale()%>" />
						<INPUT type="hidden" id="motifsDeRefusPasDeSelection" value="<%=viewBean.getSession().getLabel("JSP_RF_DEM_S_MOTIFS_DE_REFUS_PAS_DE_SELECTION")%>" />
						<INPUT type="hidden" id="motifsDeRefusSelection" value="<%=viewBean.getSession().getLabel("JSP_RF_DEM_S_MOTIFS_DE_REFUS_SELECTIONNES")%>" />
						<INPUT type="hidden" id="csEtatEnregistre" value="<%=IRFDemande.ENREGISTRE%>" />
						<INPUT type="hidden" id="csTypeDemandeDefaut" value="<%=RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEFAUT%>" />
						
						<!-- si l'état de la demande est différent d'enregistré on ne peut plus modifier le bénéficiaire -->
						<TABLE id="descFamilleCc" cellpadding="2" cellspacing="2" >
						</TABLE>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                
                <%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
                <TR><TD colspan="6"><INPUT type="hidden" name="isSaisieDemande" value="true"/></TD></TR>
                
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR id="descQd">
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_RECEPTION"/></TD>
					<TD>
						<input data-g-calendar=" "  name="dateReception" value="<%=JadeStringUtil.isEmpty(viewBean.getDateReception())?JACalendar.todayJJsMMsAAAA():viewBean.getDateReception()%>"/>
					</TD>
							
					<TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_DU_DECOMPTE"/></TD>
					<TD>
						<input data-g-calendar=" "  name="dateDecompte" value="<%=viewBean.getDateDecompte()%>"/>
					</TD>
					
					<!--  is retroactif -->
					<TD><ct:FWLabel key="JSP_RF_DEM_S_IS_RETRO"/>
						<input type="checkbox"  value="on" id="isRetro" name="isRetro" <%=viewBean.getIsRetro().booleanValue()?"CHECKED":""%>/>
						
					</TD>	
				</TR>
				<TR>
					
					<TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_DEBUT_TRAITEMENT"/></TD>
					<TD>
						<input data-g-calendar=" "  name="dateDebutTraitement" id="dateDebutTraitementIdJJMMAAAA" value="<%=viewBean.getDateDebutTraitement()%>"/>
						<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
							<input data-g-calendar="type:month"  name="dateDebutTraitement" id="dateDebutTraitementIdMMAAAA" value="<%=viewBean.getDateDebutTraitement()%>"/>
						<%}%>
					</TD>
					
					
					<TD><ct:FWLabel key="JSP_RF_DEM_S_DATE_FIN_TRAITEMENT"/></TD>
					<TD colspan="3">
						<input data-g-calendar=" "  name="dateFinTraitement" id="dateFinTraitementIdJJMMAAAA" value="<%=viewBean.getDateFinTraitement()%>"/>
						<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
							<input data-g-calendar="type:month"  name="dateFinTraitement" id="dateFinTraitementIdMMAAAA" value="<%=viewBean.getDateFinTraitement()%>"/>
						<%}%>
					</TD>
				</TR>
				
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_NO_DU_DECOMPTE"/></TD>
					<TD colspan="2">
						<INPUT type="text" name="numeroDecompte" value="<%=viewBean.getNumeroDecompte()%>" style="width: 80%;"/>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<%if(viewBean.getIsAfficherRemFournisseur()){%>
	                <TR>
		                <TD><ct:FWLabel key="JSP_RF_DEM_S_REM_FOURNISSEUR"/></TD>
		                <TD colspan="2">
		                	  <INPUT type="text" name="remarqueFournisseur" value="<%=viewBean.getRemarqueFournisseur()%>" style="width: 80%;"/>
		                </TD>
		                <TD colspan="3">&nbsp;</TD>
	                </TR>
                <%} %>
				<TR>
                    <TD><ct:FWLabel key="JSP_RF_DEM_S_DESC_FOURNISSEUR"/></TD>
                    <TD colspan="2">
						<jsp:include page="widgetFournisseur_include.jsp">
							<jsp:param value="${viewBean.getDescFournisseur()}" name="descFournisseur"/>
						</jsp:include>
					</TD>
                    <TD colspan="3">
                    	<ct:FWLabel key="JSP_RF_DEM_S_IS_CONVENTIONNE"/>
                    	<IMG src="<%=request.getContextPath()+viewBean.getImageIsConventionne()%>" alt="" name="isConventionneImg">
                    </TD>
                    <TD>&nbsp;</TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_DEM_S_MOTIFS_DE_REFUS_PAS_DE_SELECTION"/></TD>
                    <TD colspan="2">
                        <SELECT id="motifsRefusId" multiple="multiple" style="width: 400px;">
                        <%
                        	Vector<String[]> MotifsRefusParSousType = viewBean.getMotifsRefusParSousType();
                                                  for (int i=0;i<MotifsRefusParSousType.size();i++){
                                                	  if (MotifsRefusParSousType.get(i)[3].equals("false")){
                        %>
                        	<OPTION value="<%=MotifsRefusParSousType.get(i)[0]%>" <%=viewBean.isChecked(MotifsRefusParSousType.get(i)[0])?"SELECTED":""%>><%=MotifsRefusParSousType.get(i)[1]%></OPTION>
                        <%
                                                	  }}
                        %>
                        </SELECT>
                    </TD>
                    <TD></TD>
                	<TD colspan="1">
                		<ct:FWLabel key="JSP_RF_DEM_S_CONTRAT_DE_TAVAIL"/>
                		<INPUT type="checkbox" value="on" name="isContratDeTravail" <%=viewBean.getIsContratDeTravail().booleanValue()?"CHECKED":""%>/>
                	</TD>
                </TR>
   	             <%if(viewBean.getIsAfficherCaseForcerPaiement()){%>  
	            	<ct:ifhasright element="<%=IRFActions.ACTION_FORCER_PAIEMENT%>" crud="crud">
	                	<TR><TD colspan="6">&nbsp;</TD></TR>
	                	<TR>
	                		<TD colspan="4"></TD>
	                		<TD colspan="1">                		                		
	                			<ct:FWLabel key="JSP_RF_DEM_S_FORCER_PAIEMENT" />&nbsp;&nbsp;
	                			<INPUT type="checkbox" value="on" name="isForcerPaiement" <%=viewBean.getIsForcerPaiement().booleanValue()?"CHECKED":""%>/>                		
	                		</TD>	
	                	</TR>
	                </ct:ifhasright>
             	<%}%> 
                <TR>
                	<TD>&nbsp;</TD>
                	<TD colspan="5">
                		<DIV id="resumeMotifsRefusId" >
                			<UL>
	                			<%for (int i=0;i<MotifsRefusParSousType.size();i++){
	                			    	if (viewBean.isChecked(((String[]) MotifsRefusParSousType.get(i))[0]) &&
	                			    		((String[]) MotifsRefusParSousType.get(i))[2].equals("false")){%>
	                					<li><%=((String[]) MotifsRefusParSousType.get(i))[1]%></li>
	                			<%}}%>
	                		</UL>
                		</DIV>
                		<INPUT type="hidden" name="listMotifsRefusInput" value="<%=viewBean.getListMotifsRefusInput()%>"/>
                	</TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR> 
                <TR><TD colspan="6">&nbsp;</TD></TR>           
                <TR class="trContainerAdresse">
					<TD class="frqp"><ct:FWLabel key="JSP_RF_DEM_S_MONTANT_DU_DECOMPTE"/></TD>
					 <!-- FRQP payée mensuellement -->
					 <%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
						<TD width="200px" class="frqp_pp"><ct:FWLabel key="JSP_RF_DEM_S_MONTANT_PERIODE_DE_TRAITEMENT"/></TD>
					<%}%>
					<TD>
						<INPUT type="text" name="montantDecompte" value="<%=viewBean.getMontantDecompte()%>" class="montant" onkeypress="return filterCharForFloat(window.event);"/>
					</TD>
					<TD align="right">
						<ct:FWLabel key="JSP_RF_DEM_S_ADRESSE_DE_PAIEMENT"/>
                	</TD>
                	<TD colspan="3" rowspan="6">
                   		<div data-g-adresse="service:findAdressePaiement, 
                   								initThreadContext: true">   
							<input id="idAdressePaiementDemande" 
								   class="avoirPaiement.idTiers" 
								   name="idAdressePaiementDemande" 
								   value="<%=viewBean.getIdAdressePaiementDemande()%>" 
								   type="hidden">    
							<input id="idDomaineApplicatif" 
								   class="avoirPaiement.idApplication" 
								   name="idDomaineApplicatif" 
								   value="<%=viewBean.getIdDomaineApplicatif()%> " 
								   type="hidden">    
							<input id="idAffilieAdressePaiment" 
								   class="avoirPaiement.idExterne" 
								   name="idAffilieAdressePaiment" 
								   value="<%=viewBean.getIdAffilieAdressePaiment()%>" 
								   type="hidden">
						</div>
                	</TD>
				</TR>
				<TR>
					<TD colspan="3">
						<TABLE border="0" cellspacing="0" cellpadding="0" id="montantsMotifsRefusTabId">
							<%for (int j=0;j<MotifsRefusParSousType.size();j++){
	                			if (viewBean.isChecked(((String[]) MotifsRefusParSousType.get(j))[0]) && 
	                			    ((String[]) MotifsRefusParSousType.get(j))[2].equals("true")){%>
	                			    	<TR>
	                						<TD colspan="3"><%="*"+((String[]) MotifsRefusParSousType.get(j))[1]%></TD>
	                					</TR>
	                					<TR>
	                						<TD width="198px"></TD>
	                						<TD><INPUT type="text" name="champMontantMotifRefus_<%=((String[]) MotifsRefusParSousType.get(j))[0]%>" 
	                								    value="<%=new FWCurrency((String) viewBean.getMontantsMotifsRefus().get(((String[]) MotifsRefusParSousType.get(j))[0])[0]).toStringFormat()%>" 
	                								   class="montant"  onkeypress="return filterCharForFloat(window.event);" 
	                								   onChange="demandeUtils.ajoutMontantCurrentMotifsRefus(this);"/>
												<b style="color:#ff3300;">-</b>
											</TD>
											<TD></TD>
	                					</TR>
	                		<%}}%>
						</TABLE>
					</TD>
					<TD colspan="3"></TD>
				</TR>
				<TR>
					<TD>&nbsp;</TD>
					<TD align="right" width="149px" colspan="5"><HR></HR></TD>
				</TR>
				<TR>
				
					<TD class="frqp"><ct:FWLabel key="JSP_RF_DEM_S_MONTANT_A_PAYER"/></TD>
					<!-- FRQP payée mensuellement -->
					<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
						<TD class="frqp_pp" width="200px"><ct:FWLabel key="JSP_RF_DEM_S_MONTANT_PERIODE_DE_TRAITEMENT_A_PAYER"/></TD>
					<%}%>  
					
					<TD align="right" width="149px"><div id="idMontantAPayer"></div></TD>
					<TD colspan="4">&nbsp;</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
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
				<TR>
                	<TD><ct:FWLabel key="JSP_RF_DEM_S_GENRE_SOIN"/></TD>
					<TD>
						<ct:FWListSelectTag name="csGenreDeSoin" data="<%=viewBean.getCsGenreDeSoinData()%>" defaut="<%=viewBean.getCsGenreDeSoin()%>"/>
					</TD>
					<%if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())){%>
						<TD colspan="4">
	                		<ct:FWLabel key="JSP_RF_DEM_S_PP"/>
	                		<INPUT type="checkbox" value="on" id="idIsPP" name="isPP" <%=viewBean.getIsPP().booleanValue()?"CHECKED":""%>/>
	                	</TD>
					<%}%>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <%if (viewBean.getIsAfficherDetail() /*&& !viewBean.isRetourDepuisPyxis()*/){%>
                	<TR>
		                <TD>
							<LABEL for="csEtatDemande"><ct:FWLabel key="JSP_RF_DEM_S_ETAT"/></LABEL>
						</TD>
						<TD>
							<ct:FWListSelectTag name="csEtatDemande" data="<%=viewBean.getCsEtatData()%>" defaut="<%=viewBean.getCsEtat()%>"/>
						</TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD>
							<LABEL for="csSource"><ct:FWLabel key="JSP_RF_DEM_S_SOURCE"/></LABEL>
						</TD>
						<TD colspan="2">
							<ct:FWListSelectTag name="csSource" data="<%=viewBean.getCsSourceData()%>" defaut="<%=viewBean.getCsSource()%>"/>
						</TD>
					</TR>
                <%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%if (!viewBean.getIsAfficherDetail() /*!viewBean.getIsAfficherDetail() || viewBean.isRetourDepuisPyxis()*/){%>

	<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
	<TR>
		<TD bgcolor="#FFFFFF" align="right" height="18">
		
			<INPUT style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN_ACCESSKEY"/>)"                   onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_FIN_DE_SAISIE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_FIN_ACCESSKEY"/>"/>
			<INPUT style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU_ACCESSKEY"/>)"           onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_NOUVELLE_SAISIE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_NOUVEAU_ACCESSKEY"/>"/>
			<INPUT style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE_ACCESSKEY"/>)"   onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_ASSURE_ACCESSKEY"/>"/>
			<INPUT style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE_ACCESSKEY"/>)"       onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_TYPE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_TYPE_ACCESSKEY"/>"/>
			<INPUT style="background-color:#D4D0C8;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE_ACCESSKEY"/>)" onclick="if(add('<%=RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_FACTURE%>')) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_VALIDER_MEME_FACTURE_ACCESSKEY"/>"/>
			<INPUT style="background-color:#FF8B8B;width:210px;height:33px;font-size:11px;" type="button" value="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE"/>&#10(alt+<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE_ACCESSKEY"/>)"       onclick="if(cancel()) action(COMMIT); this.disabled=true;" accesskey="<ct:FWLabel key="JSP_RF_DEM_S_BUTTON_ANNULATION_SAISIE_ACCESSKEY"/>"/>
		
		</TD>
	</TR>
	</TABLE>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>