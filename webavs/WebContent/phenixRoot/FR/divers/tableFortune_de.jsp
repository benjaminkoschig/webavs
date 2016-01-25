<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP4008";
	globaz.phenix.db.divers.CPTableFortuneViewBean viewBean = (globaz.phenix.db.divers.CPTableFortuneViewBean)session.getAttribute ("viewBean");
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
top.document.title = "Cotisation - Table fortune - Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.divers.tableFortune.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.divers.tableFortune.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.divers.tableFortune.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.divers.tableFortune.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.divers.tableFortune.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Table pour fortune<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR>
            <TD nowrap width="130">Année</TD>
            <TD nowrap><INPUT type="text" name="anneeFortune" class="numero" value="<%=viewBean.getAnneeFortune()%>" maxlength="4" size="4"></TD>
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
            <TD nowrap width="130">Canton</TD>
            <TD width="100" nowrap> 
		<ct:FWCodeSelectTag name="canton"
			defaut="<%=viewBean.getCanton()%>"
			wantBlank="<%=true%>"
			codeType="PYCANTON"
			libelle="codeLibelle"
		/></TD>
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
            <TD nowrap width="130">Taux agricole</TD>
            <TD width="100" nowrap> 
              <INPUT type="text" name="tauxAgricole" style="text-transform : capitalize;" class="taux" value="<%=viewBean.getTauxAgricole()%>"></TD>
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
            <TD nowrap width="130">Taux non agricole</TD>
            <TD nowrap>  <input type="hidden" name="_creation" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test"><INPUT type="text" name="tauxNonAgricole" class="taux" value="<%=viewBean.getTauxNonAgricole()%>"></TD>
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