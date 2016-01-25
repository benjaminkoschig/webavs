<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
	<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
// Les labels de cette page commence par le préfix "JSP_PF_DECISION_R"
	idEcran="PPF2302";
	rememberSearchCriterias = false;
	bButtonFind = false;
	
	String idDossier = "";
	if ((request.getParameter("idDossier") != null) && objSession.hasRight("perseus", FWSecureConstants.ADD)) {
		idDossier = request.getParameter("idDossier");
		actionNew =  servletContext + mainServletPath + "?userAction=" + IPFActions.ACTION_DOSSIER_INFORMATION_FACTURE + ".afficher&_method=add&idDossier="+idDossier;
		bButtonNew = true;
	} else {
		bButtonNew = false;
	}
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script>
	bFind = true;
	detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPFActions.ACTION_DOSSIER_INFORMATION_FACTURE%>.lister";
	
	$(function(){	
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
		<%}%>
	});
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="MENU_PF_OPTION_DOSSIERS_INFORMATION_FACTURE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td><label for="forIdDossier"><ct:FWLabel key="JSP_PF_IDDOSSIER_INFORMATION_FACTURE"/></label>
		</td>
		<td><input type="text" name="searchModel.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1">&nbsp;</TD>
		</td>
	</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
