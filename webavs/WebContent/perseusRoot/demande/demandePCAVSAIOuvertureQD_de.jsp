<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.perseus.vb.demande.PFDemandePCAVSAIOuvertureQDViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	PFDemandePCAVSAIOuvertureQDViewBean viewBean = (PFDemandePCAVSAIOuvertureQDViewBean) session.getAttribute("viewBean");
	idEcran="PPF0331";
	bButtonCancel = false;
	bButtonDelete = false;
	bButtonNew = false;
	bButtonUpdate = false;
	bButtonValidate = false;
	boolean bButtonValider = false;
	if (!JadeStringUtil.isEmpty(viewBean.getDetailEnfants())
			&& objSession.hasRight("perseus", FWSecureConstants.ADD)) {
		bButtonValider = true;
	}
	
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
<!--

//-->
	$(function() {
		<% if (!bButtonValider) { %>
			globazNotation.utils.consoleInfo("<div style='height:80px'><%= objSession.getLabel("JSP_PF_DEMPCAVSAI_D_AVERTISSEMENT_AUCUN_ENFANT") %></div>", "<%= objSession.getLabel("JSP_PF_DEMPCAVSAI_D_AVERTISSEMENT_AUCUN_ENFANT_TITRE") %>", true);
		<% } %>
	});
	
	function createQD() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DEMPCAVSAI_D_CREATION_QD_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="<%=IPFActions.ACTION_DEMANDE_PC_AVS_AI%>.modifier";
	        document.forms[0].submit();
	    }
	}
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DEMPCAVSAI_D_TITRE" /><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr>
							<td valign="top">
								<label style="font-weight: bold;" for="idDemande"><ct:FWLabel key="JSP_PF_DEMPCAVSAI_D_ID_DEMANDE" />&nbsp;</label>
							</td>
							<td valign="top">
								<%=viewBean.getDemandeId()%>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DEMPCAVSAI_D_REQUERANT" />&nbsp;</label>
							</td>
							<td valign="top">
								<%=viewBean.getDetailRequerant()%>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DEMPCAVSAI_D_ENFANTS" />&nbsp;</label>
							</td>
							<td valign="top">
								<%=viewBean.getDetailEnfants()%>
							</td>
						</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
<%if (bButtonValider) {%><input class="btnCtrl" id="btnValidate" type="button" value="<%=btnValLabel%>" onclick="createQD()"><%}%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
