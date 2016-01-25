<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="ch.globaz.hera.business.models.famille.MembreFamille"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.api.IRFTypesBeneficiairePc"%>
<%@page import="globaz.cygnus.vb.qds.RFQdAbstractViewBean"%>
<%@page import="globaz.cygnus.api.qds.IRFQd"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.qds.RFSaisieQdViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCPCAccordee"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_QD_S"
	idEcran="PRF0020";

	RFSaisieQdViewBean viewBean = (RFSaisieQdViewBean) session.getAttribute("viewBean");
	
	//autoShowErrorPopup = true;
	
	String showModalDialogURLDetailSoldeExcedent= servletContext + "/cygnusRoot/qds/majSoldeExcedentDeRevenu_dialog.jsp?";
	String showModalDialogURLDetailSoldeCharge = servletContext + "/cygnusRoot/qds/majSoldeCharge_dialog.jsp?";
	String showModalDialogURLDetailAugmentation = servletContext + "/cygnusRoot/qds/majAugmentation_dialog.jsp?";
	String showModalDialogURLDetailPeriodeValidite = servletContext + "/cygnusRoot/qds/majPeriodeValiditeQdPrincipale_dialog.jsp?";	
	
	if (viewBean.isAfficherDetail()) {
		bButtonDelete 	= true;
		//bButtonUpdate 	= true;
		bButtonValidate = true;
		bButtonCancel 	= true;
		bButtonNew 		= false;
	}else{
		bButtonDelete 	= false;
		bButtonUpdate 	= false;
		bButtonValidate = true;
		bButtonCancel 	= true;
		bButtonNew 		= false;
	}
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsqds">
</ct:menuChange>
<script language="JavaScript">

<%@ include file="../utils/rfUtilsQd.js" %>

function readOnly(flag) {
	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
  for(i=0; i < document.forms[0].length; i++) {
      if (!document.forms[0].elements[i].readOnly &&
    	   //document.forms[0].elements[i].name != 'limiteAnnuelle' &&
    	   //document.forms[0].elements[i].name != 'isEtendue' &&
    	   //document.forms[0].elements[i].name != 'isPlafonnee' &&
	       document.forms[0].elements[i].type != 'hidden') {
          
          document.forms[0].elements[i].disabled = flag;
      }
  }
}

function cancel() {
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE%>.chercher";
}

function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.ajouter";
        
        //On récupère toutes les personnes prises dans le calcul
        var idTiersPersonnesPriseDansCalculInput = $("[name=idTiersPersonnesPriseDansCalcul]");
        idTiersPersonnesPriseDansCalculInput.val("");
        var personnesDansPlanCalculList = $("[name*=membreFamille_]:checked");        
    	$.each(personnesDansPlanCalculList,function (index, element) {
    		if (idTiersPersonnesPriseDansCalculInput.val() == ""){
    			idTiersPersonnesPriseDansCalculInput.val(element.value);
    		}else{
    			idTiersPersonnesPriseDansCalculInput.val(idTiersPersonnesPriseDansCalculInput.val()+","+element.value);
    		}
    	});
        
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.modifier";
    }
    return state;
}

function upd(){
	document.getElementById("modifierDetailSoldeCharge").style.display = "block";
	document.getElementById("modifierDetailAugmentation").style.display = "block";
	document.getElementById("modifierHistoriquePeriodeValidite").style.display = "block";
	document.getElementById("modifierDetailSoldeExcedent").style.display = "block";
}

function add(){
}

function init(){	
		
	onClickIsPlafonnee();

	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
		errorObj.text="<%=viewBean.getMessage()%>";
		showErrors();
		errorObj.text="";
	<%}else if(FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())){%>
		var warnObj = new Object();
		warnObj.text = "<%=viewBean.getMessage()%>";
	 	showWarnings(warnObj);
	<%}%>
}

