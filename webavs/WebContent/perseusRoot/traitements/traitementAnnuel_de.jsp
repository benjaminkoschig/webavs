<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.perseus.vb.traitements.PFTraitementAnnuelViewBean"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	
	PFTraitementAnnuelViewBean viewBean = (PFTraitementAnnuelViewBean) session.getAttribute("viewBean");
	idEcran="PPF2041";

	userActionValue = "perseus.traitements.traitementAnnuel.executer";

	FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		showProcessButton = !processLaunched;
	}else{
		showProcessButton = false;
	}
	
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<script type="text/javascript">

	function validate() {
		return window.confirm("<%=objSession.getLabel("JSP_PF_TRAITEMENT_ANNUEL_CONFIRMATION")%>")
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
												
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MAIL"/> CCVD
			</td>
			<td>
				<ct:inputText name="adresseMailCCVD" style="width: 400px" defaultValue="<%=JadeThread.currentUserEmail() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MAIL"/> AGLau
			</td>
			<td>
				<ct:inputText name="adresseMailAGLAU" style="width: 400px" defaultValue="<%=JadeThread.currentUserEmail() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_TEXTE"/>
			</td>
			<td>
				<ct:inputText name="texteDecision" style="width: 400px" defaultValue="<%=viewBean.getTexteDecision() %>"/>
			</td>
		</tr>
		<!-- 
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MOIS"/>
			</td>
			<td>
				<ct:inputText name="mois" style="width: 400px" defaultValue="<%=viewBean.getMois() %>" readonly="true" disabled="true"/>
			</td>
		</tr>
		-->
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>