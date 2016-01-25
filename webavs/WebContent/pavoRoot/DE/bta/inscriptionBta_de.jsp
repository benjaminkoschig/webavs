<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.pavo.db.bta.CIRequerantBta"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.pavo.db.bta.CIInscriptionBtaViewBean"%>
<%@page import="java.util.*"%>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%
 	CIInscriptionBtaViewBean viewBean = (CIInscriptionBtaViewBean)session.getAttribute("viewBean");
	userActionValue = "pavo.bta.inscriptionBta.executer";
	idEcran = "CCI3010";
	String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>BGS IK-Buchungen für das Vorjahr <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
												
						
<%@page import="globaz.pavo.db.bta.CIDossierBta"%><tr>
							<td>
								E-Mail Adresse
							</td>
							<td>
								<input type="text" name="eMailAddress" value="<%=emailAdresse%>" size = "40">
							</td>
						</tr>
						<tr>
							<td>
								Simulation
							</td>
							<td>
								<input name="simulation" size="20" type="checkbox" checked style="text-align : right;">
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>