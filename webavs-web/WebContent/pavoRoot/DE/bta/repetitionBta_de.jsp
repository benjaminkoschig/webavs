<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.pavo.db.bta.CIRepetitionBtaViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		CIRepetitionBtaViewBean viewBean = (CIRepetitionBtaViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.bta.repetitionBta.executer";
		idEcran = "CCI2015";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Audruck der Wiederholungsbriefe<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<TD>
								E-Mail Adresse
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=emailAdresse%>" size = "40">
							</TD>
						</TR>
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