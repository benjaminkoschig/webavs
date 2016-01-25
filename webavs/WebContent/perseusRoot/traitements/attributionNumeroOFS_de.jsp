<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.traitements.PFAttributionNumeroOFSViewBean"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	
	PFAttributionNumeroOFSViewBean viewBean = (PFAttributionNumeroOFSViewBean) session.getAttribute("viewBean");
	idEcran="PPF2011";

	userActionValue = "perseus.traitements.attributionNumeroOFS.executer";

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
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
		return window.confirm("<%=objSession.getLabel("JSP_PF_TRAITEMENT_ATTRIBUTION_NUMERO_OFS_CONFIRMATION")%>")
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_TRAITEMENT_ATTRIBUTION_NUMERO_OFS_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
		
		<tr>
			<td style="width: 200px">
				<ct:FWLabel key="JSP_PF_TRAITEMENT_ATTRIBUTION_NUMERO_OFS_MAIL"/>
			</td>
			<td>
				<ct:inputText name="adresseMail" style="width: 400px" defaultValue="<%=JadeThread.currentUserEmail() %>"/>
			</td>
		</tr>
												
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>