function showWarnings(warnObj) {
	if (warnObj.text != "") {
		showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warnObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}

function postInit(){
		
	<%if (!viewBean.isAfficherDetail()) {%>
			action('add');
			
			$("[name=csEtat]").change(function() {		
				// si vaut cloturé
				if($("[name=csEtat]").val()=="<%=IRFQd.CS_ETAT_QD_CLOTURE%>"){
					//alors on met la date de fin à la date du jour			
					$("#dateFin").val("<%=JACalendar.today().toStr(".")%>");
				}
			});
			
			$("[name=dateDebut]").change(function () {
					$("[name=idPotDroitPC]").val("");
					$("[name=isLimiteAnnuelleImg]").attr("src","<%=request.getContextPath()+viewBean.getImageError()%>");
					$("[name=isFamilleOk]").val("false");
					$("[name=isLimiteAnnuelleOk]").val("false");
			});
			
			$("[name=csTypeBeneficiaire]").change(function () {
				$("[name=idPotDroitPC]").val("");
				$("[name=isLimiteAnnuelleImg]").attr("src","<%=request.getContextPath()+viewBean.getImageError()%>");
				$("[name=isLimiteAnnuelleOk]").val("false");
				majTypeHome();
			});
			
			$("[name=csGenrePCAccordee]").change(function () {
				$("[name=idPotDroitPC]").val("");
				$("[name=isLimiteAnnuelleImg]").attr("src","<%=request.getContextPath()+viewBean.getImageError()%>");
				$("[name=isLimiteAnnuelleOk]").val("false");
				majTypeHome();
			});
			
			<%if (viewBean.getIsDroitPC().booleanValue()){%>
				//document.getElementById("csGenrePCAccordee").disabled=true;
				//document.getElementById("csGenrePCAccordee").readOnly=true;
			
				document.getElementById("csTypeBeneficiaire").disabled=true;
				document.getElementById("csTypeBeneficiaire").readOnly=true;
				
				document.getElementById("csTypePCAccordee").disabled=true;
				document.getElementById("csTypePCAccordee").readOnly=true;
				
				document.getElementById("excedentPCAccordee").disabled=true;
				document.getElementById("excedentPCAccordee").readOnly=true;
				
				document.getElementById("soldeExcedentPCAccordee").disabled=true;
				document.getElementById("soldeExcedentPCAccordee").readOnly=true;
			<%}%>
			
	<%}else{%>
			action('read');
			
			document.getElementById("excedentPCAccordee").disabled=true;
			document.getElementById("excedentPCAccordee").readOnly=true;
			
			document.getElementById("soldeExcedentPCAccordee").disabled=true;
			document.getElementById("soldeExcedentPCAccordee").readOnly=true;
			
			document.getElementById("soldeCharge").disabled=true;
			document.getElementById("soldeCharge").readOnly=true;

			document.getElementById("augmentationQd").disabled=true;
			document.getElementById("augmentationQd").readOnly=true;
			
			document.getElementById("dateCreation").disabled=true;
			document.getElementById("dateCreation").readOnly=true;

			document.getElementById("idGestionnaire").disabled=true;
			document.getElementById("idGestionnaire").readOnly=true;

			//document.getElementById("anchor_dateCreation").disabled=true;
			//document.getElementById("anchor_dateCreation").readOnly=true;
			
			document.getElementById("modifierDetailSoldeExcedent").style.display = "none";
			document.getElementById("modifierDetailSoldeCharge").style.display = "none";
			
			document.getElementById("modifierDetailAugmentation").style.display = "none";
			document.getElementById("modifierHistoriquePeriodeValidite").style.display = "none";

			document.getElementById("isPlafonnee").disabled=true;
			document.getElementById("isPlafonnee").readOnly=true;
		
			document.getElementById("csTypeBeneficiaire").disabled=true;
			document.getElementById("csTypeBeneficiaire").readOnly=true;
			
			//document.getElementById("csGenrePCAccordee").disabled=true;
			//document.getElementById("csGenrePCAccordee").readOnly=true;
			
			<%if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutPCAccordee())){%>
				document.getElementById("csTypePCAccordee").disabled=true;
				document.getElementById("csTypePCAccordee").readOnly=true;
			<%}%>
			
			$("[name=csTypeBeneficiaire]").change(function () {
				majTypeHome();
			});
			
			$("[name=csGenrePCAccordee]").change(function () {
				majTypeHome();
			});
	<%}%>

	document.getElementById("csGenreQd").disabled=true;
	document.getElementById("csGenreQd").readOnly=true;

	document.getElementsByName("limiteAnnuelle")[0].disabled=true;
	document.getElementsByName("limiteAnnuelle")[0].readOnly=true;
	
	document.getElementsByName("csSource")[0].disabled=true;
	document.getElementsByName("csSource")[0].readOnly=true;
	
	majTypeHome();
	
	//document.getElementsByName("isEtendue")[0].readOnly=true;
	//document.getElementsByName("isEtendue")[0].disabled=true;
	
	//document.getElementsByName("isPlafonnee")[0].readOnly=true;
	//document.getElementsByName("isPlafonnee")[0].disabled=true;
	
	//document.getElementById("dateCreation").tabIndex="0";
	//document.getElementById("anchor_dateCreation").tabIndex="1";

	//document.getElementById("dateDebut").tabIndex="0";
	//document.getElementById("anchor_dateDebut").tabIndex="1";

	//document.getElementById("dateFin").tabIndex="0";
	//document.getElementById("anchor_dateFin").tabIndex="1";
	
}

