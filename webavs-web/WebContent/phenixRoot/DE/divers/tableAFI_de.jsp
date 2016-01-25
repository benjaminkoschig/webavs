<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP4020";
	globaz.phenix.db.divers.CPTableAFIViewBean viewBean = (globaz.phenix.db.divers.CPTableAFIViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Beitrag - FZS Tabelle  - Detail"
function add() {
	document.forms[0].elements('userAction').value="phenix.divers.tableAFI.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.divers.tableAFI.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.divers.tableAFI.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.divers.tableAFI.chercher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="phenix.divers.tableAFI.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>FZS Tabelle<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
 	   <TR>
            <TD nowrap width="130">Jahr</TD>
            <TD nowrap><INPUT type="text" name="anneeAfi" class="numero" value="<%=viewBean.getAnneeAfi()%>" maxlength="4" size="4"></TD>
            <TD width="50" nowrap></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
           <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR>
            <TD nowrap width="130"></TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
  	 <TR> 
            <TD nowrap width="100">Einkommen</TD>
            <TD width="100" nowrap><INPUT type="text" name="revenuAfi" class="montant" value="<%=viewBean.getRevenuAfi()%>"></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR>
            <TD nowrap width="130"></TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>

          <TR> 
            <TD nowrap width="100"></TD>
            <TD width="100" nowrap>&nbsp;</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR>
            <TD nowrap width="130">Beitragsatz</TD>
            <TD nowrap><INPUT type="text" name="taux" style="text-transform : capitalize;" class="montant" value="<%=viewBean.getTaux()%>"><input type="hidden" name="_creation" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test"></TD>
            <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
	      
	    <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
 <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>