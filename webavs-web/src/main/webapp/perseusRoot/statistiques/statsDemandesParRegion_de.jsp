<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.perseus.vb.statistiques.PFStatsDemandesParRegionViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

	<%
		PFStatsDemandesParRegionViewBean viewBean = (PFStatsDemandesParRegionViewBean) session.getAttribute("viewBean");
		idEcran="PPF1721";
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
	var mois = function() {
		var monthStart =  $("#monthStart").val();
		var monthEnd = $("#monthEnd").val();
		if(!monthStart.length){
			monthStart = " ";
		}
		if(!monthEnd.length){
			monthEnd = " ";
		}
		return monthStart + "," + monthEnd;
	}
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_MOIS_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr height="30"><td></td></tr>
			<tr>
				<td width="20"></td>
				<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_MOIS_D_MOISDEBUT"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="monthStart" data-g-calendar="type:month,mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3) %>"/>
				</td>
			
				<td width="80"></td>
				<td>
					<label><ct:FWLabel key="JSP_PF_STATS_DEMANDES_PAR_MOIS_D_MOISFIN"/></label>
				</td>
				<td width="10"></td>
				<td>
					<input id="monthEnd" data-g-calendar="type:month,mandatory:true" value="<%=JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3) %>"/>
				</td>
				<td width="140"></td>
				<td colspan="2" align="center">
					<a data-g-download="docType:xls,
						parametres:,
						dynParametres:mois,
						serviceClassName:ch.globaz.perseus.business.services.doc.excel.ListeStatistiqueService,
						serviceMethodName:createStatsDemandesParRegionAndSave,
						docName:statsMensuelles"
					/>
				</td>
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
