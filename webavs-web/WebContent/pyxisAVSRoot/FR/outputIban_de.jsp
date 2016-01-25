 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran="A completer";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	//Récupération des beans
	TIListePaiementIBANOutputViewBean viewBean = (TIListePaiementIBANOutputViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "pyxisAVS.iban.outputprocess.executer";

%>

<%@page import="globaz.pyxisavs.process.TIListePaiementIBANOutputProcess"%>
<%@page import="globaz.pyxisavs.db.iban.TIListePaiementIBANOutputViewBean"%><SCRIPT language="JavaScript">
top.document.title = "Process de conversion des numéros de compte en IBAN"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
function reloadPage() {
		fieldFormat(document.all('dateEnvoi'),'CALENDAR');
		document.all('dateRetour').value = '';
		document.forms[0].elements('userAction').value = 'pyxisAVS.iban.inputprocess.reload';
		document.forms[0].submit();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Process de création du fichier des adresses de paiement<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD>E-mail :</TD>
				<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
			</TR>
			
			<TR>
				<td>Emplacement fichier : </td>
				<td><input type="file" id="path" name="path" value=<%= viewBean.getPath()%>> </td>
			</TR>
          
			<tr><TD>&nbsp;</TD></tr><tr ><TD>&nbsp;</TD></tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>