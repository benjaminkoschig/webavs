<%-- Page de recherche des décisions --%>
<%-- Creator: SCE, 6.10 --%>

<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="java.util.Calendar"%> 
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.pegasus.vb.decision.PCDecisionViewBean"%>
<%@page import="globaz.jade.admin.user.bean.JadeUser" %>
<%-- tpl:put name="zoneInit" --%>
<%
//recup parametre pour tri selon decision
String idDemandePc = null!=request.getParameter("idDemandePc")?request.getParameter("idDemandePc"):"";
String idDossier = null!=request.getParameter("idDossier")?request.getParameter("idDossier"):"";
String idDecision = null!=request.getParameter("idDecision")?request.getParameter("idDecision"):"";
String idDroit = null!= request.getParameter("idDroit")?request.getParameter("idDroit"):"";
String idVersionDroit = null!=request.getParameter("idVersionDroit")?request.getParameter("idVersionDroit"):"";
String idErrorMsg=request.getParameter("errorMsg");
String decisionErrorMsg = "";

if(!JadeStringUtil.isEmpty(idErrorMsg)){
	decisionErrorMsg=BusinessExceptionHandler.getErrorMessage(idErrorMsg,null);
	
}

// Les labels de cette page commence par la préfix "JSP_PC_DECISION_R"
idEcran="PPC0080";
rememberSearchCriterias = true;
bButtonNew = false;
%>
<%-- /tpl:put --%> 
<%@ include file="/theme/find/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/decision/liste.css"/>

