<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0017";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.ordregroupe.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>

<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.ordregroupe.ordreGroupe.lister";
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
		alert(" Die Schuldnerfirma existiert nicht.");
	}
}

top.document.title = "Suche der Sammelaufträge - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der Sammelaufträge<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
				<TD width="128">Schuldnerfirma</TD>
				<TD colspan="3">   
					<%-- Obligation de declarer un viewBean null pour l'include qui suit--%>
					<% LXOrdreGroupeViewBean viewBean = null; %>
					<%@ include file="/lynxRoot/include/societe.jsp" %>
				</TD>
			</TR>
			<TR>
                   	<TD>Sammelauftrag-Nr.</TD>
                   	<TD>                  	
                   		<INPUT type="text" name="forIdOrdreGroupe" size="25" maxlength="25" tabindex="1">
                   	</TD>
                   	<TD>&nbsp;</TD>
					<%
						String selectCsEtatSelect = LXSelectBlockParser.getForCsEtatOrdreGroupeSelectBlock(objSession);
		
						if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsEtatSelect)) {
							out.print("<TD nowrap align=\"left\">Status</TD>");
							out.print("<TD  nowrap >");
							out.print(selectCsEtatSelect);
							out.print("</TD>");
						}
					%>				
			</TR>
			<TR>
                   	<TD>Bezeichnung</TD>
                   	<TD>
                   		<INPUT type="text" name="likeLibelle" size="41" maxlength="40" tabindex="1">
                   	</TD>
                 	<TD>&nbsp;</TD>	
                  	<TD>Benutzer</TD>
                   	<TD>
                   		<INPUT type="text" name="likeProprietaire" size="21" maxlength="20" tabindex="1">
                   	</TD>	                   	
			</TR>
			<TR>
                   	<TD>Erstellungsdatum</TD>
                   	<TD>
                   		<ct:FWCalendarTag name="forDateCreation" doClientValidation="CALENDAR" value="" tabindex="1"/>
                   	</TD>
                   	<TD>&nbsp;</TD>	
                   	<TD>Fälligkeitsdatum</TD>
                   	<TD>
                   		<ct:FWCalendarTag name="forDateEcheance" doClientValidation="CALENDAR" value="" tabindex="1"/>
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