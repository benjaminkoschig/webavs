<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
		idEcran = "CCI2006";
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
		globaz.pavo.db.compte.CIMasseListeViewBean viewBean = (globaz.pavo.db.compte.CIMasseListeViewBean)session.getAttribute ("viewBean");
		userActionValue = "pavo.compte.compteIndividuel.compareMasseSalarialeListeExecuter.executer";
		String eMailAddress = objSession.getUserEMail()!=null?objSession.getUserEMail():"";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Statistiques CI<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<td>E-mail 2</td>
							<TD><input type="text" size="40" value="<%=eMailAddress%>" name="eMailAddress"></TD>
						</TR>
						<TR>
							<TD>Date de début</TD>
							<TD><ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>" /></TD>
						</TR>
						<TR>
							<TD>Date de fin</TD>
							<TD><ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>" /></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>