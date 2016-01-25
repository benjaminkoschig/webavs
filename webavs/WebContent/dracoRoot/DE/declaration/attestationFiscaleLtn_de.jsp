<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		DSAttestationFiscaleLtnViewBean viewBean = (DSAttestationFiscaleLtnViewBean)session.getAttribute("viewBean");
		userActionValue = "draco.declaration.attestationFiscaleLtn.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
		idEcran = "CDS2002";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.draco.db.declaration.DSAttestationFiscaleLtnViewBean"%>

<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">
	<ct:menuSetAllParams key="idDeclaration" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der BGSA Steuerbestätigungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								Mitglied
							</TD>
							<TD>
								<input name="affilieNumero"  value="<%=viewBean.getAffilie().getAffilieNumero().toString()%>" size="15" class="disabled" readonly />
							</TD>
						</TR>
						<TR>
							<TD>
								Dokumentdatum
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>" />
							</TD>
						</TR>
						<TR>
							<TD>
								E-Mail Adresse
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=!globaz.jade.client.util.JadeStringUtil.isBlank(emailAdresse)?emailAdresse:""%>" size = "40">
								
							</TD>
						</TR>
						<TR>
							<TD>
								Simulation
							</TD>
							<TD>
								<input name="simulation" size="20" type="checkbox" checked style="text-align : right;">
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>