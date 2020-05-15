<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.vb.process.APGenererDroitPandemieFinDroitViewBean" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP3010";

	userActionValue="apg.droits.finDroit.executer";
	APGenererDroitPandemieFinDroitViewBean viewBean = (globaz.apg.vb.process.APGenererDroitPandemieFinDroitViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_FIN_DROIT_PAN"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>

<TR>
	<td  width="25%" />
	<TD>
		<ct:FWLabel key="JSP_DATE_FIN_DROIT_PANDEMIE"/>&nbsp;
	</TD>
	<TD>
		<ct:inputText name="dateFin"  notation="data-g-calendar='mandatory:true'" />&nbsp;
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>