<script type="text/javascript"><!--
	bFind = <%=!JadeStringUtil.isBlankOrZero(idDossier)||!JadeStringUtil.isBlankOrZero(idDemandePc)||
	           !JadeStringUtil.isBlankOrZero(idDroit)||!JadeStringUtil.isBlankOrZero(idVersionDroit)||
	           !JadeStringUtil.isBlankOrZero(idDecision)%>;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IPCActions.ACTION_DECISION+".lister"%>";
	var idDecision = "<%= idDecision %>";
	var idDroit = "<%= idDroit %>";
	
	//sur envoi 
	function onClickFind(){
		//Recup champ no dec deux partie
		var partAn = $('#inpNoDecAn').attr('value');
		var partInc= $('#inpNoDecInc').attr('value');

		
		//Chaine a setter sur champ hidden (que si un des deux champs n'est pas vide)
		if(partAn!="" || partInc!=""){
			var chaineNoDec = partAn+"-"+partInc;
			$('#noDecisionSearch').attr('value',chaineNoDec);
		}
	}
	
	function clearFields () {
		$('.clearable,#listDecisionsSearch\\.forCsSexe,#listDecisionsSearch\\.forPreparePar,#listDecisionsSearch\\.forValidePar,#partiallikeNss').val('');
		$('#listDecisionsSearch\\.forDansDernierLot').attr('checked', false);
		$('#partiallikeNumeroAVS').focus();
	}
	
	
	$(function(){
		$('#partiallikeNss').change(function(){
			$('#hiddenlikeNss').val($('[name=likeNss]').val());
		});

		// lien sur les demandes PC
		<%if(JadeStringUtil.isEmpty(idDemandePc)){%>
		$('#lblDemandeNo,#demandeNo').hide();
		<%}else{%>
		$('[for=forIdDemande]').wrap('<a id="toDemandeLink" href="" />');
		$('#toDemandeLink').click(function () {
			window.location.href='pegasus?userAction=pegasus.demande.demande.chercher&idDemandePc=<%=idDemandePc%>';
		});
		<%}%>
		
		// lien sur les droits
		<%if(JadeStringUtil.isEmpty(idDroit)){%>
		$('#lblDroitNo,#droitNo').hide();
		<%}else{%>
		$('[for=forIdDroit]').wrap('<a id="toDroitLink" href="" />');
		$('#toDroitLink').click(function () {
			window.location.href='pegasus?userAction=pegasus.droit.droit.chercher&idDroit=<%=idDroit%>';
		});
		<%}%>
		
		// lien sur les dossiers
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
		$('#lblDossier,#dossier').hide();
		<%}else{%>
		$('[for=forIdDossier]').wrap('<a id="toDossierLink" href="" />');
		$('#toDossierLink').click(function () {
			window.location.href='pegasus?userAction=pegasus.dossier.dossier.chercher&idDossier=<%=idDossier%>';
		});
		<%}%>
		
		<%if(JadeStringUtil.isEmpty(idVersionDroit)){%>
		$('#forVersionDroit,#inpForVersionDroit').hide();
		<%}%>
		
		
		var msgError="<%=decisionErrorMsg%>";
		if(msgError!=""){
			bFind = false;
			globazNotation.utils.consoleError(msgError);
		}
	});
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DECISION_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
				<%-- tpl:put name="zoneMain" --%>
				
				<TR>
					<TD>
						<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
							<TR>
								<TD>
									<LABEL for="likeNss"><ct:FWLabel key="JSP_PC_DECISION_R_NSS"/></LABEL>
								</TD>
								<TD>
									<nss:nssPopup avsMinNbrDigit="99"
											  nssMinNbrDigit="99"
											  newnss=""
											  name="likeNss"/>
									<input type="hidden" id="hiddenlikeNss"  name="listDecisionsSearch.likeNss" class="clearable"/>
								</TD>
								<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PC_DECISION_R_NOM"/></LABEL>&nbsp;</TD>
								<TD><INPUT type="text" name="listDecisionsSearch.likeNom" value="" class="clearable"></TD>
								<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PC_DECISION_R_PRENOM"/></LABEL>&nbsp;</TD>
								<TD><INPUT type="text" name="listDecisionsSearch.likePrenom" value="" class="clearable"></TD>
							</TR>
							<TR>
								<TD>&nbsp;</TD><TD>&nbsp;</TD>
								<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PC_DECISION_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
								<TD><input type="text" name="listDecisionsSearch.forDateNaissance" value="" data-g-calendar="mandatory:false" class="clearable"/>
								</TD>
								<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PC_DECISION_R_SEXE"/></LABEL>&nbsp;</TD>	
								<TD><ct:FWCodeSelectTag name="listDecisionsSearch.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><LABEL for="preparePar"><ct:FWLabel key="JSP_PC_DECISION_R_PREPPAR"/></LABEL>&nbsp;</TD>
								<TD><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="listDecisionsSearch.forPreparePar"/></TD>
								<TD><LABEL for="validePar"><ct:FWLabel key="JSP_PC_DECISION_R_VALIDPAR"/></LABEL></TD>
								<TD><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="listDecisionsSearch.forValidePar"/></TD>
								<TD><LABEL for="DepuisValidation"><ct:FWLabel key="JSP_PC_DECISION_R_DEPUISVALID"/></LABEL>&nbsp;</TD>
								<TD><input type="text" name="listDecisionsSearch.forDepuisValidation" value="" data-g-calendar="mandatory:false" class="clearable"/></TD>
							</TR>
							<TR>
								<TD><LABEL for="noDecision"><ct:FWLabel key="JSP_PC_DECISION_R_NODECISION"/></LABEL>&nbsp;</TD>
								<TD>
									<INPUT id="inpNoDecAn" maxlength="4" type="text" name="listDecisionsSearch.forNoDecisionAn" value="" class="clearable"/>-
								    <INPUT id="inpNoDecInc" maxlength="6" type="text" name="listDecisionsSearch.forNoDecisionInc" value="" class="clearable"/>
									<input type="hidden" id="noDecisionSearch" name="listDecisionsSearch.forNoDecision" value="" class="clearable"/>
								</TD>
								<TD><LABEL for="noDroit"><ct:FWLabel key="JSP_PC_DECISION_R_NODROIT"/></LABEL></TD>
								<TD><INPUT type="text" name="listDecisionsSearch.likeNoDroit" value=""></TD>
								<TD><LABEL for="noPcAccorde"><ct:FWLabel key="JSP_PC_DECISION_R_NOPCACC"/></LABEL>&nbsp;</TD>
								<TD><INPUT type="text" name="listDecisionsSearch.likeNoPcAccorde" value=""></TD>
							</TR>
							<TR>
								<TD><LABEL for="DepuisDebutDroit"><ct:FWLabel key="JSP_PC_DECISION_R_DEPDEBDROIT"/></LABEL>&nbsp;</TD>
								<TD><input type="text" name="listDecisionsSearch.forDepuisDebutDroit" value="" data-g-calendar="mandatory:false" class="clearable"/></TD>
								<TD><LABEL for="dansDernierLot"><ct:FWLabel key="JSP_PC_DECISION_R_DERNIERLOT"/></LABEL></TD>
								<TD><INPUT type="checkbox" id="listDecisionsSearch.forDansDernierLot" value="" /></TD>
								<TD><LABEL for="etat"><ct:FWLabel key="JSP_PC_DECISION_R_ETAT"/></LABEL>&nbsp;</TD>
								<TD><ct:FWCodeSelectTag name="listDecisionsSearch.forCsEtat" codeType="PCETADEC" defaut="" wantBlank="true"/></TD>
							</TR>	
							<tr>
								<TD><LABEL for="dateDecision"><ct:FWLabel key="JSP_PC_DECISION_R_DATEDEC"/></LABEL>&nbsp;</TD>
								<TD><input type="text" name="listDecisionsSearch.forDateDecision" value="" data-g-calendar="mandatory:false" class="clearable"/></TD>
								<td colspan="6"></td>
							</tr>
							<tr>
								<td><LABEL id="lblDemandeNo" for="forIdDemande"><ct:FWLabel key="JSP_PC_DECISION_R_DEMANDENO"/></LABEL></td>
								<td>
									<input type="text" id="demandeNo" name="demandeNo" value="<%=idDemandePc%>" class="disabled" readonly tabindex="-1"/>
									<input type="hidden" name="listDecisionsSearch.forDemande" value="<%= idDemandePc%>" />
								</td>
								<td><LABEL id="lblDroitNo" for="forIdDroit"><ct:FWLabel key="JSP_PC_DECISION_R_DROITNO"/></LABEL></td>
								<td>
									<input type="text" id="droitNo" name="droitNo" value="<%=idDroit%>" class="disabled" readonly tabindex="-1"/>
									<input type="hidden" name="listDecisionsSearch.forDroit" value="<%= idDroit%>" />
								</td>
								<td><LABEL id="lblDossier" for="forIdDossier"><ct:FWLabel key="JSP_PC_DECISION_R_DOSSIER"/></LABEL></td>
								<td>
									<input type="text" id="dossier" name="dossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"/>
									<input type="hidden" name="listDecisionsSearch.forDossier" value="<%= idDossier%>" />
								</td>
							</tr>
							<tr>
								<td><label id="forVersionDroit"><ct:FWLabel key="JSP_PC_DECISION_R_VERSIONDROIT"/></label></td>
								<td>
									<input id="inpForVersionDroit" type="text" name="listDecisionsSearch.forVersionDroit" value="<%= idVersionDroit%>" class="disabled" readonly tabindex="-1"/>
								</td>
							</tr>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
								<TD colspan="5">&nbsp;</TD>										
							</TR>	
							<input type="hidden" name="listDecisionsSearch.forVersionDroitApc" value="<%=idVersionDroit %>"/>
							<input type="hidden" name="listDecisionsSearch.forVersionDroitSup" value="<%=idVersionDroit %>"/>						
						</TABLE>						</TD>
				</TR>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%><%-- /tpl:insert --%>