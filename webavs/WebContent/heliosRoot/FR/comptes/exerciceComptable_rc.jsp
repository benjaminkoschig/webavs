<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%

	idEcran="GCF0010";
String tous = "Tous";
if (languePage.equalsIgnoreCase("de")) {
	tous = "Alle";
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<SCRIPT>
usrAction = "helios.comptes.exerciceComptable.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
		  		<%if(request.getParameter(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_DESTINATION) !=null) {%>
					Veuillez d'abord choisir l'exercice comptable avec lequel vous désirez travailler					
				<% } else { %>	
					Aperçu des exercices comptables
				<% }%>
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


				<TR>
            <TD width="108">Pour le mandat</TD>
            <TD width="282">
					 <ct:FWListSelectTag name="forIdMandat"
						 defaut=""
						 data="<%=globaz.helios.translation.CGListes.getMandatListe(session,tous)%>"/>
				  </TD>
            <TD nowrap width="82" align="right">A partir du</TD>
            <TD nowrap colspan="2" width="176">
				    <ct:FWCalendarTag name="fromDateDebut" 
				      value="" 
				      doClientValidation="CALENDAR"/>                                
				  </TD>
          </TR>
				<TR>
	      <TD colspan=2>
			Afficher uniquement les exercices ouverts
			<input type="checkbox" name="forExerciceOuvert" checked>
		</TD>
            <TD colspan="2"></TD>
          </TR>

                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>