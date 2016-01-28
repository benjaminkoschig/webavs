<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0014";
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<%@page import="globaz.lynx.application.LXApplication"%>
<%@page import="globaz.lynx.utils.LXJournalUtil"%>
<%@page import="globaz.lynx.db.journal.LXJournal"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.facture.facture.lister";
bFind = true;

<%

actionNew += "&idSociete=" + request.getParameter("idSociete");
actionNew += "&idJournal=" + request.getParameter("idJournal");
actionNew += "&forceNew=true";
actionNew += "&csTypeOperation=";

btnNewLabel = "Oranger ESR";

String actionNewFactureBvrRouge = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE;
String actionNewFactureVirement = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT;
String actionNewFactureLsv = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV;
String actionNewFactureCaisse = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE;

actionNew += globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE;

if(!LXJournalUtil.isOuvert(objSession, request.getParameter("idJournal"))){
	bButtonNew = false;
}

%>

top.document.title = "Suche der Rechnungen - " + top.location.href;
// stop hiding -->
</SCRIPT>
<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=request.getParameter("idSociete")%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idJournal' value='<%=request.getParameter("idJournal")%>' checkAdd='no'/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Suche der Rechnungen<%-- /tpl:put --%>
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
		<TR>
			<%@ include file="/lynxRoot/FR/include/enteteJournalSociete.jsp" %>
		</TR>
	
		<TR> 
			<TD height="11" colspan="5"/><hr size="3" width="100%"></TD>
		</TR>
	
		<TR>
	       	<TD>Lieferant</TD>
	       	<TD >
	       		<INPUT type="text" name="likeIdFournisseur" size="25" maxlength="25" tabindex="1"/>
	       	</TD>
	       	<TD>&nbsp;</TD>
	       	<TD>MWST-Nr.</TD>
	       	<TD>
	       		<INPUT type="text" name="numTVA" size="25" maxlength="25" tabindex="1"/>
	       	</TD>
		</TR>
		<TR>
	       	<TD>Rechnungsdatum</TD>
	       	<TD>
	       		<ct:FWCalendarTag name="forDateFacture" doClientValidation="CALENDAR" value="" tabindex="1"/>
	       	</TD>
	       	<TD>&nbsp;</TD>
	       	<TD>Fälligkeit</TD>
	       	<TD>
	       		<ct:FWCalendarTag name="forDateEcheance" doClientValidation="CALENDAR" value="" tabindex="1"/>
	       	</TD>
		</TR>
		<TR>
	       	<TD>Interne Rechnung-Nr.</TD>
	       	<TD>
	       		<INPUT type="text" name="likeIdExterne" size="21" maxlength="20" tabindex="1"/>
	       	</TD>
	       	<TD>&nbsp;</TD>
	       	<TD>Lieferant Rechnung-Nr.</TD>
	       	<TD>
	       		<INPUT type="text" name="likeReferenceExterne" size="41" maxlength="40" tabindex="1"/>
	       	</TD>
		</TR>
		<INPUT type="hidden" name="withoutCsTypeOperation" value="<%=LXOperation.CS_TYPE_NOTEDECREDIT_LIEE%>">
				
				
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>

<%-- tpl:put name="zoneButtons" --%>
<%
	if(LXJournalUtil.isOuvert(objSession, request.getParameter("idJournal"))){
%>				
<ct:ifhasright element="lynx.facture.facture.afficher" crud="u">
	<INPUT type="button" name="btnNewFactureBvrRouge" value="Roter ESR" onClick="document.location.href='<%=actionNewFactureBvrRouge%>'" tabindex="1">
	<INPUT type="button" name="btnNewFactureVirement" value="Überweisung" onClick="document.location.href='<%=actionNewFactureVirement%>'" tabindex="1">
	<INPUT type="button" name="btnNewFactureLSV" value=" LSV " onClick="document.location.href='<%=actionNewFactureLsv%>'" tabindex="1">
	<INPUT type="button" name="btnNewFactureCaisse" value="Kasse" onClick="document.location.href='<%=actionNewFactureCaisse%>'" tabindex="1">
</ct:ifhasright>
<% } %>
<%-- /tpl:put --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>