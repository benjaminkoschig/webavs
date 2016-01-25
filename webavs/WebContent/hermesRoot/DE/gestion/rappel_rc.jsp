<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
bButtonNew = false;
idEcran="GAZ0016";
rememberSearchCriterias = true;
String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT language="JavaScript">
var usrAction = "hermes.gestion.rappel.lister";
bNew = false;
top.document.title = "MZR - Mahnung von ausstehende IK's";

bFind = false;
</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Mahnung von ausstehende IK's<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  

  	   <tr>
            <td class="text">SVN :&nbsp;</td>
    		<td width="200" align="left">
    		
    		<%--<input class="input" type="text" name="likeNumeroAvs" value=<%=request.getParameter("likeNumeroAvs")==null?"":request.getParameter("likeNumeroAvs")%>>--%>		    
			    <nss:nssPopup name="likeNumeroAvs" useUpDownKeys="false"
				avsMinNbrDigit="5" nssMinNbrDigit="8" />  
    		
    		</td>
    		<td class="text">&nbsp;SZ ZIK :&nbsp;</td>
    		<td><input class="input" type="text" name="forMotif" size="10"></td>
			
  	   </tr>
  	   
  	   <tr>
    		<td class="text">Kassen-Nr. :&nbsp;</td>
    		<td ><input class="input" type="text" name="forNumeroCaisse" size="15"></td>
			<td class="text">&nbsp;Datum :&nbsp;</td>
			<td>
			<ct:FWCalendarTag name="forDate" doClientValidation="CALENDAR" value=""/></td>
  	  </tr>
	  <tr>
		<%if("true".equals(objSession.getApplication().getProperty("service.input"))){%>
			<TD>Dienst :&nbsp;</TD>
			<TD>
				<INPUT type="text" name="forService" size="5" maxlength="3">
			</TD>
			<%} else {%>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
			<%}%>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
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