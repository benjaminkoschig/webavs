
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.perseus.vb.statistiques.PFStatsDemandesJournalieresViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

	<%
		PFStatsDemandesJournalieresViewBean viewBean = (PFStatsDemandesJournalieresViewBean) session.getAttribute("viewBean");
		idEcran="PPF1771";
		autoShowErrorPopup = true;		
	
		bButtonDelete = false;
		bButtonUpdate = false;
				
	%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
	var jour = function() {
		var dayStart =  $("#dayStart").val();
		var dayEnd = $("#dayEnd").val();
		if(!dayStart.length){
			dayStart = " ";
		}
		if(!dayEnd.length){
			dayEnd = " ";
		}
		return dayStart + "," + dayEnd;
	}
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_JOUR_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr height="30"><td></td></tr>
			<tr>
				<td width="20"></td>
				<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_JOUR_D_JOURDEBUT"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="dayStart" data-g-calendar="mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()) %>" />
				</td>
			
				<td width="80"></td>
				<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_JOUR_D_JOURFIN"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="dayEnd" data-g-calendar="mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()) %>" />
				</td>
				<td width="140"></td>
				<% if(objSession.hasRight("perseus", FWSecureConstants.ADD)){%>
				<td colspan="2" align="center">
					<a data-g-download="docType:xls,
						parametres:,
						dynParametres:jour,
						serviceClassName:ch.globaz.perseus.business.services.doc.excel.ListeStatistiqueService,
						serviceMethodName:createStatsDemandesJournalieresAndSave,
						docName:statsJournalieres"
					/>
				</td>
				<% } %>
			</tr>
		</table>
	</td>
</tr>			
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
