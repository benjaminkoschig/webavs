<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.statistiques.PFStatsDetailsViewBean"%>
<%@page import="globaz.perseus.vb.statistiques.PFStatsMensuellesViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_LOT_"
	idEcran="PPF1751";
	PFStatsDetailsViewBean viewBean = (PFStatsDetailsViewBean) session.getAttribute("viewBean");
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAdresse = objSession.getUserEMail();
	
	userActionValue = "perseus.statistiques.statsDetails.executer";
	autoShowErrorPopup = true;
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		showProcessButton = !processLaunched;
	}else{
		showProcessButton = false;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">


function validate() {
	ok = true;
	if ($("#eMailAdresse").val() === "") {
		ok = false;
	}
	if ($("#dateDebut").val() === "") {
		ok = false;
	}
	if ($("#dateFin").val() === "") {
		ok = false;
	if (!ok) {
		alert("Les champs marqués d'une * sont obligatoires !");
	}
	return ok;
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_STATS_DEMANDES_DETAILS_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr>
				<td>
					<label style="font-weight: bold;" for=adrMail><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_EMAIL_GESTIONNAIRE" /></label>&nbsp;
				</td>
				<td width="30"></td>
				<td><input type="text" name="eMailAdresse" id="eMailAdresse"  value="<%=eMailAdresse%>" class="libelleLong" data-g-string="mandatory:true"></td>
				<td width="50"></td>
			</tr>
			<tr>
				<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_DETAILS_D_JOURDEBUT"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="dateDebut" name="dateDebut" data-g-calendar="type:month,mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3) %>" />
				</td>
			</tr>
			<tr>
			<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_DETAILS_D_JOURFIN"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="dateFin" name="dateFin" data-g-calendar="type:month,mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3) %>" />
				</td>
			
			</tr>
		</table>
	</td>
</tr>



						
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>