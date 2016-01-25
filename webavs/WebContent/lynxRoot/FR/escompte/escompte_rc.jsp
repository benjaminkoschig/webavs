<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0028";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<% if (!JadeStringUtil.isBlank(request.getParameter("idOrdreGroupe"))) { %>
<ct:menuChange displayId="options" menuId="LX-OrdreGroupe" showTab="options">
	<ct:menuSetAllParams key='selectedId' value='<%= request.getParameter("idOrdreGroupe") %>'/>
	<ct:menuSetAllParams key='idSociete' value='<%= request.getParameter("idSociete") %>'/>
	<ct:menuSetAllParams key='idOrdreGroupe' value='<%= request.getParameter("idOrdreGroupe") %>'/>
	<ct:menuSetAllParams key='idOrganeExecution' value='<%= request.getParameter("idOrganeExecution") %>'/>
</ct:menuChange>
<% } %>
		
<% if (!JadeStringUtil.isBlank(request.getParameter("idJournal"))) { %>
<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=request.getParameter("idSociete")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idJournal' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
</ct:menuChange>
<% } %>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.escompte.escompte.lister";
bFind = true;

top.document.title = "Recherche des escomptes - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Recherche des escomptes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	</tbody>
</table>
<script language="JavaScript">
	element = document.getElementById("subtable");
  	element.style.height = "10px";
  	element.style.width = "100%";
</script>
<table cellspacing="0" cellpadding="0" style="height: 100px; width: 100%">
	<tbody>
	
	<%@ include file="/lynxRoot/FR/include/enteteOrdreGroupe.jsp" %>	
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR>
		<TD>Fournisseur</TD>
        <TD><INPUT type="text" name="likeIdFournisseur" size="21" maxlength="20" tabindex="1"></TD>
		<TD>&nbsp;</TD>	
		<TD>Montant</TD>
		<TD><INPUT type="text" name="forMontant" size="21" maxlength="20" tabindex="1"></TD>
	</TR>
	<TR>
		<TD>Date facture</TD>
        <TD><ct:FWCalendarTag name="forDateFacture" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
		<TD>&nbsp;</TD>	
		<TD>No. Facture Fournisseur</TD>
		<TD><INPUT type="text" name="likeReferenceExterne" size="41" maxlength="40" tabindex="1"></TD>
	</TR>
	<TR>
		<TD>No. Interne</TD>
        <TD><INPUT type="text" name="likeIdExterne" size="21" maxlength="20" tabindex="1"></TD>
		<TD colspan="3">&nbsp;</TD>	
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>