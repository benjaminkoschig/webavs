<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.perseus.vb.lot.PFComptabiliserLotViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	
	PFComptabiliserLotViewBean viewBean = (PFComptabiliserLotViewBean) session.getAttribute("viewBean");
	idEcran="PPF0731";

	userActionValue = "perseus.lot.comptabiliserLot.executer";

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
		return window.confirm("<%=objSession.getLabel("JSP_PF_COMPTABILISER_LOT_CONFIRMATION")%>")
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_COMPTABILISER_LOT_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_PAIEMENT_MENSUEL_MAIL"/>
			</td>
			<td>
				<ct:inputText name="adresseMail" style="width: 400px" defaultValue="<%=JadeThread.currentUserEmail() %>"/>
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_PAIEMENT_MENSUEL_MOIS"/>
			</td>
			<td>
				<ct:inputText name="mois" style="width: 400px" defaultValue="<%=viewBean.getLot().getSimpleLot().getDescription() %>" readonly="true" disabled="true"/>
			</td>
		</tr>
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>



