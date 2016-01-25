<%@page import="ch.globaz.auriga.business.constantes.AUDecisionType"%>
<%@page import="ch.globaz.auriga.business.constantes.AUDecisionEtat"%>
<%@page import="globaz.auriga.vb.decisioncap.AUDecisionCapSearchLineViewBean"%>
<%@page import="globaz.auriga.vb.decisioncap.AUDecisionCapSearchAjaxViewBean"%>
<%@page language="java" contentType="text/xml;charset=ISO-8859-1"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<liste>
<%
AUDecisionCapSearchAjaxViewBean viewBean = (AUDecisionCapSearchAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	
	for(AUDecisionCapSearchLineViewBean line: viewBean.getLineViewBeans()) {
		String theActionDetail = "window.top.fr_main.window.location='auriga?userAction=auriga.decisioncap.decisionCap.afficher&amp;idAffilie=" + line.getIdAffiliation() +"&amp;id=" + line.getIdDecisionCap()+ "&amp;idDecisionCapRectifiee=" + line.getIdDecisionRectifiee() +"'";
		
		String lineStyle="";
		if(AUDecisionEtat.RECTIFIEE.getCodeSystem().equalsIgnoreCase(line.getCodeSystemEtat())){
			lineStyle = "color:#999999;";
		}
		
		lineStyle = "style='" + lineStyle + "'";
		
%>
		<tr <%=lineStyle%> idEntity="<%=line.getIdDecisionCap()%>" >
			<td>
				<ct:menuPopup menu="auriga-optionDecision">
					<ct:menuParam key="idDecisionCapRectifiee" value="<%=line.getIdDecisionCap()%>"/>
					<ct:menuParam key="idAffilie" value="<%=line.getIdAffiliation()%>"/>
					
					<% if (!AUDecisionEtat.COMPTABILISEE.equals(line.getCodeSystemEtat()) && !AUDecisionEtat.REPRISE.equals(line.getCodeSystemEtat())) { %>
						<ct:menuExcludeNode nodeId="option_rectificative" />
					<% } %>
					
					<% if (!((AUDecisionEtat.COMPTABILISEE.equals(line.getCodeSystemEtat()) || AUDecisionEtat.REPRISE.equals(line.getCodeSystemEtat()) ) && (AUDecisionType.PROVISOIRE_RECTIFICATIVE.equals(line.getType()) || AUDecisionType.PROVISOIRE.equals(line.getType())))) { %>
						<ct:menuExcludeNode nodeId="option_definitive" />
					<% } %>
					
				</ct:menuPopup>
			</td>
			<td class="center" onClick="<%=theActionDetail%>"><%=line.getAnnee()%></td>
			<td class="center" onClick="<%=theActionDetail%>"><%=line.getTypeLibelle()%></td>
			<td class="center" onClick="<%=theActionDetail%>"><%=line.getPeriode()%></td>
			<td class="center" onClick="<%=theActionDetail%>"><%=line.getDateDonnees()%></td>
			<td class="right" onClick="<%=theActionDetail%>"><%=line.getCotisationBrute()%></td>
			<td class="right" onClick="<%=theActionDetail%>"><%=line.getPrestation()%></td>
			<td class="right" onClick="<%=theActionDetail%>"><%=line.getCotisationPeriode()%></td>
			<td class="center" onClick="<%=theActionDetail%>"><%=line.getEtat()%></td>
		</tr>
<%
	}
%>
</liste>
