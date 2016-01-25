 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
bButtonNew = false;
idEcran="GAZ0019";
rememberSearchCriterias=true;
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
usrAction = "hermes.gestion.impressionci.lister";
top.document.title='ARC - CI additionnels';
bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche CI additionnel<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
				
					  <tr >
					    <td class="text">NSS :&nbsp;</td>
					    <td width="200" align="left" >
					   	<%-- <input class="input" type="text" name="likeNumeroAvs">--%>					    
					    <nss:nssPopup name="likeNumeroAvs" useUpDownKeys="false"
						 avsMinNbrDigit="5" nssMinNbrDigit="8" />  					    
					    </td>
					    <td class="text">&nbsp;Motif :&nbsp;</td>
					    <td><input class="input" type="text" name="forMotif" size="10"></td>
					  </tr>
					  
					  <tr>
					  	<td class="text">Caisse :&nbsp;</td>
					    <td align="left"><input class="input" type="text" name="forCaisse" size="15"></td>
					    <td class="text">&nbsp;Date :&nbsp;</td>
					    <td><ct:FWCalendarTag name="forDate" value="" doClientValidation="CALENDAR" />
						</td>					
					  </tr>
					   <tr>
					    <td>&nbsp;</td>
					    <td>&nbsp;</td>
					    <td>&nbsp;</td>
					    <td>&nbsp;</td>
					  </tr>
					  <TR>
					  	<td colspan="2">Chercher dans l'archivage</td>
    					<td><input type="checkbox" name="isArchivage" value="true" >
    					</td>
    					<td></td>
					  </TR>
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