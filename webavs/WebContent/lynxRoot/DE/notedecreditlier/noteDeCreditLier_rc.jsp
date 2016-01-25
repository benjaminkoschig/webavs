<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.lynx.db.notedecreditlier.LXNoteDeCreditLierListViewBean"%>
<%@page import="globaz.lynx.db.journal.LXJournal"%>
<%
	idEcran = "GLX0022"; 
	rememberSearchCriterias = true;
	
	LXNoteDeCreditLierListViewBean viewBean = new LXNoteDeCreditLierListViewBean();
	viewBean.setForIdSection(request.getParameter("idSection"));
	viewBean.setForIdOperationSrc(request.getParameter("idOperationSrc"));
	viewBean.setSession(objSession);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.application.LXApplication"%>

<%@page import="globaz.lynx.db.operation.LXOperation"%>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

usrAction = "<%=LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.notedecreditlier.noteDeCreditLier.lister";
bFind = true;

<%

String montantRestant = viewBean.getMontantRestant();

actionNew += "&idSociete=" + request.getParameter("idSociete");
actionNew += "&idFournisseur=" + request.getParameter("idFournisseur");
actionNew += "&montantNote=" + montantRestant;
actionNew += "&idSection=" + request.getParameter("idSection");
actionNew += "&idOperationSrc=" + request.getParameter("idOperationSrc");
actionNew += "&idJournal=" + request.getParameter("idJournal");
actionNew += "&libelleNote=" + request.getParameter("libelleNote");

%>

top.document.title = "Suche der verbundenen Gutschriften" + top.location.href;
// stop hiding -->
</SCRIPT>
<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" value='<%=request.getParameter("idJournal")%>' checkAdd="no"/>
	<ct:menuSetAllParams key="idSociete" value='<%=request.getParameter("idSociete")%>' checkAdd="no"/>
	<ct:menuSetAllParams key="idJournal" value='<%=request.getParameter("idJournal")%>' checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
Suche der verbundenen Gutschriften
<%-- /tpl:put --%>
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
	<TD width="128">Lieferant</TD>
	<TD height="11" colspan="4">	
		<INPUT type="hidden" name="idFournisseur" value="<%= request.getParameter("idFournisseur") %>"/>
		<INPUT type="text" value="<%=globaz.lynx.utils.LXFournisseurUtil.getIdEtLibelleNomComplet(objSession, request.getParameter("idFournisseur"))%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
	</TD>
</TR>
<TR> 
	<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
</TR>
<TR> 
	<TD width="128">Bezeichnung</TD>
	<TD>	
		<INPUT type="text" value="<%= request.getParameter("libelleNote") %>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
	</TD>
	<TD>&nbsp;</TD>
	<TD width="128">Restbetrag</TD>
	<TD>	
		<INPUT type="text" value="<%=globaz.globall.util.JANumberFormatter.fmt(montantRestant, true, true, false, 2)%>" style="width:7cm;text-align : right" class="libelleLongDisabled" readonly="readonly">		
	</TD>
</TR>
<TR> 
	<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
</TR>

<INPUT type="hidden" name="idJournal" value="<%= request.getParameter("idJournal") %>"/>
<INPUT type="hidden" name="idOperationSrc" value="<%= request.getParameter("idOperationSrc") %>"/>
<INPUT type="hidden" name="forIdOperationSrc" value="<%= request.getParameter("idOperationSrc") %>"/>
<INPUT type="hidden" name="libelleNote" value="<%= request.getParameter("libelleNote") %>"/>
<INPUT type="hidden" name="idSection" value="<%= request.getParameter("idSection") %>"/>

<INPUT type="hidden" name="forIdSection" value="<%= request.getParameter("idSection") %>"/>
<INPUT type="hidden" name="forCsTypeOperation" value="<%= LXOperation.CS_TYPE_NOTEDECREDIT_LIEE %>"/>

				
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>