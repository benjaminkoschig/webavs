<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0010";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.journal.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>

<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.journal.journal.lister";
bFind = true;

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("forIdSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" La société débitrice n'existe pas.");
	}
}

top.document.title = "Recherche des journaux - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des journaux<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
				<TD width="128">Soci&eacute;t&eacute; d&eacute;bitrice</TD>
				<TD colspan="3">   
					<%-- Obligation de declarer un viewBean null pour l'include qui suit--%>
					<% LXJournalViewBean viewBean = null; %>
					<%@ include file="/lynxRoot/include/societe.jsp" %>
				</TD>
			</TR>
			<TR>
                   	<TD>No journal</TD>
                   	<TD>                  	
                   		<INPUT type="text" name="forIdJournal" size="25" maxlength="25" tabindex="1">
                   	</TD>
                   	<TD>&nbsp;</TD>
					<%
						String selectCsEtatSelect = LXSelectBlockParser.getForCsEtatJournalSelectBlock(objSession);
		
						if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsEtatSelect)) {
							out.print("<TD nowrap align=\"left\">Etat</TD>");
							out.print("<TD  nowrap >");
							out.print(selectCsEtatSelect);
							out.print("</TD>");
						}
					%>
					
			</TR>
			<TR>
                   	<TD>Libell&eacute;</TD>
                   	<TD>
                   		<INPUT type="text" name="likeLibelle" size="41" maxlength="40" tabindex="1">
                   	</TD>
                 	<TD>&nbsp;</TD>	
                   	<TD>Propri&eacute;taire</TD>
                   	<TD>
                   		<INPUT type="text" name="likeProprietaire" size="21" maxlength="20" tabindex="1">
                   	</TD>	
                   	
			</TR>
			<TR>
                   	<TD>Date cr&eacute;ation</TD>
                   	<TD>
                   		<ct:FWCalendarTag name="forDateCreation" doClientValidation="CALENDAR" value="" tabindex="1"/>
                   	</TD>
                   	<TD>&nbsp;</TD>	
                   	<TD>Date comptable</TD>
                   	<TD>
                   		<ct:FWCalendarTag name="forDateValeurCG" doClientValidation="CALENDAR" value="" tabindex="1"/>
                   	</TD>
                   
			</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>