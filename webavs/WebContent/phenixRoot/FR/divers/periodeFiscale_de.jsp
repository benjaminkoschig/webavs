<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP4016";
	globaz.phenix.db.divers.CPPeriodeFiscaleViewBean viewBean = (globaz.phenix.db.divers.CPPeriodeFiscaleViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
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
top.document.title = "Cotisation - Periode Fiscale - Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.divers.periodeFiscale.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.divers.periodeFiscale.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.divers.periodeFiscale.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.divers.periodeFiscale.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.divers.periodeFiscale.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Periode fiscale<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap width="150">Numero IFD</TD>
            <TD nowrap><INPUT type="text" name="numIfd" value="<%=viewBean.getNumIfd()%>" maxlength="4" size="4"></TD>
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
            <TD nowrap width="160">Année de début du revenu</TD>
            <TD width="100" nowrap><INPUT type="text" name="anneeRevenuDebut" value="<%=viewBean.getAnneeRevenuDebut()%>" size="4" maxlength="4"></TD>
	      <TD width="50"></TD>
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
            <TD nowrap width="150">Année de fin du revenu</TD>
            <TD width="100" nowrap><INPUT type="text" name="anneeRevenuFin" value="<%=viewBean.getAnneeRevenuFin()%>" size="4" maxlength="4"></TD>
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
            <TD nowrap width="150">Année de décision</TD>
            <TD nowrap><INPUT type="text" name="anneeDecisionDebut" style="text-transform : capitalize;" value="<%=viewBean.getAnneeDecisionDebut()%>" size="4" maxlength="4"><input type="hidden" name="_creation" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test"></TD>
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
            <td nowrap width="125">
		<INPUT type="hidden" name="_pos" value="">
		<INPUT type="hidden" name="_meth" value="">
		<INPUT type="hidden" name="_act" value="">
		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
		</td>
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