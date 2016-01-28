<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ3014";
userActionValue="ij.process.validerDecision.executer";
	IJValiderDecisionViewBean viewBean = (IJValiderDecisionViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>

<SCRIPT language="javascript">
function validate() {
	return true;
}
</SCRIPT>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.vb.process.IJValiderDecisionViewBean"%>
<%@page import="globaz.ij.api.decisions.IIJDecisionIJAI"%><ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>




<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_VALIDER_DECISION"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<TR><TD class="areaGlobazBlue" colspan="4">
						<table>
						<TR>
						<TD><ct:FWLabel key="JSP_NO_DECISION"/></TD>
						<TD><INPUT type="text" name="f1" value="<%=viewBean.getIdDecision()%>" class="disabled" readonly></TD>
						<TD><ct:FWLabel key="JSP_ETAT_DECISION"/></TD>
						<TD>
							<%
							
							String state =objSession.getLabel("JSP_ETAT_NON_VALIDE"); 
							 if (viewBean.getIsDecisionValidee()!=null && viewBean.getIsDecisionValidee().booleanValue()) {
								 state =objSession.getLabel("JSP_ETAT_VALIDE");
							 }
							%>
							<INPUT type="text" name="f2" value="<%=state%>" class="disabled" readonly>
						</TD>
						</TR>

						<TR>
						<TD><ct:FWLabel key="JSP_DATE_DECISION"/></TD>
						<TD><INPUT type="text" name="date4" value="<%=viewBean.getDateDecision()%>" class="disabled" readonly></TD>
						<TD><ct:FWLabel key="JSP_DATE_SUR_DOCUMENT"/></TD>
						<TD><INPUT type="text" name="date1" value="<%=viewBean.getDateSurDocument()%>" class="disabled" readonly></TD>
						</TR>

						<TR><TD><ct:FWLabel key="JSP_ETAT_MISE_EN_GED"/></TD>
						<TD><INPUT type="text" name="codeLibelleEtatMiseEnGed" value="<%=viewBean.getCodeLibelleEtatMiseEnGed()%>" class="disabled" readonly></TD>
						<TD colspan = "2">
							<%
							System.out.println(viewBean.getCsEtatMiseEnGed());
							if (!IIJDecisionIJAI.CS_ETAT_ENVOI_DECISION_EN_GED_ATTENTE.equals(viewBean.getCsEtatMiseEnGed())) {%>
								 <ct:FWLabel key="JSP_DATE_MISE_EN_GED"/> <%=viewBean.getDateMiseEnGed()%>
							<%}%>							
						</TD></TR>

						<TR>
						<TD><ct:FWLabel key="JSP_ETAT_SEDEX"/></TD>
						<TD><INPUT type="text" name="codeLibelleEtatSEDEX" value="<%=viewBean.getCodeLibelleEtatSEDEX()%>" class="disabled" readonly></TD>
						<TD colspan="2">
							<%if (!IIJDecisionIJAI.CS_ETAT_ENVOI_DECISION_A_SEDEX_ATTENTE.equals(viewBean.getCsEtatSEDEX())) {%>
								 <ct:FWLabel key="JSP_DATE_ENVOI_SEDEX"/> <%=viewBean.getDateEnvoiSedex()%>
							<%}%>
						</TD>
						</TR>
						</table>
						</TD></TR>
						
						<tr><td colspan="4"><br/></td></tr>
			
						<tr>
							<td colspan="1" class="ongletGlobazBlue" width="10%">
													<table width="10%" border="0">
													<tr>
														<td colspan="7"><strong><ct:FWLabel key="JSP_TRAITEMENT"/></strong></td>								
													</tr>
													</table>
							</td>
							<td colspan="3"></td>
						</tr>

						<tr><td colspan="2" class="areaGlobazBlue" width="40%">
							<table>
							<tr>
								<td><ct:FWLabel key="JSP_TRAITEMENT_1"/></td>
								<td><input type="checkbox" name="isValiderDecision" checked"/></td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_TRAITEMENT_2"/></td>
								<td><input type="checkbox" name="isEnvoyerSEDEX" checked"/></td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_TRAITEMENT_3"/></td>
								<td><input type="checkbox" name="isEnvoyerGED" checked"/></td>
							</tr>
							</table>
						</td>
						<td colspan="2" valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;<ct:FWLabel key="JSP_ADRESSE_EMAIL"/> <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></td>
						</tr>												
						<TR><TD><BR/></TD></TR>
																							
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>