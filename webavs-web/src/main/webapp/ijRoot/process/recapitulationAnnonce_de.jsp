<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ2002";

	userActionValue="ij.process.recapitulationAnnonce.executer";
	globaz.ij.vb.process.IJRecapitulationAnnonceViewBean viewBean = (globaz.ij.vb.process.IJRecapitulationAnnonceViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RECAPITULATION_ANNONCE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%><TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
							    <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/>&nbsp;
							    <ct:FWCalendarTag name="forMoisAnneeComptable" value="" displayType="MONTH"/>&nbsp;
							    <ct:FWLabel key="JSP_FORMAT_MOISANNEECOMPTABLE"/>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>