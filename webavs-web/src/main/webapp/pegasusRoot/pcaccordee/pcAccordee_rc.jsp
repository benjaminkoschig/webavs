<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/find/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss"%>
<%
	idEcran = "PPC0060";
	//Les labels de cette page commence par le préfix "JSP_PC_DEM_R"

	rememberSearchCriterias = true;

	bButtonNew = false;

	String idTiers = null != request.getParameter("idTiers") ? request
			.getParameter("idTiers") : "";
	String idDroit = null != request.getParameter("idDroit") ? request
			.getParameter("idDroit") : "";
	String noVersion = null != request.getParameter("noVersion")
			? request.getParameter("noVersion")
			: "";
	String whereKey = null != request.getParameter("whereKey")
			? request.getParameter("whereKey")
			: "";
	String idDemande = null != request.getParameter("idDemandePc")
			? request.getParameter("idDemandePc")
			: "";
	String idDossier = null != request.getParameter("idDossier")
			? request.getParameter("idDossier")
			: "";
	String isReadyForDac = null != request.getParameter("isReadyForDac")
			? request.getParameter("isReadyForDac")
					: "";
	String idVersionDroit = null != request.getParameter("idVersionDroit")
	? request.getParameter("idVersionDroit")
			: "";
	actionNew = servletContext + mainServletPath + "?userAction="
			+ IPCActions.ACTION_PCACCORDEE
			+ ".afficher&_method=add&idTiers=" + idTiers;
	
	//Warning du calculateur
	String idCalculWarningMsg=request.getParameter("calculWarningMsg");
	String listCalculWarnParams=request.getParameter("CalculWarnParams");
	String calculWarnMsg="";
	if(!JadeStringUtil.isEmpty(idCalculWarningMsg)){
		
		String[] params=null;
		
		if(!JadeStringUtil.isEmpty(listCalculWarnParams)){
			params=listCalculWarnParams.split(",");
		}
		
		calculWarnMsg=BusinessExceptionHandler.getErrorMessage(idCalculWarningMsg,params);
	}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT language="JavaScript">
	bFind = <%=!JadeStringUtil.isBlankOrZero(idDossier)||!JadeStringUtil.isBlankOrZero(idDemande)||!JadeStringUtil.isBlankOrZero(idDroit)||!JadeStringUtil.isBlankOrZero(idVersionDroit)%>;
	usrAction = "<%=IPCActions.ACTION_PCACCORDEE%>.lister";
	
	function clearFields () {
		$('.clearable,#pcAccordeesSearch\\.forCsSexe,#partialpcAccordeesSearch\\.likeNss,#hiddenlikeNumeroAVS,#pcAccordeesSearch\\.forCsEtat').val('');
		$('#partialpcAccordeesSearch\\.likeNss').focus();
	}
	
	$(function(){
		$('#forCacherHistorique').attr('checked','checked');
		
		<%if (JadeStringUtil.isEmpty(idDroit)) {%>
		$('#tdLblDroit,#tdInputDroit').hide();
		<%}else{%>
			$('[for=forIdDroit]').wrap('<a id="toDroitLink" href="" />');
			$('#toDroitLink').click(function () {
				window.location.href='pegasus?userAction=pegasus.droit.droit.chercher&idDroit=<%=idDroit%>';
			});
		<%}%>
		<%if (JadeStringUtil.isEmpty(noVersion)) {%>
		$('#forNoVersion,[for=forNoVersion]').hide().after('&nbsp;');
		<%} else {%>
		$('#forCacherHistorique,[for=forCacherHistorique]').hide();
		<%}%>
		
		//id demande vide
		<%if (JadeStringUtil.isEmpty(idDemande)) {%>
			$('#tdLblDemande,#tdInputDemande').hide();
		<%}else{%>
			$('[for=forIdDemande]').wrap('<a id="toDemandeLink" href="" />');
			$('#toDemandeLink').click(function () {
				window.location.href='pegasus?userAction=pegasus.demande.demande.chercher&idDemandePc=<%=idDemande%>';
			});
		<%}%>
		
		//id demande vide
		<%if (JadeStringUtil.isEmpty(idDemande)) {%>
			$('#tdLblDemande,#tdInputDemande').hide();
		<%}else{%>
			$('[for=forIdDemande]').wrap('<a id="toDemandeLink" href="" />');
			$('#toDemandeLink').click(function () {
				window.location.href='pegasus?userAction=pegasus.demande.demande.chercher&idDemandePc=<%=idDemande%>';
			});
		<%}%>
		
<%-- 		//id idVersionDroit 
		<%if (!JadeStringUtil.isEmpty(idVersionDroit)) {%>
			//$('#toDroitLink').click(function () {
				window.location.href='pegasus?userAction=pegasus.dossier.dossier.chercher&idVersionDroit=<%=idVersionDroit%>';
			//});
		<%}%> --%>
		//lien prep dac
		<%if(!JadeStringUtil.isEmpty(idDroit)&&(!JadeStringUtil.isEmpty(noVersion))&&(!JadeStringUtil.isEmpty(isReadyForDac))) {
		%>$('#linkToPrepDac').attr('href','pegasus?userAction=pegasus.decision.prepDecisionApresCalcul.afficher&idDroit=<%=idDroit%>&csTypeDecisionPrep=64042003&noVersion=<%=noVersion%>&idVersionDroit=<%=idVersionDroit%>');
		
		<%}else{
			%>
			$('#linkToPrepDac').hide();
		<%}%>
		
		//Warnings
		var msgWarn ="<%=calculWarnMsg%>";
		if(msgWarn!=""){
			globazNotation.utils.consoleWarn(msgWarn);
		}
	});
	
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_PC_PCACCORDEE_R_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<TR>
	<TD>
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%"
		class="rcFields">
		<TR>

			<TD><LABEL for="likeNss"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_NSS" /></LABEL>&nbsp;</TD>
			<TD><nss:nssPopup avsMinNbrDigit="99" nssMinNbrDigit="99"
				newnss="" name="pcAccordeesSearch.likeNss" /> <input type="hidden"
				id="hiddenlikeNumeroAVS" name="likeNss"></TD>
			<TD><LABEL for="likeNom"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_NOM" /></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" id="likeNom"
				name="pcAccordeesSearch.likeNom" class="clearable" value=""></TD>
			<TD><LABEL for="likePrenom"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_PRENOM" /></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" name="pcAccordeesSearch.likePrenom"
				class="clearable" value=""></TD>
		</TR>
		<TR>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<TD><LABEL for="forDateNaissance"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_DATE_NAISSANCE" /></LABEL>&nbsp;</TD>
			<TD><input type="text" id="forDateNaissance"
				name="pcAccordeesSearch.forDateNaissance" class="clearable" value=""
				data-g-calendar="mandatory:false" /></TD>
			<TD><LABEL for="forCsSexe"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_SEXE" /></LABEL>&nbsp;</TD>
			<TD colspan="3"><ct:FWCodeSelectTag
				name="pcAccordeesSearch.forCsSexe" codeType="PYSEXE" defaut=""
				wantBlank="true" /></TD>
		</TR>
		<TR>
			<TD colspan="6">&nbsp;</TD>
		</TR>
		<TR>
			<TD><LABEL for="forEtat"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_ETAT" /></LABEL></TD>
			<TD><ct:select styleClass="csEtat"
				name="pcAccordeesSearch.forCsEtat" id="pcAccordeesSearch.forCsEtat"
				wantBlank="true">
				<ct:optionsCodesSystems csFamille="PCETAPCA" />
			</ct:select></TD>
			<TD id="tdLblDemande"><LABEL for="forIdDemande"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_ID_DEMANDE" /></LABEL></TD>
			<td id="tdLblDroit"><LABEL for="forIdDroit"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_NO_DROIT" /></LABEL></TD>
			<td id="tdLblDossier">
				<LABEL for="forIdDossier"><ct:FWLabel key="JSP_PC_PCACCORDEE_R_NO_DOSSIER"/></LABEL></td>

			<td id="tdInputDemande"><INPUT type="text" id="forIdDemande"
				value="<%=idDemande%>" class="disabled" readonly tabindex="-1"></td>
			<td id="tdInputDroit"><INPUT type="text"
				name="pcAccordeesSearch.forIdDroit" id="forIdDroit"
				value="<%=idDroit%>" class="disabled" readonly tabindex="-1"></TD>
			<td id="tdInputDossier"><input type="text" id="forIdDossier" name="pcAccordeesSearch.forIdDossier"
				value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></td>

			<TD><LABEL for="forNoVersion"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_NO_VERSIONDROIT" /></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" name="pcAccordeesSearch.forNoVersion"
				id="forNoVersion" value="<%=noVersion%>" class="disabled" readonly
				tabindex="-1">
				<INPUT type="text" name="pcAccordeesSearch.forIdVersionDroit"
				id="forIdVersionDroit" value="<%=idVersionDroit%>" class="disabled" readonly
				tabindex="-1">
			</TD>

		</TR>
		<TR>
			<TD colspan="6">&nbsp;</TD>
		</TR>
		<TR>
			<TD><LABEL for="forCacherHistorique"><ct:FWLabel
				key="JSP_PC_PCACCORDEE_R_HISTORIQUE" /></LABEL>&nbsp;</TD>
			<TD><INPUT type="checkbox" id="forCacherHistorique"
				name="pcAccordeesSearch.forCacherHistorique" checked="checked" /> <input
				type="hidden" name="whereKey" value="<%=whereKey%>" /> <input
				type="hidden" name="idDemande" value="<%=idDemande%>" /> 
				<inpu type="hidden" name="idDossier" value="<%=idDossier%>" />
				<inpu type="hidden" name="idVersionDroit" value="<%=idVersionDroit%>" />
				</TD>
		</TR>
		<TR>
			<TD colspan="6">&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<input 	type="button" 
					onclick="clearFields()" 
					accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
					value="<ct:FWLabel key="JSP_EFFACER"/>">
				<label>
					[ALT+<ct:FWLabel key="AK_EFFACER"/>]
				</label>
			</TD>										
			<TD colspan="5">&nbsp;</TD>
			<td>
				<a id="linkToPrepDac"><ct:FWLabel key="JSP_PC_PCACCORDEE_R_LINK_DAC" />
				</a>
			</td>
		</TR>
	</TABLE>
	</TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
<%-- /tpl:insert --%>