function afficherHistoriqueSoldeExcedent(){
	var url=	"<%=showModalDialogURLDetailSoldeExcedent%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function afficherHistoriqueAugmentation(){
	var url=	"<%=showModalDialogURLDetailAugmentation%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function afficherHistoriqueSoldeCharge(){
	var url=	"<%=showModalDialogURLDetailSoldeCharge%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function afficherHistoriquePeriodeValidite(){
	var url=	"<%=showModalDialogURLDetailPeriodeValidite%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function modifierDetailSoldeExcedent(){
	
	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
	
}

function modifierDetailSoldeCharge(){
	
	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_SOLDE_CHARGE%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
	
}

function modifierDetailAugmentation(){

	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_AUGMENTATION%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
}

function modifierHistoriquePeriodeValidite(){

	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
}

function onClickIsPlafonnee(){
	
	if (document.getElementsByName("isPlafonnee")[0].checked){
		document.getElementById("tBodyMntResiduelId").style.display = "block";
		document.getElementById("tBodyAugmentationQdId").style.display = "block";
		document.getElementById("tBodyLimiteAnnuelleId").style.display = "block";
		document.getElementById("soldeChargeCharId").style.visibility='visible';
		<%if (viewBean.isAfficherDetail()){%>
			document.getElementById("soldeChargeCharId").innerHTML='-';
		<%}%>//else {
			//$("#btnMajLimiteAnnuelle").show();
		//<}%>

	}else{
		document.getElementById("tBodyMntResiduelId").style.display = "none";
		document.getElementById("tBodyAugmentationQdId").style.display = "none";
		document.getElementById("tBodyLimiteAnnuelleId").style.display = "none";
		document.getElementById("soldeChargeCharId").style.visibility='hidden';
		 <%if (viewBean.isAfficherDetail()){%>
			document.getElementById("soldeChargeCharId").innerHTML='+';
		<%}%>//else {
			//$("#btnMajLimiteAnnuelle").hide();
		//<}%>
	}

	
}

function majLimiteAnnuelle(){
	
	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.majLimiteAnnuelleQdDroitPC";
	document.forms[0].method = "get";
	document.forms[0].submit();
}

function del(){
	if (window.confirm("<ct:FWLabel key='WARNING_RF_QD_S_JSP_DELETE_MESSAGE_INFO'/>")){
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.supprimer";
		document.forms[0].submit();
	}
	
}

