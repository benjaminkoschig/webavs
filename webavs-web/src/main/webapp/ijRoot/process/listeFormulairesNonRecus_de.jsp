<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ2000";
	userActionValue=globaz.ij.servlet.IIJActions.ACTION_LISTE_FORMULAIRES_NON_RECUS + ".executer";
	globaz.ij.vb.process.IJListeFormulairesNonRecusViewBean viewBean = (globaz.ij.vb.process.IJListeFormulairesNonRecusViewBean)(session.getAttribute("viewBean"));

	String[] mois = viewBean.getMoisList();
	String[] annees = viewBean.getAnneesList();
	String moisCourant = viewBean.getMoisCourant();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GLFA_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_GLFA_D_EMAIL"/>&nbsp;
							    <INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>">
							</TD>
							<TD></TD>
						</TR>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_GENERER_ATTESTATIONS_MOIS"/>&nbsp;
								<SELECT name="forMois">
									<%for (int i=0; i<mois.length; i=i+2){%>
									<OPTION value="<%=mois[i]%>" <%=mois[i].equalsIgnoreCase(moisCourant)?"selected":""%>><%=mois[i+1]%></OPTION>
									<%}%>
								</SELECT>
								<ct:FWLabel key="JSP_GENERER_ATTESTATIONS_ANNEE"/>&nbsp;
								<SELECT name="forAnnee">
									<%for (int i=0; i<annees.length; i=i+2){%>
									<OPTION value="<%=annees[i]%>" <%=i==2?"selected":""%>><%=annees[i+1]%></OPTION>
									<%}%>
								</SELECT>
							</TD>
						</TR>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_GFO_D_DATE_DOCUMENT"/>&nbsp;
								<ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>"/>
							</TD>
						</TR>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>