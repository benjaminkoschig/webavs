<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%idEcran="CFA2004";%>
<%@ page import="globaz.musca.db.facturation.*"%>
<%
	//Récupération des beans
	 FAPassageModuleFacturationViewBean viewBean = (FAPassageModuleFacturationViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationModuleFact.executer";

%>
<SCRIPT language="JavaScript">
top.document.title = "Musca - Generierung eines Modul"
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
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Generierung eines Modul für den Ausdruck eines Saldo ESR oder einer Zinsenverfügung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
 		<tr>
			<td>Modul</td>
			<td> <INPUT name="libelleModuleFact" class="Disabled" size="50" value="<%=viewBean.getLibelleModuleFact()%>" type="text"></td>
		</tr>
 		<tr>
			<td>Job-Nr.</td>
			<td> <INPUT name="idPassage" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" type="text"> * <input type="hidden" name="idPassage" value="<%=viewBean.getIdPassage()%>"></td>
		</tr>
        <TR>
            <TD>E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()%>'></TD>
        </TR>
        <TR>
          	<TD>In der DMS senden</TD>
            <TD> <input type="checkbox" name="envoyerGed">
        </TR>
        <INPUT type="hidden" name="callEcran"   value='<%=new Boolean(true)%>'>
					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>