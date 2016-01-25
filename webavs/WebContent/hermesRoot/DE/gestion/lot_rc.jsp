
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script language="JavaScript">
top.document.title = "MZR - Job suchen";
</script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
usrAction = "hermes.gestion.lot.lister";
bFind = true;
</SCRIPT>
<%
bButtonNew = false;
idEcran="GAZ0001";
rememberSearchCriterias = true;
%>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Job suchen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr>
            <td>ZAS-Datum&nbsp;</td>
            <td>
            <%String defaultVon = request.getParameter("von")==null?(request.getParameter("dateEnvoi")==null?globaz.globall.util.JACalendar.todayJJsMMsAAAA():request.getParameter("dateEnvoi")):request.getParameter("von");%>
              <ct:FWCalendarTag name="von" doClientValidation="CALENDAR" value="<%=defaultVon%>"/>
              <input type="hidden" name="triLot" value="date">
            </td>           
            <td>&nbsp;Typ&nbsp;</td>
            <td> 
            <%String defaultType = request.getParameter("typeLot")==null?"":request.getParameter("typeLot");%>
            	<ct:FWCodeSelectTag name="typeLot" codeType="HETYPE" wantBlank="true" defaut="<%=defaultType%>"/> 
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
          	<td colspan="2">Archivsuche</td>
    	   	<td><input type="checkbox" name="isArchivage" value="true" ></td>
    	   	<td>&nbsp;</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>
              &nbsp;<input type="hidden" name="from">
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>