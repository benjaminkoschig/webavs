<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
		idEcran = "CCI2004";	
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		//String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
    	
		CIImprimerInscriptionActifViewBean viewBean = (CIImprimerInscriptionActifViewBean)session.getAttribute ("viewBean");
		
		userActionValue = "pavo.process.imprimerInscriptionActif.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer inscriptions après décès<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
<%@page import="globaz.pavo.db.process.CIImprimerInscriptionActifViewBean"%><TR>
							<td>E-mail :</td>
							<TD><input type="text" size="40" value="<%=emailAdresse%>" name="eMailAddress"></TD>
						</TR>
						<TR>
							 <td >Année de : </td>
							 <td >
							 	<input type="text" name="dateDebut" size="5" value="<%=viewBean.getDateDebut()%>" >&nbsp;à&nbsp;	
							 	<input type="text" name="dateFin"  size="5" value="<%=viewBean.getDateFin()%>" >
							 </td>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>