function majTypeHome(){
	
	<%if(viewBean.getIsAfficherTypeRemboursement().booleanValue()){%>
		if($("#csGenrePCAccordee").val() == <%=IPCPCAccordee.CS_GENRE_PC_HOME%>){
			
			if($("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE%> ||
					$("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME%> ||
					$("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.COUPLE_A_DOMICILE%> ||
					$("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS%>){
				
				$("div[id*='typeRemboursementRequerantDiv']").show();
				//$("div[id*='typeRemboursementConjointDiv']").show();
				$("div[id*='typeRemboursementConjointDiv']").hide();
				
			}else if ($("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS%> || 
					$("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES%>){	
				
				$("div[id*='typeRemboursementRequerantDiv']").show();
				$("div[id*='typeRemboursementConjointDiv']").hide();
				
			}else if ($("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.ENFANTS_AVEC_ENFANTS%>){
				
				$("div[id*='typeRemboursementRequerantDiv']").hide();
				$("div[id*='typeRemboursementConjointDiv']").hide();
			}
			
		}else{	
			if($("#csTypeBeneficiaire").val() == <%=IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE%>){
				$("div[id*='typeRemboursementRequerantDiv']").hide();
				//$("div[id*='typeRemboursementConjointDiv']").show();
				$("div[id*='typeRemboursementConjointDiv']").hide();
			}else{
				$("div[id*='typeRemboursementRequerantDiv']").hide();
				$("div[id*='typeRemboursementConjointDiv']").hide();
			}
		}
	<%}else{%>
		$("div[id*='typeRemboursementRequerantDiv']").hide();
		$("div[id*='typeRemboursementConjointDiv']").hide();
	<%}%>
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_QD_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

				
				<%
					//url GED pour le requérant
					String urlGED = servletContext + "/cygnus?" 
					+ "userAction=" + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + ".actionAfficherDossierGed" 
					+ "&noAVSId=" + viewBean.getNss() 
					+ "&idTiersExtraFolder=" + null 
					+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
				
				%>

<%--				<%
				//url GED pour les membres famille
				viewBean.setServletContextGed(servletContext + "/cygnus?");
				viewBean.setUserActionGed("userAction=" + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + ".actionAfficherDossierGed");
				viewBean.setIdTiersExtraFolderGed("&idTiersExtraFolder=" + null);
				viewBean.setNumAvsTiersGed("&noAVSId=");
				viewBean.setServiceNameGed("&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED));
				%>
--%>
 				<%if (viewBean.isAfficherDetail()){%>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_NO_QD"/></TD>
                	<TD colspan="5">
                		<B><%=viewBean.getIdQdPrincipale()%></B>
                	</TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <%}%>
				<TR>
					<TD width="200px"><ct:FWLabel key="JSP_RF_QD_S_GESTIONNAIRE"/></TD>
					<TD colspan="5">
						<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
                        																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
						<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>" />
						<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>" />
						<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
						<INPUT type="hidden" name="idQd" value="<%=viewBean.getIdQdPrincipale()%>" />
						<INPUT type="hidden" name="csGenreQdHistorique" value="<%=viewBean.getCsGenreQd()%>" />
						<INPUT type="hidden" name="anneeQd" value="<%=viewBean.getAnneeQd()%>" />
						<INPUT type="hidden" name="idPotDroitPC" value="<%=viewBean.getIdPotDroitPC()%>" />
						<INPUT type="hidden" name="isDroitPC" value="<%=viewBean.getIsDroitPC().booleanValue()%>" />
						<INPUT type="hidden" name="isLimiteAnnuelleOk" value="<%=viewBean.getIsLimiteAnnuelleOk()%>" />
						<INPUT type="hidden" name="isFamilleOk" value="<%=viewBean.getIsFamilleOk()%>" />
						<INPUT type="hidden" name="idTiersPersonnesPriseDansCalcul" value="" />
						<INPUT type="hidden" name="isAfficherTypeRemboursement" value="<%=viewBean.getIsAfficherTypeRemboursement().booleanValue()%>" />
						<INPUT type="hidden" name="isAfficherCaseRi" value="<%=viewBean.getIsAfficherCaseRi().booleanValue()%>" />
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR valign="top">
					<TD><ct:FWLabel key="JSP_RF_QD_S_TIERS"/></TD>
					<TD colspan="6">
					    <TABLE>
							<TD><%=viewBean.getDetailAssure()%></TD>
						<%--	<%if(!JadeStringUtil.isEmpty(viewBean.getIdQd())) {%> --%>
						
								<TD class="mtd" align="left"><br /><a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')"><ct:FWLabel key="JSP_LIEN_GED"/></a></TD>
						<%--	<%} %> --%>
						</TABLE>
					</TD>
				</TR>
					<%if (viewBean.hasMembreFamille()){%>
						<TR><TD colspan="6">&nbsp;</TD></TR>
		                <TR><TD colspan="6"><HR></HR></TD></TR>
		                <TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD valign="top" width="200px"><ct:FWLabel key="JSP_RF_QD_S_FAMILLE"/></TD>
							<TD colspan="5">
								<TABLE>				
									
									<%int inc = 0; %>
									<% for(String[] membre: viewBean.getMembresFamille()) {
										inc ++;
										if (!membre[0].equals(viewBean.getIdTiers())) {
											if (membre[8].equals(Boolean.TRUE.toString())) {
																				
												String situationFamiliale =  viewBean.getSession().getCodeLibelle(membre[1]);
												String nssTiers = membre[2];
												String nomTiers = membre[3];
												String prenomTiers = membre[4];
												String dateNaissanceTiers = membre[5];
												String sexeTiers = RFUtils.getLibelleCourtSexe(membre[6]);
												String nationaliteTiers = RFUtils.getLibellePays(membre[7], viewBean.getSession());
												String detailTiers = nomTiers +" "+ prenomTiers +" / "+ dateNaissanceTiers +" / "+ sexeTiers +" / "+ nationaliteTiers;
																						
												//url GED pour le requérant
												String urlGedMembreFamille = servletContext + "/cygnus?" 
													+ "userAction=" + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + ".actionAfficherDossierGed" 
													+ "&noAVSId=" + nssTiers 
													+ "&idTiersExtraFolder=" + null 
													+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
												
												%>
												<tr>
													<% if(!viewBean.isAfficherDetail()) {%>
													<td><INPUT type="checkbox" name="membreFamille_<%=inc%>" value="<%=membre[0]%>" checked="checked" /></td>
													<%}%>
													<td><em><%=situationFamiliale%> :</em></td>
													<td><b><%=nssTiers%></b> / </td>
													<td><%=detailTiers%> / </td>
													<td class="mtd" align="left"><a href="#" onclick="window.open('<%=urlGedMembreFamille%>','GED_CONSULT')"><ct:FWLabel key="JSP_LIEN_GED"/></a></td>
												</tr>
									<%}}}%>
								</TABLE>			
							</TD>
							<%-- <TD class="mtd" align="left"><a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">GED</a></TD> --%>					
						</TR>
					<%}%>
				<TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_TYPE_QD"/></TD>
					<TD colspan="5">
						<SELECT name="csGenreQd" onchange="onChangeTypeQd()">
						<%Vector<String[]> genreQdData = viewBean.getGenresQdData();
						for (int i=0;i<genreQdData.size();i++){
							if(((String[]) genreQdData.get(i))[0].equals(viewBean.getCsGenreQd())){%>
								<OPTION value="<%=((String[]) genreQdData.get(i))[0]%>" selected><%=((String[]) genreQdData.get(i))[1]%></OPTION>
							<%}else{%>
								<OPTION value="<%=((String[]) genreQdData.get(i))[0]%>"><%=((String[]) genreQdData.get(i))[1]%></OPTION>
							<%}%>
						<%}%>
						</SELECT>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR><TD colspan="6"><HR></HR></TD></TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>        	
               <TR>
               		<TD colspan="6">
                		<TABLE border="0" cellspacing="0" cellpadding="0">
                			<TR>
	                			<TD><ct:FWLabel key="JSP_RF_QD_S_EXCEDENT_RECETTE"/></TD>
	                			<TD colspan="5"><INPUT type="text" name="excedentPCAccordee" value="<%=viewBean.getExcedentPCAccordee()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/></TD>
               				</TR>
                			<TR>
			                	<TD width="200px"><ct:FWLabel key="JSP_RF_QD_S_SOLDE_EXCEDENT_RECETTE"/></TD>
			                	<TD><INPUT type="text" name="soldeExcedentPCAccordee" value="<%=viewBean.getSoldeExcedentPCAccordee()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/></TD>
								<%if (viewBean.isAfficherDetail()){%>
									<TD align="left" colspan="4">
										<TABLE border="0" cellspacing="3px" cellpadding="0">
											<TR>
												<TD>
													<!-- <a id="afficherHistoriqueSoldeExcedent"  data-g-externallink=" " href="showModalDialogURLDetailSoldeExcedent+"idQd="+viewBean.getIdQdPrincipale()">-->
													<a id="afficherHistoriqueSoldeExcedent" href="#" onclick="afficherHistoriqueSoldeExcedent();">
														<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
													</a>
												</TD>
												<TD>
													<a id="modifierDetailSoldeExcedent" href="#" onclick="modifierDetailSoldeExcedent();">
														<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
													</a>
												</TD>
											</TR>
										</TABLE> 
									</TD>
								 <%}else{%>
									<TD colspan="6"></TD>
								<%}%>
							</TR>
						</TABLE>
					</TD>	                
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_CREATION"/></TD>
                	<TD colspan="5"><input data-g-calendar=" "  name="dateCreation" value="<%=JadeStringUtil.isEmpty(viewBean.getDateCreation())?
                																	JACalendar.todayJJsMMsAAAA():viewBean.getDateCreation()%>"/></TD>
                </TR>
                 <%if (!viewBean.isAfficherDetail()){%>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_DEBUT"/></TD>
                	<TD><input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></TD>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_FIN"/></TD>
                	<TD><input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
                </TR>
                <%}else{%>
                	<!-- Liste des périodes de validité -->
                	<TR><TD colspan="6">&nbsp;</TD></TR>
                	<TR><TD colspan="6">&nbsp;</TD></TR>
                	<TR><TD colspan="6">&nbsp;</TD></TR>
                	<TR valign="top">
						<TD><ct:FWLabel key="JSP_RF_QD_S_LISTE_PERIODE_VALDITE"/></TD>
						<TD>
								<TABLE bgcolor="white"  border="1px" cellspacing="0" cellpadding="5px">
									<TR align="center"  style="border=1px solid black; ">
										<!-- <TH><ct:FWLabel key="JSP_RF_QD_S_LISTE_PERIODE_VALDITE_NO"/></TH>-->
										<TH><ct:FWLabel key="JSP_RF_QD_S_LISTE_PERIODE_VALDITE_DU"/></TH>
										<TH><ct:FWLabel key="JSP_RF_QD_S_LISTE_PERIODE_VALDITE_AU"/></TH>
										
									</TR>
									<!-- pour chaque ligne de la liste de Devis -->
									<%
										Vector<String[]> listPeriodesVal = viewBean.getPeriodesValidite();
										for (int i=0;i<listPeriodesVal.size();i++){
											String[] periode = (String[]) listPeriodesVal.get(i);%>
											<TR align="center">
												<!-- <TD style="border-right=1px solid #C0C0C0">periode[0]</TD>-->
												<TD style="border-right=1px solid #C0C0C0"><%=periode[1]%></TD>
												<TD style="border-right=1px solid #C0C0C0"><%=JadeStringUtil.isBlankOrZero(periode[2])?"-":periode[2]%></TD>
											</TR>
									<%}%>
								</TABLE>
						</TD>
					</TR>
					<TR>
						<TD></TD>
						<TD>
								<TABLE  border="0" cellspacing="3px" cellpadding="0">
									<TR>
										<TD>
											<a id="afficherHistoriquePeriodeValidite"  href="#" onclick="afficherHistoriquePeriodeValidite();">
												<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
											</a>
										</TD>
										<TD>
											<a id="modifierHistoriquePeriodeValidite" href="#" onclick="modifierHistoriquePeriodeValidite();">
												<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
											</a>
										</TD>
									</TR>
								</TABLE>
						</TD>              
					</TR>
         			<!-- Liste des périodes de validité -->
                <%}%>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD colspan="6">
	                	<TABLE border="0" cellspacing="0" cellpadding="0">
			                <TR>
			                	<TD width="200px"><ct:FWLabel key="JSP_RF_QD_S_IS_PLAFONNEE"/></TD>
			                	<TD><INPUT type="checkbox" name="isPlafonnee" onclick="onClickIsPlafonnee();" <%=viewBean.getIsPlafonnee().booleanValue()?"CHECKED":""%>/></TD>
			                </TR>
			                <TBODY id="tBodyLimiteAnnuelleId">
				                <TR>
				                	<TD ><ct:FWLabel key="JSP_RF_QD_S_LIMITE_ANNUELLE"/></TD>
				                	<TD><INPUT type="text" name="limiteAnnuelle" value="<%=viewBean.getLimiteAnnuelle()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/></TD>
				                	<%if (!viewBean.isAfficherDetail()){%>
				                		<TD><IMG src="<%=request.getContextPath()+viewBean.getImageLimiteAnnuelle()%>" alt="" name="isLimiteAnnuelleImg"></TD>
				                	<%}%>
				                </TR>
			                </TBODY>
			                <TR>
			                	<TD><ct:FWLabel key="JSP_RF_QD_S_SOLDE_CHARGE_RFM"/></TD>
			                	<TD>
									<INPUT type="text" name="soldeCharge" value="<%=viewBean.getSoldeCharge()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/>
								</TD>
								<TD>
									<B style="color:red;" id="soldeChargeCharId">-</B>
								</TD>
				                <%if (viewBean.isAfficherDetail()){%>
				                	<TD colspan="4" align="left">
										<TABLE>
				               				<TR>
												<TD>
													<a id="afficherHistoriqueSoldeCharge"  href="#" onclick="afficherHistoriqueSoldeCharge();">
														<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
													</a>
												</TD>
												<TD colspan="3" align="left">
													<a id="modifierDetailSoldeCharge" href="#" onclick="modifierDetailSoldeCharge();">
														<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
													</a>
												</TD>
											</TR>
										</TABLE>
									</TD>
				                <%}else{%>
				                	<TD></TD>
				                <%}%>	                
			                </TR>
			                <TBODY id="tBodyAugmentationQdId">
			                <TR>
			                	<TD><ct:FWLabel key="JSP_RF_QD_S_AUGMENTATION_QD"/></TD>
			                	<TD>
			                		<INPUT type="text" name="augmentationQd" value="<%=viewBean.getAugmentationQd()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/>
			                	</TD>
			                	<TD>
			                		<b style="color:blue;">+</b>
			                	</TD>
			                	<%if (viewBean.isAfficherDetail()){%>
			               			<TD align="left">
			               				<TABLE>
				               				<TR>
												<TD>
													<a id="afficherHistoriqueAugmentation"  href="#" onclick="afficherHistoriqueAugmentation();">
														<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
													</a>
												</TD>
												<TD colspan="3" align="left">
													<a id="modifierDetailAugmentation" href="#" onclick="modifierDetailAugmentation();">
														<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
													</a>
												</TD>
											</TR>
										</TABLE>
									</TD>
			                	 <%}else{%>
				                	<TD></TD>
				                <%}%>                	
			                </TR>
			                </TBODY>
			                <%if (viewBean.isAfficherDetail()){%>
					                <TR>
					                	<TD><ct:FWLabel key="JSP_RF_QD_S_CHARGE_RFM"/></TD>
					                	<TD align="right">
					                		<div><b><%=new FWCurrency(viewBean.getMontantChargeRfm()).toStringFormat()%></b></div>
					                	</TD>
					                	<TD>
					                		<b style="color:red;">-</b>			                	
					                	</TD>
					                </TR>
				            <%}%>
			                <TBODY id="tBodyMntResiduelId">
				                <TR><TD></TD><TD><HR></HR></TD></TR>
				                <TR>
				                	<TD><ct:FWLabel key="JSP_RF_QD_S_QD_RESIDUELLE"/></TD>
				                	<TD align="right"><div id="idMntResiduelAffiche"></div></TD>
				                	<TD><b style="color:black;">=</b></TD>         	
				                </TR>
			                </TBODY>
			                <TR><TD colspan="6">&nbsp;</TD></TR>
			                <TR><TD colspan="6">&nbsp;<INPUT type="hidden" name="mntCharge" value="<%=viewBean.getMontantChargeRfm()%>"/></TD></TR>
			           	</TABLE>
                	</TD>
                	</TR>
                	<TR>
						<TD><ct:FWLabel key="JSP_RF_QD_S_GENRE_PC_ACCORDEE"/></TD>
						<TD>
							<ct:FWListSelectTag name="csGenrePCAccordee" data="<%=viewBean.getGenresPCAccordeeData()%>" 
								defaut="<%=viewBean.getCsGenrePCAccordee()%>"/>
						</TD>
						<TD><ct:FWLabel key="JSP_RF_QD_S_TYPE_PC_ACCORDEE"/></TD>
						<TD>
							<ct:FWListSelectTag name="csTypePCAccordee" data="<%=viewBean.getTypesPcData()%>" 
								defaut="<%=viewBean.getCsTypePCAccordee()%>"/>
						</TD>
						<TD><ct:FWLabel key="JSP_RF_QD_S_DEGRE_API"/></TD>
						<TD>
							<ct:FWListSelectTag name="csDegreApi" data="<%=viewBean.getCsDegresApiData()%>" 
								defaut="<%=viewBean.getCsDegreApi()%>"/>
						</TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD><div id="typeRemboursementRequerantDiv"><ct:FWLabel key="JSP_RF_QD_S_TYPE_REMBOURSEMENT_HOME_REQUERANT"/></div></TD>
						<TD><div id="typeRemboursementRequerantDiv">
							<ct:FWListSelectTag name="typeRemboursementRequerant" data="<%=viewBean.getTypesRemboursementRequerantData()%>" 
								defaut="<%=viewBean.getTypeRemboursementRequerant()%>"/></div>
						</TD>
						<TD><div id="typeRemboursementConjointDiv"><ct:FWLabel key="JSP_RF_QD_S_TYPE_REMBOURSEMENT_HOME_CONJOINT"/></div></TD>
						<TD><div id="typeRemboursementConjointDiv">
							<ct:FWListSelectTag name="typeRemboursementConjoint" data="<%=viewBean.getTypesRemboursementConjointData()%>" 
								defaut="<%=viewBean.getTypeRemboursementConjoint()%>"/></div>
						</TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_QD_S_TYPE_BENEFICIAIRE"/></TD>
						<TD colspan="5">
							<ct:FWListSelectTag name="csTypeBeneficiaire" data="<%=viewBean.getTypesBeneficiaireData()%>" 
								defaut="<%=viewBean.getCsTypeBeneficiaire()%>"/>
							<IMG src="<%=request.getContextPath()+viewBean.getImageDroitPC()%>" alt="" name="isDroitPcImg">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<%if(viewBean.getIsAfficherCaseRi().booleanValue()){%>
								<ct:FWLabel key="JSP_RF_QD_S_EST_AU_RI"/>
								<INPUT type="checkbox" value="on" name="isRi" <%=viewBean.getIsRi().booleanValue()?"CHECKED":""%>/>
							<%}%>
						</TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_QD_S_ETAT_QD"/></TD>
						<TD>
							<ct:FWListSelectTag name="csEtat" data="<%=viewBean.getEtatsQdData(false)%>" defaut="<%=viewBean.getCsEtat()%>"/>
						</TD>
						<TD colspan="2"></TD>
						<TD><ct:FWLabel key="JSP_RF_QD_S_SOURCE_QD"/></TD>
						<TD>
							<ct:FWListSelectTag name="csSource" data="<%=viewBean.getSourcesQdData()%>" 
								defaut="<%=viewBean.isAfficherDetail()?viewBean.getCsSource():IRFQd.CS_SOURCE_QD_GESTIONNAIRE%>"/>
						</TD>
					</TR>
					<%if (!viewBean.isAfficherDetail()){%>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD></TD>
							<TD colspan="5">
		                		<TABLE border="0" cellspacing="0" cellpadding="0">
			                		<TD colspan="2">
			                			<INPUT type="button" id="btnMajLimiteAnnuelle" value="<ct:FWLabel key="JSP_RF_QD_S_MAJ_LIMITE_ANNUELLE_DROITPC"/>" onclick="majLimiteAnnuelle();">
			                		</TD>
							    </TABLE>
					    	</TD>
	                	</TR>
	                	<TR><TD colspan="6">&nbsp;</TD></TR>
                	<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>