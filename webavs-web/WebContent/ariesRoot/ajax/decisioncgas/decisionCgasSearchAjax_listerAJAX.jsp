<%@page import="ch.globaz.aries.business.constantes.ARDecisionType"%>
<%@page import="ch.globaz.aries.business.constantes.ARDecisionEtat"%>
<%@page import="globaz.aries.vb.decisioncgas.ARDecisionCgasSearchLineViewBean"%>
<%@page import="globaz.aries.vb.decisioncgas.ARDecisionCgasSearchAjaxViewBean"%>
<%@page language="java" contentType="text/xml;charset=ISO-8859-1"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<liste>
<%
ARDecisionCgasSearchAjaxViewBean viewBean = (ARDecisionCgasSearchAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	
	for(ARDecisionCgasSearchLineViewBean line: viewBean.getLineViewBeans()) {
		String theActionDetail = "window.top.fr_main.window.location='aries?userAction=aries.decisioncgas.decisionCgas.afficher&amp;idAffiliation=" + line.getIdAffiliation() + "&amp;id=" + line.getIdDecision() + "&amp;idDecisionCgasRectifiee=" + line.getIdDecisionRectifiee() +"'";	

		String lineStyle="";
		if(ARDecisionEtat.RECTIFIEE.getCodeSystem().equalsIgnoreCase(line.getCodeSystemEtat())){
			lineStyle = "color:#999999;";
		}
		
		lineStyle = "style='" + lineStyle + "'";
%>
		<tr <%=lineStyle%>  idEntity="<%=line.getIdDecision()%>">
			<td>
				<ct:menuPopup menu="aries-optionDecision">
					<ct:menuParam key="idDecisionCgasRectifiee" value="<%=line.getIdDecision()%>"/>
					<ct:menuParam key="idAffiliation" value="<%=line.getIdAffiliation()%>"/>
					
					<% if (!ARDecisionEtat.COMPTABILISEE.equals(line.getCodeSystemEtat()) && !ARDecisionEtat.REPRISE.equals(line.getCodeSystemEtat())) { %>
						<ct:menuExcludeNode nodeId="option_rectificative" />
					<% } %>
					
					<% if (!((ARDecisionEtat.COMPTABILISEE.equals(line.getCodeSystemEtat()) || ARDecisionEtat.REPRISE.equals(line.getCodeSystemEtat()) ) && (ARDecisionType.PROVISOIRE_RECTIFICATIVE.equals(line.getCodeSystemType()) || ARDecisionType.PROVISOIRE.equals(line.getCodeSystemType())))) { %>
						<ct:menuExcludeNode nodeId="option_definitive" />
					<% } %>
					
					
				</ct:menuPopup>
			</td>
			<td class="left" onClick="<%=theActionDetail%>"><%=line.getAnnee()%>
				<span data-g-note="idExterne:<%=line.getIdDecision()%>, tableSource:ARDECI, inList: true"> </span>
			</td>
			<td class="left"  onClick="<%=theActionDetail%>"><%=line.getType()%></td>
			<td class="left"  onClick="<%=theActionDetail%>"><%=line.getPeriode()%></td>
			<td class="left"  onClick="<%=theActionDetail%>"><%=line.getDateDecision()%></td>
			<td class="right" onClick="<%=theActionDetail%>"><%=line.getCotisationPeriode()%></td>
			<td class="left"  onClick="<%=theActionDetail%>"><%=line.getEtat()%></td>
		</tr>
<%
	}
%>
</liste>
